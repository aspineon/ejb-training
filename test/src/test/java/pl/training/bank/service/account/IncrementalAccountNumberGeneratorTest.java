package pl.training.bank.service.account;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class IncrementalAccountNumberGeneratorTest {

    private static final String ACCOUNT_NUMBER_FORMAT = "\\d{26}";

    @EJB
    private AccountNumberGenerator accountNumberGenerator;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(AccountNumberGenerator.class)
                .addClass(IncrementalAccountNumberGenerator.class);
    }

    @Test
    public void shouldGenerateValidAccountNumber() {
        String accountNumber = accountNumberGenerator.getNext();
        assertTrue(accountNumber.matches(ACCOUNT_NUMBER_FORMAT));
    }

}
