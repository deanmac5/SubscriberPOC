package subscriberpoc

class Subscriber {

    String email
    boolean verified

    static hasMany = [subscriptions: Agency]

    static constraints = {
        email email:true, unique: true
    }
}
