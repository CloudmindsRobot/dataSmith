package com.cloudminds.data.smith.external.feishu.bitable.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据表字段属性
 *
 * @author Tao.Liu
 * @date 2022/6/22 16:46
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FieldProperty {

    public FieldProperty(final String... optionNames) {
        this.options = Arrays.stream(optionNames).map(e -> new Option(e)).collect(Collectors.toList());
    }

    /**
     * 单选、多选字段的选项信息
     */
    private List<Option> options;

    /**
     * 数字、公式字段的显示格式
     */
    private String formatter;

    /**
     * 日期、创建时间、最后更新时间字段的显示格式
     */
    private String dateFormatter;

    /**
     * 日期字段中新纪录自动填写创建时间
     */
    private Boolean autoFill;

    /**
     * 人员字段中允许添加多个成员，单向关联、双向关联中允许添加多个记录
     */
    private Boolean multiple;

    /**
     * 单向关联、双向关联字段中关联的数据表的id
     */
    private String tableId;

    /**
     * 单向关联、双向关联字段中关联的数据表的名字
     */
    private String tableName;

    /**
     * 双向关联字段中关联的数据表中对应的双向关联字段的名字
     */
    private String backFieldName;


    /**
     * 单选、多选字段的选项信息
     */
    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Option {

        /**
         * 选项id
         */
        private String id;

        /**
         * 选项名
         */
        private String name;

        /**
         * 选项颜色，取值范围：0 ～ 54
         */
        private Integer color;

        public Option(final String name) {
            this.name = name;
        }

    }

}
