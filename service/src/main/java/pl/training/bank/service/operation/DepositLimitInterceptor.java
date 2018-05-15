package pl.training.bank.service.operation;

import javax.annotation.Resource;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Topic;

public class DepositLimitInterceptor {

    private static final int FUNDS_INDEX = 0;
    private static final int ACCOUNT_NUMBER_INDEX = 1;
    private static final int DEPOSIT_LIMIT = 1_000;

    @Resource(lookup = "java:jboss/exported/jms/RemoteConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(lookup = "java:jboss/exported/jms/topic/Bank")
    private Topic topic;

    @AroundInvoke
    public Object addEntry(InvocationContext invocationContext) throws Exception {
        Object result = invocationContext.proceed();
        Object[] parameters = invocationContext.getParameters();
        long funds = (Long) parameters[FUNDS_INDEX];
        String accountNumber = (String) parameters[ACCOUNT_NUMBER_INDEX];
        checkDepositLimit(funds, accountNumber);
        return result;
    }

    private void checkDepositLimit(long funds, String accountNumber) {
        if (funds > DEPOSIT_LIMIT) {
            String message = "Deposit limit on account: " + accountNumber;
            try (JMSContext jmsContext = connectionFactory.createContext()) {
                jmsContext.createProducer().send(topic, message);
            }
        }
    }

}
