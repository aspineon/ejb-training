package pl.training.bank.service.account;

import pl.training.bank.entity.account.Account;

import javax.ejb.Local;

@Local
public interface AccountRepository {

    Account getByNumber(String number);

    Account getById(Long id);

    void update(Account account);

    Account save(Account account);

}
