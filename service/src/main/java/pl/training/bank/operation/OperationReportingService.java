package pl.training.bank.operation;

import pl.training.bank.api.OperationReporting;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Stateless
public class OperationReportingService implements OperationReporting {

    @Asynchronous
    @Override
    public Future<List<OperationDto>> generateOperationsReport() {
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(new ArrayList<>());
    }

}
