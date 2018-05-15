package pl.training.bank.service.operation;

import pl.training.bank.entity.account.Account;
import pl.training.bank.entity.operation.Operation;
import pl.training.bank.entity.operation.OperationType;
import pl.training.bank.service.account.AccountRepository;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.Date;

public class OperationHistoryInterceptor {

    private static final int FUNDS_INDEX = 0;
    private static final int ACCOUNT_NUMBER_INDEX = 1;

    @EJB(beanName = "JpaAccountRepositoryService")
    private AccountRepository accountRepository;
    @EJB
    private JpaOperationRepositoryService operationRepositoryService;

    @AroundInvoke
    public Object addEntry(InvocationContext invocationContext) throws Exception {
        Object result = invocationContext.proceed();
        Operation operation = mapToOperation(invocationContext);
        operationRepositoryService.save(operation);
        return result;
    }

    private Operation mapToOperation(InvocationContext invocationContext) {
        String methodName = invocationContext.getMethod().getName();
        Object[] parameters = invocationContext.getParameters();
        Account account = mapToAccount(parameters);
        OperationType operationType = mapToOperationType(methodName);
        long funds = mapToFunds(parameters);
        return createOperation(account, operationType, funds);
    }

    private Account mapToAccount(Object[] parameters) {
        String accountNumber = (String) parameters[ACCOUNT_NUMBER_INDEX];
        return accountRepository.getByNumber(accountNumber);
    }

    private OperationType mapToOperationType(String methodName) {
        return OperationType.valueOf(methodName.toUpperCase());
    }

    private long mapToFunds(Object[] parameters) {
        return (Long) parameters[FUNDS_INDEX];
    }

    private Operation createOperation(Account account, OperationType operationType, long funds) {
        Operation operation = new Operation(account, operationType, funds);
        operation.setDate(new Date());
        return operation;
    }

}
