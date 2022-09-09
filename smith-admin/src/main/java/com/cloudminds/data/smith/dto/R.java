package com.cloudminds.data.smith.dto;

import com.cloudminds.data.smith.exception.BaseErrorCodeEnum;
import com.cloudminds.data.smith.exception.ICode;
import com.cloudminds.data.smith.exception.IServiceCode;
import com.cloudminds.data.smith.exception.ServiceCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author lengleng
 */
@Data
@ApiModel(description = "响应信息体")
@Accessors(chain = true)
@Slf4j
public class R<T> implements Serializable {

    private static final long serialVersionUID = -5663717945191277844L;

    /**
     * 响应码
     */
    @ApiModelProperty("响应码，0为成功，其它表示异常")
    private Integer code;

    /**
     * 详细响应码
     */
    @ApiModelProperty("具体业务响应码，0为成功，其它表示异常")
    private String subCode;

    /**
     * 消息
     */
    @ApiModelProperty("异常消息描述")
    private String message;

    /**
     * 数据
     */
    @ApiModelProperty("响应数据")
    private T data;

    /**
     * 成功结果
     *
     * @return
     */
    public static <T> R<T> success() {
        return new R<T>(ServiceCodeEnum.SUCCESS);
    }

    /**
     * 成功结果
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R<T> success(final T data) {
        return new R<T>(ServiceCodeEnum.SUCCESS, data);
    }

    /**
     * 错误响应，响应参数错误
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> R<T> error(final String message) {
        return new R<>(ServiceCodeEnum.PARAM_ERROR, BaseErrorCodeEnum.PARAM_ERROR.getCode(), message);
    }

    /**
     * 参数错误响应
     *
     * @param subCodeEnum
     * @param message
     * @param <T>
     * @return
     */
    public static <T> R<T> error(final ICode subCodeEnum, final String message) {
        return new R<>(ServiceCodeEnum.PARAM_ERROR, subCodeEnum.getCode(), message);
    }

    /**
     * 业务错误响应
     *
     * @param subCodeEnum
     * @param <T>
     * @return
     */
    public static <T> R<T> business(final ICode subCodeEnum) {
        return new R<>(ServiceCodeEnum.BUSINESS_ERROR, subCodeEnum);
    }

    /**
     * 错误响应
     *
     * @param serviceCodeEnum
     * @param subCodeEnum
     * @param <T>
     * @return
     */
    public static <T> R<T> error(IServiceCode serviceCodeEnum, final ICode subCodeEnum) {
        return new R<>(serviceCodeEnum, subCodeEnum);
    }

    /**
     * 错误响应
     *
     * @param serviceCodeEnum
     * @param subCodeEnum
     * @param message
     * @param <T>
     * @return
     */
    public static <T> R<T> error(IServiceCode serviceCodeEnum, final ICode subCodeEnum, final String message) {
        return new R<>(serviceCodeEnum, subCodeEnum.getCode(), message);
    }

    /**
     * 错误响应
     *
     * @param serviceCodeEnum
     * @param subCode
     * @param message
     * @param <T>
     * @return
     */
    public static <T> R<T> error(IServiceCode serviceCodeEnum, final String subCode, final String message) {
        return new R<>(serviceCodeEnum, subCode, message);
    }

    public R() {

    }

    private R(final IServiceCode serviceCodeEnum) {
        this.code = serviceCodeEnum.getCode();
        this.message = serviceCodeEnum.getMessage();
    }

    private R(final IServiceCode serviceCodeEnum, final ICode subCodeEnum) {
        this(serviceCodeEnum);
        this.subCode = subCodeEnum.getCode();
        this.message = subCodeEnum.getMessage();
    }

    private R(final IServiceCode serviceCodeEnum, final T data) {
        this(serviceCodeEnum);
        this.data = data;
    }

    private R(final IServiceCode serviceCodeEnum, final String subCode, final String message) {
        this(serviceCodeEnum);
        this.subCode = subCode;
        this.message = message;
    }

}
