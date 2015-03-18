package subscriberpoc

import grails.rest.RestfulController

class MediaListController extends RestfulController {

    static scaffold = true
    static responseFormats = ['html', 'json', 'xml']
    MediaListController() {
        super(MediaList)
    }
}
