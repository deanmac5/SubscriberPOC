package subscriberpoc

class Subscription {

    Date created = new Date()

    static belongsTo = [subscriber: Subscriber]

    static constraints = {
    }
}