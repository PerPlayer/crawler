package com.crawler.util;

import com.alibaba.fastjson.JSON;
import com.crawler.exception.ParameterException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by c_sunhaiwei on 2017/10/26.
 */
public class BeanValidUtil {

    /**
     * 校验Bean属性
     * @param obj
     * @return
     */
    public static Map<String, Object> valid(Object obj, Class... clazzes) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> validate = null;
        validate = validator.validate(obj, clazzes);
        return getStringObjectMap(validate);
    }

    private static Map<String, Object> getStringObjectMap(Set<ConstraintViolation<Object>> validate) {
        Map<String, Object> map = new HashMap<>();
        for (ConstraintViolation<Object> cp : validate) {
            map.put(cp.getPropertyPath().toString(), cp.getMessage());
        }
        return map;
    }

    /**
     * 从BindingResult中提取结果，并抛出异常
     * @param result
     */
    public static void valid(BindingResult result) {
        if (result.getFieldErrorCount() > 0) {
            Map<String, Object> map = new HashMap<>();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            throw new ParameterException(JSON.toJSONString(map));
        }
    }

    /**
     * 从Errors中提取结果，并抛出异常
     * @param errors
     */
    public static void valid(Errors errors) {
        if (errors.hasErrors()) {
            Map<String, Object> map = new HashMap<>();
            List<FieldError> fieldErrors = errors.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            throw new ParameterException(JSON.toJSONString(map));
        }
    }




}
