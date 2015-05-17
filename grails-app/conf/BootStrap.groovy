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
//        def userRole = new Role(authority: 'ROLE_USER').save(flush: true)
        Agency agri = new Agency(title: "Dept of Agriculture").save()
        Agency afma = new Agency(title: "Australian Fisheries Management Authority").save()
        Agency ags = new Agency(title: "Attorney Generals").save()

        agri.addToSites(new Site(created: "dcterms.date", description: "dcterms.description", url: "http://www.agricultureparlsec.gov.au/Pages/default.aspx", createdRegex: "yyyy-MM-dd'T'HH:mm", mediaReleaseSelector: "h1:contains(Media Release)")).save()
        afma.addToSites(new Site(created: "DC.Date.created", description: "DC.Description", url: "http://http://www.afma.gov.au/news-media/", createdRegex: "yyyy-MM-dd", mediaReleaseSelector: "data-value:contains(media-releases)")).save()
//        agency2.addToSites(new Site(created: "DCTERMS.date", description: "description", url: "http://www.finance.gov.au/about-the-department/media-centre/secretary/", createdRegex: "yyyy-MM-dd'T'HH:mm", mediaReleaseSelector: "h1:contains(Media Release)")).save()
        ags.addToSites(new Site(created: "dcterms.date", description: "dcterms.description", url: "http://www.attorneygeneral.gov.au/Pages/Newsroom.aspx", createdRegex: "yyyy-MM-dd'T'HH:mm", mediaReleaseSelector: "h1:contains(Media Release)")).save()

        def first = new User(username: "Pablo", password: 'password', email: "pablo@test.com").save()
        def second = new User(username: "Deano", password: 'password', email: "deano@test.com").save()
        UserRole.create first, adminRole, true
        UserRole.create second, adminRole, true

        Subscriber third = new Subscriber(email: "mediareleasetester@gmail.com", verified: true, confirmCode: "a90ab7d7-31ef-41b0-b814-bee216bc1436", subscriptions: [Topic.findByName("Agriculture")]).save(flush: true)
        Subscriber fourth = new Subscriber(email: "media.releasetester@gmail.com", verified: false, confirmCode: "blah", subscriptions: [Topic.findByName("Business and Industry")]).save(flush: true)

        Topic agriculture = new Topic(name: 'Agriculture', description: "stuff", agencies:[agri, afma] ).save()
        Topic business = new Topic(name: "Business and Industry", agencies: ags).save()



    }


}
