package com.learn.blog.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-05-16:53
 */
//@Configuration
//@AutoConfigureAfter(MysqlGenTableConfig.class)
//public class MyBatisMapperScannerConfig {
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer() throws Exception{
//        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setBasePackage("com.example.mapper.*;com.gitee.sunchenbin.mybatis.actable.dao.*");
//        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//        return mapperScannerConfigurer;
//    }
//
//}
