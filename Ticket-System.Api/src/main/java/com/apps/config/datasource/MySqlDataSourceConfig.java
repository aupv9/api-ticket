package com.apps.config.datasource;


import com.apps.config.DatasourceConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@Configuration
@Slf4j
@MapperScan(basePackages = {
        MySqlDataSourceConfig.BASE_PACKAGE_CORE,
        MySqlDataSourceConfig.BASE_PACKAGE_AUTHENTICATE},
            sqlSessionTemplateRef = MySqlDataSourceConfig.SESSION_FACTORY_TEMPLATE)
public class MySqlDataSourceConfig {
    public static final String BASE_PACKAGE_CORE = "com.apps.mybatis.mysql";
    public static final String BASE_PACKAGE_AUTHENTICATE = "com.apps.authenticate.repository";

    public static final String MAPPER_LOCATION_PATTERN = "classpath:/mappers/mysql/**/*.xml";
    public static final String SESSION_FACTORY_TEMPLATE = "mysqlSessionFactoryTemplate";
    public static final String SESSION_FACTORY = "mysqlSessionFactory";

    @Bean(name = SESSION_FACTORY)
    public SqlSessionFactory createSqlSessionFactory(DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        Resource[] resources = applicationContext.getResources(MAPPER_LOCATION_PATTERN);
        SqlSessionFactory sqlSessionFactory = DatasourceConfig.sqlSessionFactory(dataSource,resources);
        if (dataSource instanceof HikariDataSource) {
            log.info("HikariDataSource MySQL url {}", ((HikariDataSource) dataSource).getJdbcUrl());
        }
        org.apache.ibatis.session.Configuration config = sqlSessionFactory.getConfiguration();
        config.setJdbcTypeForNull(JdbcType.VARCHAR);
        return sqlSessionFactory;
    }

    @Bean(name = SESSION_FACTORY_TEMPLATE)
    public SqlSessionTemplate oracleSqlSessionTemplate(@Qualifier(SESSION_FACTORY) SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
