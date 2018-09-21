package com.xmall.controller.aspectj;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @author xies
 * @date 2018/3/13.
 */
@Aspect
@Component
@Slf4j
public class ControllerAspectj {

    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<>("ThreadLocal BeginTime");

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    private void aspectJMethod(){
        log.info("==================controllerAspectj===============");
    }

    @Before("aspectJMethod()")
    public void doBefore(JoinPoint joinPoint){
        Date beginTime = new Date();
        beginTimeThreadLocal.set(beginTime);
    }
//
//    @After("aspectJMethod()")
//    public void after(JoinPoint joinPoint){
//        try{
//            String username =
//        }catch (Exception e){
//
//        }
//    }

}
