package pl.training.bank.api;

import pl.training.bank.account.AccountDto;

import javax.ejb.Remote;

@Remote
public interface Bank {

    AccountDto createAccount();

    long getBalance(String accountNumber);

}
