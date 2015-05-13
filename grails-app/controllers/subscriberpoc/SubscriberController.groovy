package subscriberpoc
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SubscriberController {

    def mailService

    def index() {
        [subscriberInstance: new Subscriber(params)]
    }

    def signup(){
        def subscriberInstance = new Subscriber(params)
        subscriberInstance.verified = false;
        subscriberInstance.confirmCode = UUID.randomUUID().toString()
        if(!subscriberInstance.save(flush: true)){
            return
        }

        mailService.sendMail {
            to subscriberInstance.email
            subject "New Subscription Confirmation"
            html g.render(template: "mailtemplate", model: [code:subscriberInstance.confirmCode])
        }

        render(view: "success", model: [subscriberInstance: subscriberInstance])
        redirect(action: "success")
    }

    def success(){

        flash.message = 'Your subscription is almost created. Please complete the process ' +
                'using the email we have now sent to your email address'
        render(view: 'index' )
    }

    def confirm(String id){
        Subscriber subscriberInstance = Subscriber.findByConfirmCode(id)
        if(!subscriberInstance){
            return
        }

        subscriberInstance.verified = true
        if(!subscriberInstance.save(flush: true)){
            render(view: "success", model: [message: 'Problem activating account'])
            return
        }
        render(view: "success", model: [message: 'Your subscription has been successfully activated'])
    }
}
