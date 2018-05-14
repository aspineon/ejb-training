package pl.training.bank.operation;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import java.util.List;

@Stateless
public class OperationExecutorService {

    @Resource(lookup = "java:jboss/exported/jms/RemoteConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(lookup = "java:jboss/exported/jms/queue/Bank")
    private Queue queue;

    public void submit(List<OperationDto> operationDtos) {
        try (JMSContext jmsContext = connectionFactory.createContext()) {
            JMSProducer jmsProducer = jmsContext.createProducer();
            operationDtos.forEach(operationDto -> jmsProducer.send(queue, operationDto));
        }
    }

}
