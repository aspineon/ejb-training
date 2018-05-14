package pl.training.bank.client;

import pl.training.bank.account.AccountDto;
import pl.training.bank.api.Bank;
import pl.training.bank.api.OperationCart;
import pl.training.bank.api.OperationReporting;
import pl.training.bank.operation.OperationDto;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.NamingException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static pl.training.bank.operation.OperationTypeDto.DEPOSIT;
import static pl.training.bank.operation.OperationTypeDto.WITHDRAW;

public class EjbClient {

    private static final String BANK_JNDI_NAME = "java:/bank/BankService!pl.training.bank.api.Bank";
    private static final String OPERATION_CART_JNDI_NAME = "java:/bank/OperationCartService!pl.training.bank.api.OperationCart";
    private static final String OPERATION_REPORTING_CART_JNDI_NAME = "java:/bank/OperationReportingService!pl.training.bank.api.OperationReporting";
    private static final String CONNECTION_FACTORY_JNDI = "jms/RemoteConnectionFactory";
    private static final String BANK_QUEUE_JNDI = "jms/queue/Bank";

    public static void main(String[] args) throws NamingException, InterruptedException, ExecutionException {
        ProxyFactory proxyFactory = new ProxyFactory();
        Bank bank = proxyFactory.createProxy(BANK_JNDI_NAME);

        AccountDto account = bank.createAccount();

        bank.processOperation(new OperationDto(account, DEPOSIT, 1_001L));

        ConnectionFactory connectionFactory = proxyFactory.createProxy(CONNECTION_FACTORY_JNDI);
        Queue queue = proxyFactory.createProxy(BANK_QUEUE_JNDI);
        try (JMSContext jmsContext = connectionFactory.createContext()) {
            jmsContext.createProducer().send(queue, new OperationDto(account, WITHDRAW, 500L));
        }

        Thread.sleep(3000);
        System.out.println(bank.getBalance(account.getNumber()));

        OperationCart operationCart = proxyFactory.createProxy(OPERATION_CART_JNDI_NAME);
        operationCart.add(new OperationDto(account, WITHDRAW, 200L));
        operationCart.add(new OperationDto(account, WITHDRAW, 100L));
        operationCart.submit();

        Thread.sleep(3000);
        System.out.println(bank.getBalance(account.getNumber()));

        OperationReporting operationReporting = proxyFactory.createProxy(OPERATION_REPORTING_CART_JNDI_NAME);
        Future<List<OperationDto>> result = operationReporting.generateOperationsReport();
        System.out.println("Is done: " + result.isDone());
        System.out.println("Result : " + result.get());
    }

}
