import subscriberpoc.*

class BootStrap {

    def init = { servletContext ->
        environments {
            development {

                if(!Agency.count() && (!User.count())) createSampleData()

            }
        }
    }
    def destroy = {
    }

    private createSampleData(){
        println "Creating Sample Data"
        def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
        def userRole = new Role(authority: 'ROLE_USER').save(flush: true)
        Agency agency1 = new Agency(title: "Dept of Industry", portfolio: "Industry").save()
        Agency agency2 = new Agency(title: "Dept of Finance", portfolio: "Finance").save()
        agency1.addToSites(new Site(created: "dcterms.date", description: "dcterms.description", url: "http://www.minister.industry.gov.au/ministers/media_releases", createdRegex: "yyyy-MM-dd'T'HH:mm", mediaReleaseSelector: "body[class*=node-type-media-releases]")).save()
        agency2.addToSites(new Site(created: "DC.Date.created", description: "DC.Description", url: "http://www.financeminister.gov.au/media/2015/index.html", createdRegex: "yyyy-MM-dd", mediaReleaseSelector: "h1:contains(Media Release)")).save()
        agency2.addToSites(new Site(created: "DCTERMS.date", description: "description", url: "http://www.finance.gov.au/about-the-department/media-centre/secretary/", createdRegex: "yyyy-MM-dd'T'HH:mm", mediaReleaseSelector: "h1:contains(Media Release)")).save()
        def first = new User(username: "Pablo", password: 'password', email: "pablo@test.com").save()
        def second = new User(username: "Deano", password: 'password', email: "deano@test.com").save()
        UserRole.create first, adminRole, true
        UserRole.create second, userRole, true


        def third = new Subscriber(email: "mediareleasetester@gmail.com", subscriptions: [Agency.findByTitle("Dept of Finance")]).save()
        def fourth = new Subscriber(email: "mediareleasetester@gmail.com", subscriptions: [Agency.findByTitle("Dept of Industry")]).save()
    }


}
