package com.example.exception;

import com.example.entity.ErrorInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @ExceptionHandler({BizException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public  ErrorInfo handleBizException(BizException e){
        this.logger.info("------------全局捕获BIZ异常开始------------");
        ErrorInfo r = new ErrorInfo();
        r.setSuccess(false);
        r.setMsg(e.getMessage());
        this.logger.info("ErrorInfo==" + ToStringBuilder.reflectionToString(r));
        this.logger.info("------------全局捕获BIZ异常结束------------");
        return r;

    }
}
