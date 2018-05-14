package pl.training.bank.api;

import pl.training.bank.operation.OperationDto;

import javax.ejb.Remote;

@Remote
public interface OperationCart {

    void add(OperationDto operationDto);

    void submit();

    void cancel();

}
