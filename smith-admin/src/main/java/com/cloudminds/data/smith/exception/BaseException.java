package com.cloudminds.data.smith.exception;

/**
 * 异常处理父类，所有的异常处理基于该类派生
 *
 * @author Tao.Liu
 * @date 2021/12/9 15:15
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -1535405572500029548L;

    public BaseException(final String message) {
        super(message);
    }

    public BaseException(final Throwable cause) {
        super(cause);
    }

    public BaseException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
