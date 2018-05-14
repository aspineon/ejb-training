package pl.training.bank.service.account;

import lombok.Setter;
import pl.training.bank.entity.account.Account;
import pl.training.bank.account.InsufficientFundsException;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Topic;

@Setter
@Stateless
public class AccountService {

    @EJB
    private AccountNumberGenerator accountNumberGenerator;
    @EJB
    private AccountRepository accountRepository;
    @Resource(lookup = "java:jboss/exported/jms/RemoteConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(lookup = "java:jboss/exported/jms/topic/Bank")
    private Topic topic;

    public Account createAccount() {
        String accountNumber = accountNumberGenerator.getNext();
        Account account = new Account(accountNumber);
        accountRepository.save(account);
        return account;
    }

    public void deposit(long funds, String accountNumber) {
        if (funds > 1000) {
            String message = "Deposit limit on account: " + accountNumber;
            try (JMSContext jmsContext = connectionFactory.createContext()) {
                jmsContext.createProducer().send(topic, message);
            }
        }
        Account account = accountRepository.getByNumber(accountNumber);
        account.deposit(funds);
    }

    public void withdraw(long funds, String accountNumber) {
        Account account = accountRepository.getByNumber(accountNumber);
        if (account.getBalance() < funds) {
            throw new InsufficientFundsException();
        }
        account.withdraw(funds);
    }

    public Account getAccountById(Long id) {
        return accountRepository.getById(id);
    }

    public long getBalance(String accountNumber) {
        return accountRepository.getByNumber(accountNumber).getBalance();
    }

}
