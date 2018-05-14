package pl.training.bank.service.account;

import lombok.extern.java.Log;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.*;

//@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Log
@Startup
@Singleton
public class IncrementalAccountNumberGenerator implements AccountNumberGenerator {

    private static final String ACCOUNT_NUMBER_FORMAT = "%026d";

    private long counter;

    //@Lock(LockType.WRITE)
    @Override
    public String getNext() {
        return format(++counter);
    }

    @Lock(LockType.READ)
    @Override
    public String getLast() {
        return format(counter);
    }

    private String format(long accountNumber) {
        return String.format(ACCOUNT_NUMBER_FORMAT, accountNumber);
    }

    @PostConstruct
    public void init() {
        log.info(getClass().getSimpleName() + " is ready...");
    }

    @PreDestroy
    public void destroy() {
        log.info(getClass().getSimpleName() + " is going down...");
    }

}
