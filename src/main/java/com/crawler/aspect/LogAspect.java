package com.crawler.aspect;

import com.alibaba.fastjson.JSON;
import com.crawler.annonation.Logger;
import com.crawler.entity.LogEntity;
import com.crawler.service.LogService;
import com.crawler.util.EntityUtil;
import com.crawler.util.HttpContextUtils;
import com.crawler.util.IPUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Consumer;
import java.util.function.Function;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogService logService;

    @Pointcut("@annotation(com.crawler.annonation.Logger)")
    private void pointcut(){}

    @Around("pointcut()")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object obj = joinPoint.proceed();

        long end = System.currentTimeMillis();
        LogEntity logEntity = new LogEntity();
        logEntity.setTime(end - start);
        EntityUtil.init(logEntity);
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        logEntity.setMethod(signature.getName());
        Logger annotation = signature.getMethod().getAnnotation(Logger.class);
        logEntity.setOperation(annotation.value());

        Object[] args = joinPoint.getArgs();

        logEntity.setParams(JSON.toJSONString(
                ArrayUtils.subarray(args, 0, args.length - 1)));
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        logEntity.setIp(IPUtils.getIpAddr(request));

        logService.save(logEntity);
        return obj;
    }


}
