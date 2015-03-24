package subscriberpoc

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class UserController extends RestfulController {

    private static final log = LogFactory.getLog(this)



    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", index: "GET"]

    static responseFormats = ['html', 'json', 'xml']

    UserController() {
        super(User)
    }

    def create() {

        respond new User(params)
    }


    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond User.list(params), model: [userInstanceCount: User.count()]
    }

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    def show(User userInstance) {
        respond userInstance
    }



    @Secured(['ROLE_USER','ROLE_ADMIN'])
    @Transactional
    def save(User userInstance) {
        log.debug("Parameters == " + params)
        if (userInstance == null) {
            notFound()
            return
        }

        if (userInstance.hasErrors()) {
            respond userInstance.errors, view: 'create'
            return
        }

        log.debug("selected checkboxes " + params?.agency)
        for(i in params?.agency){
            userInstance.addToSubscriptions(Agency.get(i))
        }

        log.debug("Object with subscriptions" + userInstance)
        userInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'subscriber.label', default: 'User'), userInstance.id])
                redirect userInstance
            }
            '*' { respond userInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    def edit(User userInstance) {
        respond userInstance
    }

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    @Transactional
    def update(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }

        if (userInstance.hasErrors()) {
            respond userInstance.errors, view: 'edit'
            return
        }

        userInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'User.label', default: 'User'), userInstance.id])
                redirect userInstance
            }
            '*' { respond userInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def delete(User userInstance) {

        if (userInstance == null) {
            notFound()
            return
        }

        userInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'User.label', default: 'User'), userInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'User.label', default: 'User'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
