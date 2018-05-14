package pl.training.bank.client;

import pl.training.bank.account.AccountDto;
import pl.training.bank.api.Bank;

import javax.naming.NamingException;

public class EjbClient {

    private static final String BANK_JNDI_NAME = "java:/bank/BankService!pl.training.bank.api.Bank";

    public static void main(String[] args) throws NamingException {
        ProxyFactory proxyFactory = new ProxyFactory();
        Bank bank = proxyFactory.createProxy(BANK_JNDI_NAME);

        AccountDto account = bank.createAccount();
        System.out.println(bank.getBalance(account.getNumber()));
    }

}
