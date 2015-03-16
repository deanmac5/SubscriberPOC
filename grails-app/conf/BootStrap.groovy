import subscriberpoc.Agency

class BootStrap {

    def init = { servletContext ->
        environments {
            development {

                if(!Agency.count()) createSampleData()
            }
        }
    }
    def destroy = {
    }

    private createSampleData(){

        new Agency(title: "Dept of Pork Rolls", portfolio: "Food").save()
        new Agency(title: "Dept of Finance", portfolio: "Finance", urls: ["http://finance.gov.au/about-the-department/media-centre","http://financeminister.gov.au/media/2015/index.html"]).save()

    }
}
