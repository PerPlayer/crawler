package com.crawler.extend;

import com.crawler.exception.BusinessException;
import com.crawler.exception.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by c_sunhaiwei on 2017/10/25.
 */
public class HandlerExceptionResolverExtend implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(HandlerExceptionResolverExtend.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.info("handle exception: {}", ex.getMessage());
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("msg", ex.getMessage());

        //如果为Ajax请求，直接返回异常信息
        if (ex instanceof ParameterException || ex instanceof IllegalArgumentException || isAjax(request)) {
            try {
                //设置编码，不然会乱码
                response.setContentType("text/txt; charset=utf-8");
                response.getWriter().print(model);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 根据不同错误转向不同页面
        if(ex instanceof BusinessException) {
            return new ModelAndView("exmessage", model);
        }else if(ex instanceof Exception){
            return new ModelAndView("exception", model);
        }else {
            return new ModelAndView("error", model);
        }
    }

    // 判断是否为Ajax请求
    public static boolean isAjax(HttpServletRequest request){
        String ajaxHeader = request.getHeader("X-Requested-With");
        return !StringUtils.isEmpty(ajaxHeader);
    }
}
