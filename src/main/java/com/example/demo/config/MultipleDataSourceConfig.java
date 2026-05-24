package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

//@Configuration
public class MultipleDataSourceConfig {
//	/ 主数据源（必须有@Primary注解）
//    @Bean(name = "userDataSource")
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource.user")
//    public DataSource userDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//    
//    // 第二个数据源
//    @Bean(name = "orderDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.order")
//    public DataSource orderDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//    
//    // 为每个数据源创建JdbcTemplate
//    @Bean(name = "userJdbcTemplate")
//    public JdbcTemplate userJdbcTemplate(
//            @Qualifier("userDataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//    
//    @Bean(name = "orderJdbcTemplate")
//    public JdbcTemplate orderJdbcTemplate(
//            @Qualifier("orderDataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
}
