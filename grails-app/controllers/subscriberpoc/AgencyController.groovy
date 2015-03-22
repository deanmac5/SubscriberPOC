package subscriberpoc

import grails.rest.RestfulController
import org.apache.commons.logging.LogFactory

class AgencyController extends RestfulController {

    private static final log = LogFactory.getLog(this)

    static scaffold = true
    static responseFormats = ['html', 'json', 'xml']
    AgencyController() {
        super(Agency)
    }

}
