package com.example.shape.config;

import com.example.shape.Util.Basepublic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Hxxxxxx
 * @Date: 2023/8/28
 * @Description:
 */
@ControllerAdvice
public class ExceptionAdvice {
    static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Basepublic handleControllerException(HttpServletRequest request, Throwable e) {
        e.printStackTrace();
        if (e instanceof MethodArgumentNotValidException) {
            // 入参校验不通过失败

            logger.warn("未捕获的异常：", e);
            Object[] error = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors().stream().map(e1 -> String.format("参数：%s，异常：%s", e1.getField(), e1.getDefaultMessage())).toArray();
            return Basepublic.fail(error, "参数不合法");
        }
        if (e instanceof BindException) {
            // 入参序列化失败
            logger.warn("未捕获的异常：", e);
            Object[] error = ((BindException) e).getBindingResult().getFieldErrors().stream().map(e1 -> String.format("参数：%s，异常：%s", e1.getField(), e1.getRejectedValue())).toArray();
            return Basepublic.fail(error, "参数不合法");
        }

        logger.error("未捕获的异常：", e);
        return Basepublic.fail(null, "请求失败");
    }
}
