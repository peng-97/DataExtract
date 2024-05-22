package com.example.shape.datasource;

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
@MapperScan(basePackages = {"com.example.shape.mapper.sourcetop"}, sqlSessionFactoryRef = "sourcetopSqlSessionFactory")
public class SourceTopDataSourceConfig extends DruidPasswordCallback {
    @Autowired
    private MybatisConfig mybatisConfig;
    @Value("${spring.datasource.sourcetop.password}")
    private String password;


    @Bean(name = "sourcetopDataSource")//注入到这个容器
    @ConfigurationProperties(prefix = "spring.datasource.sourcetop")
    public DataSource sourcetopDataSource() throws Exception {
//        return DataSourceBuilder.create().build();
        DruidPasswordCallback druidPasswordCallback = new DruidPasswordCallback();
        druidPasswordCallback.setPassword( password.toCharArray());
        DruidDataSource dataSource = new DruidDataSource();
        //这里是配置密码回调类
        dataSource.setPasswordCallback(druidPasswordCallback);
        dataSource.setUseGlobalDataSourceStat(true);

        return dataSource;
    }

    @Bean(name = "sourcetopSqlSessionFactory")
    public SqlSessionFactory sourcetopSqlSessionFactory(@Qualifier("sourcetopDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfiguration(mybatisConfig.getMybatisSourcetopConfiguration());
        return bean.getObject();
    }

    @Bean(name="sourcetopTransactionManager")//配置事务
    public DataSourceTransactionManager sourcetopTransactionManager(@Qualifier("sourcetopDataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name="sourcetopSqlSessionTemplate")
    public SqlSessionTemplate sourcetopSqlSessionTemplate(@Qualifier("sourcetopSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
