package subscriberpoc

import grails.rest.RestfulController

class SiteController extends RestfulController {

    static scaffold = true
    static responseFormats = ['html', 'json', 'xml']

    SiteController() {
        super(Site)
    }
}
