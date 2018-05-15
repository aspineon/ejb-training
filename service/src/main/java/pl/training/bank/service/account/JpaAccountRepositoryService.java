package pl.training.bank.service.account;

import lombok.Setter;
import pl.training.bank.account.AccountNotFoundException;
import pl.training.bank.entity.account.Account;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pl.training.bank.entity.account.Account.GET_BY_NUMBER_QL;

@Setter
@Stateless
public class JpaAccountRepositoryService implements AccountRepository {

    @PersistenceContext(unitName = "bank")
    private EntityManager entityManager;

    @Override
    public Account getByNumber(String number) {
        try {
            return entityManager.createNamedQuery(GET_BY_NUMBER_QL, Account.class)
                    .setParameter("number", number)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new AccountNotFoundException();
        }
    }

    @Override
    public Account getById(Long id) {
        Account account = entityManager.find(Account.class, id);
        if (account == null) {
            throw new AccountNotFoundException();
        }
        return account;
    }

    @Override
    public void update(Account account) {
        getById(account.getId());
        entityManager.merge(account);
    }

    @Override
    public Account save(Account account) {
        entityManager.persist(account);
        entityManager.flush();
        entityManager.refresh(account);
        return account;
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "JpaAccountRepositoryService is ready...");
    }

    @PreDestroy
    public void destroy() {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "JpaAccountRepositoryService is going down...");
    }

}
