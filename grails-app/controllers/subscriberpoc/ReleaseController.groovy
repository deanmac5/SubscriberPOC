package subscriberpoc

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ReleaseController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Release.list(params), model: [releaseInstanceCount: Release.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def show(Release releaseInstance) {
        respond releaseInstance
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        respond new Release(params)
    }

    @Transactional
    def save(Release releaseInstance) {
        if (releaseInstance == null) {
            notFound()
            return
        }

        if (releaseInstance.hasErrors()) {
            respond releaseInstance.errors, view: 'create'
            return
        }

        releaseInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'release.label', default: 'Release'), releaseInstance.id])
                redirect releaseInstance
            }
            '*' { respond releaseInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit(Release releaseInstance) {
        respond releaseInstance
    }

    @Transactional
    def update(Release releaseInstance) {
        if (releaseInstance == null) {
            notFound()
            return
        }

        if (releaseInstance.hasErrors()) {
            respond releaseInstance.errors, view: 'edit'
            return
        }

        releaseInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Release.label', default: 'Release'), releaseInstance.id])
                redirect releaseInstance
            }
            '*' { respond releaseInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def delete(Release releaseInstance) {

        if (releaseInstance == null) {
            notFound()
            return
        }

        releaseInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Release.label', default: 'Release'), releaseInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'release.label', default: 'Release'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
