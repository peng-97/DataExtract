package com.example.shape.datasource;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.DruidPasswordCallback;
import com.example.shape.config.MybatisConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;


@Configuration
@MapperScan(basePackages = {"com.example.shape.mapper.sourceor"}, sqlSessionFactoryRef = "sourceorSqlSessionFactory")
public class SourceOrDataSourceConfig extends DruidPasswordCallback {
    @Autowired
    private MybatisConfig mybatisConfig;
    @Value("${spring.datasource.sourceor.password}")
    private String password;

//    @Value("${spring.datasource.workreform.publickey}")
//    private String publickey;

    @Bean(name = "sourceorDataSource")//注入到这个容器
    @ConfigurationProperties(prefix = "spring.datasource.sourceor")
    public DataSource sourceorDataSource() throws Exception {
//        return DataSourceBuilder.create().build();
        DruidPasswordCallback druidPasswordCallback = new DruidPasswordCallback();
        druidPasswordCallback.setPassword( password.toCharArray());
        DruidDataSource dataSource = new DruidDataSource();
        //这里是配置密码回调类
        dataSource.setPasswordCallback(druidPasswordCallback);
        dataSource.setUseGlobalDataSourceStat(true);

        return dataSource;
    }

    @Bean(name = "sourceorSqlSessionFactory")
    public SqlSessionFactory sourceorSqlSessionFactory(@Qualifier("sourceorDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfiguration(mybatisConfig.getMybatisSourceorConfiguration());
        return bean.getObject();
    }

    @Bean(name="sourceorTransactionManager")//配置事务
    public DataSourceTransactionManager sourceorTransactionManager(@Qualifier("sourceorDataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name="sourceorSqlSessionTemplate")
    public SqlSessionTemplate sourceorSqlSessionTemplate(@Qualifier("sourceorSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
