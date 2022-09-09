package com.cloudminds.data.smith.external.feishu.common.constant;

import com.cloudminds.data.smith.exception.ICode;

/**
 * 飞书错误码
 *
 * @author Tao.Liu
 * @date 2021/12/9 14:50
 */
public enum FeishuErrorCodeEnum implements ICode {

    TABLE_ERROR("smith.feishu_table_error", "飞书接口响应异常"),
    TRY_AGAIN_LATER("cade.feishu_try_again_later", "请求超时，请稍后重试"),
    ;
    /**
     * 错误码
     */
    private final String code;

    /**
     * 消息
     */
    private final String message;

    FeishuErrorCodeEnum(final String code, final String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
