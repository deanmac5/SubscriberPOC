package subscriberpoc

import grails.rest.RestfulController

class ReleaseController extends RestfulController {

    static scaffold = true
    static responseFormats = ['html', 'json', 'xml']
    ReleaseController() {
        super(Release)
    }
}
