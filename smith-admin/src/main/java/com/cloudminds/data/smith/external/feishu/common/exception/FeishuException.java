package com.cloudminds.data.smith.external.feishu.common.exception;

import com.cloudminds.data.smith.exception.BaseServiceException;
import com.cloudminds.data.smith.exception.ServiceCodeEnum;
import com.cloudminds.data.smith.external.feishu.common.constant.FeishuErrorCodeEnum;

/**
 * 飞书异常
 *
 * @author Tao.Liu
 * @date 2022/6/30 10:28
 */
public class FeishuException extends BaseServiceException {

    private static final long serialVersionUID = -1191541920182893393L;

    public FeishuException(final FeishuErrorCodeEnum codeEnum, final String message) {
        super(ServiceCodeEnum.EXTERNAL_SYSTEM_ERROR, codeEnum, message);
    }

}
