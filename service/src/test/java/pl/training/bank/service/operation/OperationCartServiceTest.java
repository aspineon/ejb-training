package pl.training.bank.service.operation;

import org.junit.Before;
import org.junit.Test;
import pl.training.bank.operation.OperationDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OperationCartServiceTest {

    private OperationExecutorService operationExecutorService = mock(OperationExecutorService.class);
    private OperationCartService operationCartService = new OperationCartService();
    private List<OperationDto> operationDtos = new ArrayList<>();
    private OperationDto operationDto = new OperationDto();

    @Before
    public void setup() {
        operationCartService.setOperationExecutorService(operationExecutorService);
        operationCartService.setOperationDtos(operationDtos);
        operationCartService.add(operationDto);
    }

    @Test
    public void shouldAddOperations() {
        assertTrue(operationDtos.contains(operationDto));
    }

    @Test
    public void shouldSubmitAllAddedOperations() {
        operationCartService.submit();
        verify(operationExecutorService).submit(operationDtos);
    }

}
