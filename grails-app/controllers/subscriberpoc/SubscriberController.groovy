package subscriberpoc

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

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
//        if (!subscriberInstance.save(flush: true)) {
//            return
//        }

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
            return
        }

        subscriberInstance.verified = true
        if (!subscriberInstance.save(flush: true)) {
            render(view: "success", model: [message: 'Problem activating account'])
            return
        }
        render(view: "success", model: [message: 'Your subscription has been successfully activated'])
    }
}
