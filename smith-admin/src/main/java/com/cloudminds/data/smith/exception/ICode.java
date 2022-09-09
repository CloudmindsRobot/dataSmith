package com.cloudminds.data.smith.exception;

/**
 * 错误码接口
 *
 * @author Tao.Liu
 * @date 2021/9/27 15:13
 */
public interface ICode {

    /**
     * 获取消息编号
     *
     * @return
     */
    String getCode();

    /**
     * 获取消息内容
     *
     * @return
     */
    String getMessage();

}
