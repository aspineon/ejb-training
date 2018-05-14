package pl.training.bank.service.operation;

import lombok.Setter;
import lombok.extern.java.Log;
import pl.training.bank.api.OperationCart;
import pl.training.bank.operation.OperationDto;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.*;
import java.util.ArrayList;
import java.util.List;

@Log
@Setter
@Stateful
public class OperationCartService implements OperationCart  {

    @EJB
    private OperationExecutorService operationExecutorService;
    private List<OperationDto> operationDtos = new ArrayList<>();

    @Override
    public void add(OperationDto operationDto) {
        operationDtos.add(operationDto);
    }

    @Remove
    @Override
    public void submit() {
        operationExecutorService.submit(operationDtos);
    }

    @Remove
    @Override
    public void cancel() {
    }

    @PostConstruct
    public void init() {
        log.info(getClass().getSimpleName() + " is ready...");
    }

    @PreDestroy
    public void destroy() {
        log.info(getClass().getSimpleName() + " is going down...");
    }

    @PrePassivate
    public void prePassivate() {
        log.info(getClass().getSimpleName() + " will be passivated...");
    }

    @PostActivate
    public void postActivate() {
        log.info(getClass().getSimpleName() + " will be activated...");
    }

}
