package com.bonc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author xiongwei
 * @WriteTime 2021-03-24 16:41
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    //基本信息，页面展示
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("测试项目标题")
                .description("接口描述")
                //联系人实体类
                .contact(
                        new Contact("名字", "网址", "邮箱")
                )
                //版本号
                .version("1.0.0-SNAPSHOT")
                .build();
    }
}