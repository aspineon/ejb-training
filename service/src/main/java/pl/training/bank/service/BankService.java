package pl.training.bank.service;

import lombok.Setter;
import org.modelmapper.ModelMapper;
import pl.training.bank.entity.account.Account;
import pl.training.bank.operation.OperationDto;
import pl.training.bank.service.operation.OperationExecutorService;
import pl.training.bank.service.account.AccountService;
import pl.training.bank.account.AccountDto;
import pl.training.bank.api.Bank;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Collections;
import java.util.List;

@Stateless
public class BankService implements Bank {

    @Setter
    @EJB
    private AccountService accountService;
    @Setter
    @EJB
    private OperationExecutorService operationExecutorService;
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public AccountDto createAccount() {
        Account account = accountService.createAccount();
        return modelMapper.map(account, AccountDto.class);
    }

    @Override
    public long getBalance(String accountNumber) {
        return accountService.getBalance(accountNumber);
    }

    @Override
    public void processOperation(OperationDto operationDto) {
        operationExecutorService.submit(Collections.singletonList(operationDto));
    }

    @Override
    public void processOperations(List<OperationDto> operationDtos) {
        operationExecutorService.submit(operationDtos);
    }

}
