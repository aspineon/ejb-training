package pl.training.bank.account;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountDto implements Serializable {

    private String number;
    private long balance;

}
