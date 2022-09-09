package com.cloudminds.data.smith.config;

import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * swagger文档配置
 *
 * @author Tao.Liu
 * @date 2022/6/28 17:07
 */
@Configuration
@EnableSwagger2WebMvc
@ConditionalOnExpression("${knife4j.switch:false}")
public class Knife4jConfiguration {

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder().title("数据自动化导出平台接口API").version("v1.0").build())
                .directModelSubstitute(LocalTime.class, String.class)
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(LocalDateTime.class, String.class)
                .select().apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any()).build().globalOperationParameters(buildHeaderList());
    }

    /**
     * 构建请求头参数
     *
     * @return
     */
    public static List<Parameter> buildHeaderList() {
        final List<Parameter> parameterList = new ArrayList<>();
        parameterList.add((new ParameterBuilder()).name("authorization").description("认证信息")
                .modelRef(new ModelRef("string")).defaultValue("").parameterType("header")
                .required(false).build());
        return parameterList;
    }
}
