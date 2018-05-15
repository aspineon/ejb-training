package pl.training.bank.service.account;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.training.bank.account.AccountNotFoundException;
import pl.training.bank.api.BankException;
import pl.training.bank.entity.account.Account;

import javax.ejb.EJB;

import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class JpaAccountRepositoryServiceTest {

    private static final String ACCOUNT_NUMBER = "0001";

    @EJB
    private AccountRepository accountRepository;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(Account.class)
                .addClasses(BankException.class, AccountNotFoundException.class)
                .addClasses(AccountRepository.class, JpaAccountRepositoryService.class)
                .addAsResource("META-INF/persistence.xml");
    }

    @Test(expected = AccountNotFoundException.class)
    public void shouldThrowExceptionWhenAccountDoesNotExist() {
        accountRepository.getByNumber(ACCOUNT_NUMBER);
    }

    @Cleanup(strategy = CleanupStrategy.USED_ROWS_ONLY, phase = TestExecutionPhase.AFTER)
    @Test
    public void shouldGetAccountByNumber() {
        accountRepository.save(new Account(ACCOUNT_NUMBER));
        assertNotNull(accountRepository.getByNumber(ACCOUNT_NUMBER));
    }

}
