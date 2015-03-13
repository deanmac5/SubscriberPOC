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
    }
}
