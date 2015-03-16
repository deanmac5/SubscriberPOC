package subscriberpoc

class Subscription {

    Date created = new Date()

    static belongsTo = [subscriber: Subscriber]
    static hasMany = [agencies: Agency]

    static constraints = {
        subscriber blank: false
        agencies blank: false
    }
}
