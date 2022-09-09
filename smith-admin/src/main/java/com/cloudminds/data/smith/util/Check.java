package com.cloudminds.data.smith.util;

import com.cloudminds.data.smith.exception.BaseServiceException;
import com.cloudminds.data.smith.exception.BusinessException;
import com.cloudminds.data.smith.exception.ICode;
import com.cloudminds.data.smith.exception.ParameterException;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 断言工具类，用于断言程序中的参数，确保参数的可用性，避免NPE
 *
 * @author Tao.Liu
 * @date 2021/9/13 10:24
 */
public abstract class Check {

    /**
     * 断言值为true
     *
     * @param isTrue  被断言的值
     * @param message 不符合断言要抛出的异常消息
     * @param params  不符合断言要抛出的异常消息参数
     */
    public static void isTrue(final boolean isTrue, final String message, final Object... params) {
        isTrue(isTrue, () -> new ParameterException(buildMessage(message, params)));
    }

    /**
     * 断言值为true
     *
     * @param isTrue    被断言的值
     * @param errorCode 错误码
     */
    public static void isTrue(final boolean isTrue, final ICode errorCode) {
        isTrue(isTrue, () -> new BusinessException(errorCode));
    }

    /**
     * 断言值为true
     *
     * @param isTrue 被断言的值
     * @param ex     自定义异常
     */
    public static void isTrue(final boolean isTrue, final Supplier<BaseServiceException> ex) {
        if (!isTrue) {
            throw ex.get();
        }
    }

    /**
     * 断言对象为null
     *
     * @param object  被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params  不符合断言要抛出的异常消息参数
     */
    public static void isNull(final Object object, final String message, final Object... params) {
        isNull(object, () -> new ParameterException(buildMessage(message, params)));
    }

    /**
     * 断言对象为null
     *
     * @param object    被断言的对象
     * @param errorCode 错误码
     */
    public static void isNull(final Object object, final ICode errorCode) {
        isNull(object, () -> new BusinessException(errorCode));
    }

    /**
     * 断言对象为null
     *
     * @param object 被断言的对象
     * @param ex     自定义异常
     */
    public static void isNull(final Object object, final Supplier<BaseServiceException> ex) {
        if (object != null) {
            throw ex.get();
        }
    }

    /**
     * 断言对象不为null
     *
     * @param object  被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params  不符合断言要抛出的异常消息参数
     */
    public static void notNull(final Object object, final String message, final Object... params) {
        notNull(object, () -> new ParameterException(buildMessage(message, params)));
    }

    /**
     * 断言对象不为null
     *
     * @param object    被断言的对象
     * @param errorCode 错误码
     */
    public static void notNull(final Object object, final ICode errorCode) {
        notNull(object, () -> new BusinessException(errorCode));
    }

    /**
     * 断言对象不为null
     *
     * @param object 被断言的对象
     * @param ex     自定义异常
     */
    public static void notNull(final Object object, final Supplier<BaseServiceException> ex) {
        if (object == null) {
            throw ex.get();
        }
    }

    /**
     * 断言String对象不为空
     *
     * @param object  被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params  不符合断言要抛出的异常消息参数
     */
    public static void hasText(final String object, final String message, final Object... params) {
        hasText(object, () -> new ParameterException(buildMessage(message, params)));
    }

    /**
     * 断言String对象不为空
     *
     * @param object    被断言的对象
     * @param errorCode 错误码
     */
    public static void hasText(final String object, final ICode errorCode) {
        hasText(object, () -> new BusinessException(errorCode));
    }

    /**
     * 断言String对象不为空
     *
     * @param object 被断言的对象
     * @param ex     自定义异常
     */
    public static void hasText(final String object, final Supplier<BaseServiceException> ex) {
        if (Strings.isBlank(object)) {
            throw ex.get();
        }
    }

    /**
     * 断言集合对象为空
     *
     * @param object  被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params  不符合断言要抛出的异常消息参数
     */
    public static void isEmpty(final Collection object, final String message, final Object... params) {
        isEmpty(object, () -> new ParameterException(buildMessage(message, params)));
    }

    /**
     * 断言集合对象为空
     *
     * @param object    被断言的对象
     * @param errorCode 错误码
     */
    public static void isEmpty(final Collection object, final ICode errorCode) {
        isEmpty(object, () -> new BusinessException(errorCode));
    }

    /**
     * 断言集合对象为空
     *
     * @param object 被断言的对象
     * @param ex     自定义异常
     */
    public static void isEmpty(final Collection object, final Supplier<BaseServiceException> ex) {
        if (Lists.isNotEmpty(object)) {
            throw ex.get();
        }
    }

    /**
     * 断言集合对象不为空
     *
     * @param object  被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params  不符合断言要抛出的异常消息参数
     */
    public static void notEmpty(final Collection object, final String message, final String... params) {
        notEmpty(object, () -> new ParameterException(buildMessage(message, params)));
    }

    /**
     * 断言集合对象不为空
     *
     * @param object    被断言的对象
     * @param errorCode 错误码
     */
    public static void notEmpty(final Collection object, final ICode errorCode) {
        notEmpty(object, () -> new BusinessException(errorCode));
    }

    /**
     * 断言集合对象不为空
     *
     * @param object 被断言的对象
     * @param ex     自定义异常
     */
    public static void notEmpty(final Collection object, final Supplier<BaseServiceException> ex) {
        if (object == null || object.isEmpty()) {
            throw ex.get();
        }
    }

    /**
     * 断言Map对象不为空
     *
     * @param object  被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params  不符合断言要抛出的异常消息参数
     */
    public static void notEmpty(final Map object, final String message, final String... params) {
        notEmpty(object, () -> new ParameterException(buildMessage(message, params)));
    }

    /**
     * 断言Map对象不为空
     *
     * @param object    被断言的对象
     * @param errorCode 错误码
     */
    public static void notEmpty(final Map object, final ICode errorCode) {
        notEmpty(object, () -> new BusinessException(errorCode));
    }

    /**
     * 断言Map对象不为空
     *
     * @param object 被断言的对象
     * @param ex     自定义异常
     */
    public static void notEmpty(final Map object, final Supplier<BaseServiceException> ex) {
        if (object == null || object.isEmpty()) {
            throw ex.get();
        }
    }


    /**
     * 断言数组对象不为空
     *
     * @param array   被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params  不符合断言要抛出的异常消息参数
     */
    public static void notEmpty(final Object[] array, final String message, final String... params) {
        notEmpty(array, () -> new ParameterException(buildMessage(message, params)));
    }

    /**
     * 断言数组对象不为空
     *
     * @param array     被断言的对象
     * @param errorCode 错误码
     */
    public static void notEmpty(final Object[] array, final ICode errorCode) {
        notEmpty(array, () -> new BusinessException(errorCode));
    }

    /**
     * 断言数组对象不为空
     *
     * @param array 被断言的对象
     * @param ex    自定义异常
     */
    public static void notEmpty(final Object[] array, final Supplier<BaseServiceException> ex) {
        if (array == null || array.length == 0) {
            throw ex.get();
        }
    }

    /**
     * 构建格化式消息
     *
     * @param msg
     * @param params
     * @return
     */
    private static String buildMessage(String msg, Object... params) {
        if (params == null || params.length <= 0) {
            return msg;
        }
        return String.format(msg, params);
    }

}
