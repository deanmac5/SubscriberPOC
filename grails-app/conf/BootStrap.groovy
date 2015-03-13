import subscriberpoc.Agency

class BootStrap {

    def init = { servletContext ->
        environments {
            development {
                print "INSIDE DEVELOPMENT CHECKING IF WE NEED TO CREATE SAMPLE DATA"
                if(!Agency.count()) createSampleData()
            }
        }
    }
    def destroy = {
    }

    private createSampleData(){
        print "Inputting sample data"
        new Agency(title: "Dept of Pork Rolls", portfolio: "Food").save()
        print "Inputed sample data"
    }
}
