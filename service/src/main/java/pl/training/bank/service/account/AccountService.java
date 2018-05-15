package pl.training.bank.service.account;

import lombok.Setter;
import pl.training.bank.account.InsufficientFundsException;
import pl.training.bank.entity.account.Account;
import pl.training.bank.service.Timer;
import pl.training.bank.service.operation.DepositLimitInterceptor;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jms.ConnectionFactory;
import javax.jms.Topic;

@Setter
@Stateless
public class AccountService {

    @EJB
    private AccountNumberGenerator accountNumberGenerator;
    @EJB(beanName = "JpaAccountRepositoryService")
    private AccountRepository accountRepository;

    public Account createAccount() {
        String accountNumber = accountNumberGenerator.getNext();
        Account account = new Account(accountNumber);
        accountRepository.save(account);
        return account;
    }

    @Interceptors({DepositLimitInterceptor.class, Timer.class})
    public void deposit(long funds, String accountNumber) {
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
