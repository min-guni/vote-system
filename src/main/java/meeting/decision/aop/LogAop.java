package meeting.decision.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
//@Component //aop 대신 interceptor 사용
@Slf4j
public class LogAop {

    private static final long WARNING_THRESHOLD = 1000;

    @Around("execution(* meeting.decision.controller..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        String uuid = UUID.randomUUID().toString().substring(0,8);

        long startTime = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        if(executionTime > WARNING_THRESHOLD){
            log.warn("[{}] [{}] executed in {} ms", uuid , joinPoint.getSignature(), executionTime);
        }
        else{
            log.info("[{}] [{}] executed in {} ms", uuid , joinPoint.getSignature(), executionTime);
        }

        return proceed;
    }
}