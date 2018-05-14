package pl.training.bank.operation;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.training.bank.account.AccountDto;

import java.io.Serializable;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class OperationDto implements Serializable {

    @NonNull
    private AccountDto primaryAccount;
    private AccountDto secondaryAccount;
    @NonNull
    private OperationTypeDto type;
    @NonNull
    private Long funds;

}
