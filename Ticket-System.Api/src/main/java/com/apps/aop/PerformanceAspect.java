package com.apps.aop;

import com.apps.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Aspect
@Slf4j
public class PerformanceAspect {
//    @Value("${application.aop.warnningExecutionTime:1000}")
//    private long warnningExecutionTime;
//
//    @Around("execution(* *(..)) &&" +
//            "(" +
//            "    within(com.apps.*) " +
//            ")")
//    public Object performce(ProceedingJoinPoint joinPoint) throws Throwable {
//        long start = System.currentTimeMillis();
//
////        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////        final String username = auth != null ? auth.getName() : "";
//
//        try {
//            return joinPoint.proceed();
//        } finally {
//            long executionTime = System.currentTimeMillis() - start;
//            if (executionTime > warnningExecutionTime) {
//                String level = "ok";
//                if(executionTime >= ( 1000*60)) {
//                    level = "highest";
//                } else if (executionTime >= (1000*30)) {
//                    level = "high";
//                } else if (executionTime >= (1000*15)) {
//                    level = "medium";
//                }
//
//                log.info(" {} executed in {} ({})", joinPoint.getSignature(), CommonUtils.toHumanReadableDuration(executionTime), level);
//            }
//        }
//
//    }
//
//    @Before("execution(* com.apps.controllers.*.*(..))")
//    public void beforeControllers(JoinPoint joinPoint) {
//        List<String> args = new ArrayList<>();
//
//        if (joinPoint.getArgs() != null) {
//            args = Arrays.stream(joinPoint.getArgs()).map(a -> a!=null ? a.toString() : null).collect(Collectors.toList());
//        }
//
////        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////        final String username = auth.getName();
//        log.debug(" execute ({}) {}",
////                username,
//                joinPoint.getSignature(),
//                CommonUtils.isNullOrEmpty(args) ? "" : "message: " + String.join(", ", args));
//
//    }
}
