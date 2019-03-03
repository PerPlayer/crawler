package com.crawler.aspect.beanvalidaspect;

import com.alibaba.fastjson.JSON;
import com.crawler.annonation.GroupValid;
import com.crawler.exception.ParameterException;
import com.crawler.util.BeanValidUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * Created by SHW on 2019/3/3.
 */
@Aspect
@Component
public class BeanValidAspect {

    @Pointcut("execution(* com.crawler.service.impl.*.*(..))")
    public void pointcut(){}

    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        Parameter[] parameters = targetMethod.getParameters();// JDK1.8以后支持
        for(int i=0; i<parameters.length; i++) {
            Parameter parameter = parameters[i];
            GroupValid valid = parameter.getAnnotation(GroupValid.class);
            if (valid != null) {
                Object obj = joinPoint.getArgs()[i];
                Map<String, Object> errors = null;
                if (valid.groups().length > 0) {
                    errors = BeanValidUtil.valid(obj, valid.groups());
                } else {
                    errors = BeanValidUtil.valid(obj, valid.value());
                }
                if (errors.size() > 0) {
                    throw new ParameterException(JSON.toJSONString(errors));
                }
            }
        }
    }

    /**
     * Aop方法，校验update方法参数的Bean属性
     * @param joinPoint
     * @throws Throwable
     */
    public void before1(JoinPoint joinPoint) throws Throwable {
        /*
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Object[] args = joinPoint.getArgs();
            for (Object obj : args) {
            Method method = methodSignature.getMethod();
            Annotation[] annotations = method.getParameterAnnotations()[0];
            boolean bool = false;
            for (Annotation annotation : annotations) {
                bool = annotation instanceof Valid;
                break;
            }
            if (bool) {
                Map<String, Object> errors = BeanValidUtil.valid(obj);
                if (errors.size() > 0) {
                    throw new ParameterException(JSON.toJSONString(errors));
                }
            }
        }
        */
    }
}
