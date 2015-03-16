package subscriberpoc

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Agency)
class AgencySpec extends Specification {

    def "Retrieving urls from Agency"(){
        given: "A new agency"
        def agency = new Agency(title: "Dept of Testing", portfolio: "The portfolio", urls: ["http://somethingOne", "http://somethingElse"])

        when: "the agency is saved"
        agency.save()

        then: "it has been saved and the url's can be retrieved"
        agency.errors.errorCount == 0
        agency.id != null
        agency.urls[1] == "http://somethingElse"
        agency.urls[0] != "anything"
    }
}
