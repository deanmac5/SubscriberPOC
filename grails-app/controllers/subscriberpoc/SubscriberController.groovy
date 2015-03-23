package subscriberpoc

import com.drew.metadata.Age
import org.apache.commons.logging.LogFactory

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SubscriberController {

    private static final log = LogFactory.getLog(this)

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Subscriber.list(params), model: [subscriberInstanceCount: Subscriber.count()]
    }

    def show(Subscriber subscriberInstance) {
        respond subscriberInstance
    }

    def create() {

        respond new Subscriber(params)
    }

    @Transactional
    def save(Subscriber subscriberInstance) {
        log.debug("Parameters == " + params)
        if (subscriberInstance == null) {
            notFound()
            return
        }

        if (subscriberInstance.hasErrors()) {
            respond subscriberInstance.errors, view: 'create'
            return
        }

        log.debug("selected checkboxes " + params?.agency)
        for(i in params?.agency){
            subscriberInstance.addToSubscriptions(Agency.get(i))
        }

        log.debug("Object with subscriptions" + subscriberInstance)
        subscriberInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'subscriber.label', default: 'Subscriber'), subscriberInstance.id])
                redirect subscriberInstance
            }
            '*' { respond subscriberInstance, [status: CREATED] }
        }
    }

    def edit(Subscriber subscriberInstance) {
        respond subscriberInstance
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
