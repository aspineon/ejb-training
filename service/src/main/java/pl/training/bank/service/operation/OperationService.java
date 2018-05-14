package pl.training.bank.service.operation;

import lombok.Setter;
import lombok.extern.java.Log;
import pl.training.bank.operation.OperationDto;
import pl.training.bank.service.account.AccountService;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@Log
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "BankDS")
})
public class OperationService implements MessageListener {

    @Setter
    @EJB
    private AccountService accountService;

    @Override
    public void onMessage(Message message) {
        try {
            OperationDto operationDto = message.getBody(OperationDto.class);
            log.info("Processing new operation ...");
            switch (operationDto.getType()) {
                case DEPOSIT:
                    accountService.deposit(operationDto.getFunds(), operationDto.getPrimaryAccount().getNumber());
                    break;
                case WITHDRAW:
                    accountService.withdraw(operationDto.getFunds(), operationDto.getPrimaryAccount().getNumber());
                    break;
                case TRANSFER:
                    accountService.withdraw(operationDto.getFunds(), operationDto.getPrimaryAccount().getNumber());
                    accountService.deposit(operationDto.getFunds(), operationDto.getSecondaryAccount().getNumber());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operation");
            }
        } catch (JMSException e) {
            log.severe(e.getMessage());
        }
    }

}
