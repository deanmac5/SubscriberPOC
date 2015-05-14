package subscriberpoc

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

@Transactional(readOnly = true)
class SubscriberController {


    private static final log = LogFactory.getLog(this)
    def mailService
    def springSecurityService


    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", index: "GET"]
    static responseFormats = ['html', 'json', 'xml']

    def index() {
        [subscriberInstance: new Subscriber(params)]
    }

    @Secured(['ROLE_ADMIN'])
    def list(Integer max) {

        def authenticatedUser = User.findByUsername(springSecurityService.principal?.username)

        def subscribers = []
        params.max = Math.min(max ?: 10, 100)
        subscribers = Subscriber.list(params)


        respond subscribers

    }

    def signup() {
        def subscriberInstance = new Subscriber(params)
        subscriberInstance.verified = false;
        subscriberInstance.confirmCode = UUID.randomUUID().toString()
        if (!subscriberInstance.save(flush: true)) {
            return
        }

        mailService.sendMail {
            to subscriberInstance.email
            subject "New Subscription Confirmation"
            html g.render(template: "mailtemplate", model: [code: subscriberInstance.confirmCode])
        }

        render(view: "success", model: [subscriberInstance: subscriberInstance])
        redirect(action: "success")
    }

    def success() {

        flash.message = 'Your subscription is almost created. Please complete the process ' +
                'using the email we have now sent to your email address'
        render(view: 'index')
    }

    def confirm(String id) {
        Subscriber subscriberInstance = Subscriber.findByConfirmCode(id)
        if (!subscriberInstance) {
            render(view: "success", model: [message: 'Problem confirming account'])
            return
        }

        subscriberInstance.verified = true
        if (!subscriberInstance.save(flush: true)) {
            render(view: "success", model: [message: 'Problem activating account'])
            return
        }
        flash.message = 'Your subscription has been successfully activated'
        render(view: 'index')
    }

    def modify(String id) {
        Subscriber subscriberInstance = Subscriber.findByConfirmCode(id)
        if (!subscriberInstance) {
            render(view: "success", model: [message: 'Problem accessing account'])
            return
        }
        render(view: "edit", model: [subscriberInstance: subscriberInstance])

    }

    @Transactional
    def update(Subscriber subscriberInstance) {
        if (subscriberInstance == null) {
            notFound()
            return
        }

        if (subscriberInstance.hasErrors()) {
            respond subscriberInstance.errors, view: 'edit'
            return
        }

        subscriberInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Subscriber.label', default: 'Subscriber'), subscriberInstance.id])
                redirect subscriberInstance
            }
            '*' { respond subscriberInstance, [status: OK] }

        }
    }

    def show(Subscriber subscriberInstance) {
        respond subscriberInstance
    }

    def remove(String id) {
        Subscriber subscriberInstance = Subscriber.findByConfirmCode(id)
        if (subscriberInstance == null) {
            notFound()
            return
        }

        subscriberInstance.delete flush: true

        request.withFormat {

            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Subscriber.label', default: 'Subscriber'), subscriberInstance.id])
                render(view: 'index')
            }
        }
        render(view: 'index')
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'Subscriber.label', default: 'Subscriber'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

}
