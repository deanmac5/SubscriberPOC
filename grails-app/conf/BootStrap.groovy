import subscriberpoc.Agency
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

        new Agency(title: "Dept of Industry", portfolio: "Industry", urls: ["http://www.minister.industry.gov.au/ministers/media_releases"]).save()
        new Agency(title: "Dept of Finance", portfolio: "Finance", urls: ["http://www.financeminister.gov.au/media/2015/index.html"]).save()
        new Subscriber(name: "Pablo", email: "pablo@test.com").save()
        new Subscription(subscriber: Subscriber.findByName("Pablo"), agencies: Agency.findByTitle("Dept of Industry")).save()
    }
}
