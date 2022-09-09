package com.cloudminds.data.smith.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * 业务异常
 *
 * @author Tao.Liu
 * @date 2021/12/9 14:31
 */
@Slf4j
public class BusinessException extends BaseServiceException {

    private static final long serialVersionUID = -1191541920182893393L;

    public BusinessException(final ICode codeEnum) {
        super(ServiceCodeEnum.BUSINESS_ERROR, codeEnum);
    }

    public BusinessException(final ICode codeEnum, final Throwable cause) {
        super(ServiceCodeEnum.BUSINESS_ERROR, codeEnum, cause);
    }

}
