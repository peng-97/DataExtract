package com.example.shape.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {
    @Bean
    @ConfigurationProperties(prefix = "mybatis.dcidmsconfiguration")
    public org.apache.ibatis.session.Configuration getMybatisDcidMsConfiguration() {
        return new org.apache.ibatis.session.Configuration();
    }

    @Bean
    @ConfigurationProperties(prefix = "mybatis.sourceorconfiguration")
    public org.apache.ibatis.session.Configuration getMybatisSourceorConfiguration() {
        return new org.apache.ibatis.session.Configuration();
    }
    @Bean
    @ConfigurationProperties(prefix = "mybatis.sourcetopconfiguration")
    public org.apache.ibatis.session.Configuration getMybatisSourcetopConfiguration() {
        return new org.apache.ibatis.session.Configuration();
    }
}
