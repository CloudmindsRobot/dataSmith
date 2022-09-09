package com.cloudminds.data.smith.external.feishu.bitable.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * 飞书人员对象
 *
 * @author Tao.Liu
 * @date 2022/6/29 20:15
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Person {

    /**
     * 人员Id
     */
    private String id;

    /**
     * 中文姓名
     */
    private String name;

    /**
     * 英文姓名
     */
    private String enName;

    /**
     * 邮箱
     */
    private String email;
}
