package pl.training.bank.service.operation;

import lombok.extern.java.Log;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@Log
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "BankDS")
})
public class LargeDepositService implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println(message.getBody(String.class));
        } catch (JMSException e) {
            log.severe(e.getMessage());
        }
    }

}
