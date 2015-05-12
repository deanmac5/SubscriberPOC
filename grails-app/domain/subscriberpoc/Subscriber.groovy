package subscriberpoc

class Subscriber {

    String email
    boolean verified

    static hasMany = [subscriptions: Agency]

    static constraints = {
        email email:true // unique: false  //TODO put this in once prototyping is complete
    }
}
