package subscriberpoc

import grails.rest.RestfulController
import org.apache.commons.logging.LogFactory

class SiteController extends RestfulController {

    private static final log = LogFactory.getLog(this)

    static scaffold = true
    static responseFormats = ['html', 'json', 'xml']

    SiteController() {
        super(Site)
    }
}
