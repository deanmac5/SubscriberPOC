package subscriberpoc

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class UserController extends RestfulController {

    static defaultAction = "create"

    private static final log = LogFactory.getLog(this)

    def springSecurityService


    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", index: "GET"]

    static responseFormats = ['html', 'json', 'xml']

    UserController() {
        super(User)
    }

    def create() {

        respond new User(params)
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def index(Integer max) {

        def authenticatedUser = User.findByUsername(springSecurityService.principal?.username)

        def users = []
        params.max = Math.min(max ?: 10, 100)
        if (SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN')) {
            users = User.list(params)
        } else {
            users.add(User.findById(authenticatedUser?.id))
        }

            respond users

    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def show(User userInstance) {
        def authenticatedUser = User.findByUsername(springSecurityService.principal?.username)

        if (SpringSecurityUtils.ifAnyGranted('ROLE_USER')) {
            userInstance = User.findById(authenticatedUser?.id)
        }
        respond userInstance
    }

//    @Secured(['ROLE_USER','ROLE_ADMIN'])
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
        for (i in params?.agency) {
            userInstance.addToSubscriptions(Agency.get(i))
        }

        log.debug("Object with subscriptions" + userInstance)
        userInstance.save flush: true

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), userInstance.id])
                    redirect view: ('create')
                }
                '*' { respond userInstance, [status: CREATED] }
            }

    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def edit(User userInstance) {
        def authenticatedUser = User.findByUsername(springSecurityService.principal.username)

        if (SpringSecurityUtils.ifAnyGranted('ROLE_USER')) {
            userInstance = User.findById(authenticatedUser.id)
        }
        respond userInstance
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
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

//    def success(User userInstance){
//        log.debug("User should be saved at this point")
//        redirect(controller: "User", action: "")
//    }

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
