package com.cloudminds.data.smith.dto;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudminds.data.smith.util.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页响应结构体
 *
 * @author Tao.Liu
 * @date 2022/6/29 15:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页响应")
public class PageResult<T> {

    @ApiModelProperty("每页显示条数，默认 10")
    private Long size;

    @ApiModelProperty("总数")
    private Long total;

    @ApiModelProperty("数据")
    private List<T> records;

    /**
     * 数据转换
     *
     * @param mapper
     * @param <R>
     * @return
     */
    public <R> PageResult<R> convert(final Function<? super T, ? extends R> mapper) {
        final List<R> records = this.getRecords().stream().map(mapper).collect(Collectors.toList());
        return new PageResult<>(this.getSize(), this.getTotal(), records);
    }

    /**
     * 数据类型转换
     *
     * @param clazz
     * @param <E>
     * @return
     */
    public <E> PageResult<E> convert(final Class<E> clazz) {
        final List<E> list = BeanUtil.copyToList(this.getRecords(), clazz);
        return new PageResult<>(this.getSize(), this.getTotal(), list);
    }


    /**
     * 转换mybatis plus分页参数
     *
     * @param page
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> of(final IPage<T> page) {
        final PageResult pageResult = new PageResult();
        pageResult.setSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }

    /**
     * 转换mybatis plus分页实体
     *
     * @param page
     * @param <T>
     * @return
     */
    public static <T, E> PageResult<T> of(final IPage<E> page, final Class<T> clazz) {
        final List<T> list = BeanUtil.copyToList(page.getRecords(), clazz);
        return new PageResult<>(page.getSize(), page.getTotal(), list);
    }

    /**
     * 空分页
     *
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> empty() {
        final PageResult pageResult = new PageResult();
        pageResult.setSize(10L);
        pageResult.setTotal(0L);
        pageResult.setRecords(Lists.emptyList());
        return pageResult;
    }

}
