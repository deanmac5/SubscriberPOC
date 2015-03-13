package subscriberpoc

class Subscriber {

    String name
    String email

    static hasMany = [subscriptions: Subscription]

    static constraints = {
        name blank: false, maxSize: 100
        email email: true
    }
}
