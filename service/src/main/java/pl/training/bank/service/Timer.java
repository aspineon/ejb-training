package pl.training.bank.service;

import lombok.extern.java.Log;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

@Log
public class Timer {

    @AroundInvoke
    public Object measure(InvocationContext invocationContext) throws Exception {
        long startTime = System.nanoTime();
        Object result = invocationContext.proceed();
        log.info("Invocation time: " + (System.nanoTime() - startTime) + " for method: " + invocationContext.getMethod().getName());
        return result;
    }

}
