package subscriberpoc

class Subscriber {

    String email
    String confirmCode
    boolean verified

    static hasMany = [subscriptions: Agency]

    static constraints = {
        email email:true, unique: true
    }
}
