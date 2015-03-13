package subscriberpoc

import grails.rest.RestfulController

class AgencyController extends RestfulController {

    static scaffold = true
    static responseFormats = ['html', 'json', 'xml']
    AgencyController() {
        super(Agency)
    }

}
