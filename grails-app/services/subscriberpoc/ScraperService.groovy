package subscriberpoc

import grails.transaction.Transactional

@Transactional
class ScraperService {

    def getAgencyReleases() {

        def allAgencies = Agency.list()

        print allAgencies

    }
}
