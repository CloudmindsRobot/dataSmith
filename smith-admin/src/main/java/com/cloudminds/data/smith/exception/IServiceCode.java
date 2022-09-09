package com.cloudminds.data.smith.exception;

/**
 * 服务编码
 *
 * @author Tao.Liu
 * @date 2021/12/10 13:42
 */
public interface IServiceCode {

    /**
     * 获取值
     *
     * @return
     */
    Integer getCode();

    /**
     * 获取消息内容
     *
     * @return
     */
    String getMessage();

}
