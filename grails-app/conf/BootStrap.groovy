import subscriberpoc.Agency
import subscriberpoc.MediaList
import subscriberpoc.Subscriber
import subscriberpoc.Subscription

class BootStrap {

    def init = { servletContext ->
        environments {
            development {

                if(!Agency.count() && (!Subscriber.count())) createSampleData()
            }
        }
    }
    def destroy = {
    }

    private createSampleData(){
        println "Creating Sample Data"
        Agency agency1 = new Agency(title: "Dept of Industry", portfolio: "Industry").save()
        Agency agency2 = new Agency(title: "Dept of Finance", portfolio: "Finance").save()
        agency1.addToMediaLists(new MediaList(created: "dcterms.date", description: "dcterms.description", url: "http://www.minister.industry.gov.au/ministers/media_releases")).save()
        //agency2.addToMediaLists(new MediaList(created: "DC.Date.created", description: "DC.Description", url: "http://www.financeminister.gov.au/media/2015/index.html")).save()
        //agency2.addToMediaLists(new MediaList(created: "DCTERMS.date", description: "description", url: "http://www.finance.gov.au/about-the-department/media-centre/secretary/")).save()
        new Subscriber(name: "Pablo", email: "pablo@test.com").save()
        new Subscription(subscriber: Subscriber.findByName("Pablo"), agencies: Agency.findByTitle("Dept of Industry")).save()
    }
}
