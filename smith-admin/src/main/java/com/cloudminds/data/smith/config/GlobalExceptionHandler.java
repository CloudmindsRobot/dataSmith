
package com.cloudminds.data.smith.config;

import com.cloudminds.data.smith.dto.R;
import com.cloudminds.data.smith.exception.BaseErrorCodeEnum;
import com.cloudminds.data.smith.exception.BaseServiceException;
import com.cloudminds.data.smith.exception.ServiceCodeEnum;
import com.cloudminds.data.smith.util.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.Optional;

/**
 * 全局的的异常处理器
 *
 * @author admin
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数验证失败异常，后续需要废弃主动抛此类异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<R<Void>> validationExceptionHandler(HttpServletRequest request, ValidationException e) {
        log.warn("{} 参数检验异常---> ({}){}", request.getRequestURI(), e.getLocalizedMessage());
        return ResponseEntity.ok(R.error(e.getLocalizedMessage()));
    }

    /**
     * GET请求校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<R<Void>> validExceptionHandler(HttpServletRequest request, BindException e) {
        final FieldError fieldError = e.getFieldError();
        if (fieldError == null) {
            log.warn("{} 参数异常---> {}", request.getRequestURI(), e.getMessage());
            return ResponseEntity.ok(R.error(BaseErrorCodeEnum.PARAM_ERROR.getMessage()));
        }
        final Optional<String> optional = Arrays.stream(fieldError.getCodes())
                .filter(code -> Strings.equals(code, "typeMismatch")).findFirst();
        if (optional.isPresent()) {
            log.warn("{} 参数异常---> {}", request.getRequestURI(), fieldError.getDefaultMessage());
            return ResponseEntity.ok(R.error(BaseErrorCodeEnum.PARAM_INVALID, "field[" + fieldError.getField() + "] type mismatch"));
        }
        return this.handFieldError(request, fieldError);
    }

    /**
     * POST请求带Body的数校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<R<Void>> validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException e) {
        final FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError != null) {
            return this.handFieldError(request, fieldError);
        }
        return ResponseEntity.ok(R.success());
    }

    /**
     * 处理字段错误
     *
     * @param request
     * @param fieldError
     * @return
     */
    private ResponseEntity<R<Void>> handFieldError(final HttpServletRequest request, final FieldError fieldError) {
        final String message = fieldError.getField() + fieldError.getDefaultMessage();
        log.warn("{} 参数检验异常---> ({}){}", request.getRequestURI(), fieldError.getField(), fieldError.getDefaultMessage());
        return ResponseEntity.ok(R.error(message));
    }


    /**
     * 请求参数异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = ServletRequestBindingException.class)
    public ResponseEntity<R<Void>> validParameterExceptionHandler(final HttpServletRequest request, final Exception e) {
        log.warn("{} 参数异常---> {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.ok(R.error(BaseErrorCodeEnum.PARAM_MISS, e.getMessage()));
    }

    /**
     * 请求方式错误处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Object methodNotSupportedHandler(final HttpRequestMethodNotSupportedException e) {
        log.warn("错误的请求方式，message={}", e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(R.error("不支持的请求方式"));
    }

    /**
     * 业务基础异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = BaseServiceException.class)
    public ResponseEntity<R<Void>> validExceptionHandler(HttpServletRequest request, BaseServiceException e) {
        log.warn("{} 业务异常---> ({}){}", request.getRequestURI(), e.getCode(), e.getMessage());
        return ResponseEntity.ok(R.error(e.getServiceCode(), e.getCode(), e.getMessage()));
    }

    /**
     * 全局异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<R<Void>> exception(HttpServletRequest request, Exception e) {
        log.error("{} 系统异常", request.getRequestURI(), e);
        return ResponseEntity.ok(R.error(ServiceCodeEnum.SYSTEM_ERROR, BaseErrorCodeEnum.SYSTEM_ERROR));
    }

}
