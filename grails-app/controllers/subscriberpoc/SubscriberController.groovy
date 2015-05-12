package subscriberpoc

import org.apache.commons.logging.LogFactory
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SubscriberController {

    static defaultAction = "create"

    private static final log = LogFactory.getLog(this)

    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static responseFormats = ['html', 'json', 'xml']

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Subscriber.list(params), model: [subscriberInstanceCount: Subscriber.count()]
    }


    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def show(Subscriber subscriberInstance) {
        respond subscriberInstance
    }

    def create() {
        respond new Subscriber(params)
    }

    @Transactional
    def save(Subscriber subscriberInstance) {
        if (subscriberInstance == null) {
            notFound()
            return
        }

        if (subscriberInstance.hasErrors()) {
            respond subscriberInstance.errors, view: 'create'
            return
        }

        subscriberInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'subscriber.label', default: 'Subscriber'), subscriberInstance.id])
                respond subscriberInstance, view: 'create'
            }
            '*' { respond subscriberInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def edit(Subscriber subscriberInstance) {
        respond subscriberInstance
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
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

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def delete(Subscriber subscriberInstance) {

        if (subscriberInstance == null) {
            notFound()
            return
        }

        subscriberInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Subscriber.label', default: 'Subscriber'), subscriberInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'subscriber.label', default: 'Subscriber'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
