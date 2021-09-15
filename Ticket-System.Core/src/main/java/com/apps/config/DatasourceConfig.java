package com.apps.config;

import com.apps.utils.CommonUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

public class DatasourceConfig {
    public static SqlSessionFactory sqlSessionFactory(DataSource dataSource, String locationResource) throws Exception {

        PathMatchingResourcePatternResolver pathMapper = new PathMatchingResourcePatternResolver();
        Resource[] mapperLocations = pathMapper.getResources(locationResource);

        return sqlSessionFactory(dataSource, mapperLocations);
    }

    public static SqlSessionFactory sqlSessionFactory(DataSource dataSource, Resource[] mapperLocations)
            throws Exception {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setVfs(SpringBootVFS.class);
        factoryBean.setTransactionFactory(new ManagedTransactionFactory());

        if (!CommonUtils.isNullOrEmpty(mapperLocations)) {
            factoryBean.setMapperLocations(mapperLocations);
        }

        SqlSessionFactory factory = factoryBean.getObject();
        assert factory != null;
        factory.getConfiguration().setMapUnderscoreToCamelCase(true);
        factory.getConfiguration().setUseGeneratedKeys(true);
        org.apache.ibatis.session.Configuration config = factory.getConfiguration();
        config.setJdbcTypeForNull(JdbcType.VARCHAR);

        return factory;
    }
}
