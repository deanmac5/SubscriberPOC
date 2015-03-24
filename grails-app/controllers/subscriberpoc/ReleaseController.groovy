package subscriberpoc

import grails.converters.JSON
import grails.rest.RestfulController
import org.apache.commons.logging.LogFactory

class ReleaseController extends RestfulController {

    private static final log = LogFactory.getLog(this)

    static scaffold = true
    static responseFormats = ['html', 'json', 'xml']
    ReleaseController() {
        super(Release)
    }
}
