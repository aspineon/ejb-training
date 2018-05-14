package pl.training.bank.api;

import pl.training.bank.account.AccountDto;
import pl.training.bank.operation.OperationDto;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface Bank {

    AccountDto createAccount();

    long getBalance(String accountNumber);

    void processOperation(OperationDto operationDto);

    void processOperations(List<OperationDto> operationDtos);

}
