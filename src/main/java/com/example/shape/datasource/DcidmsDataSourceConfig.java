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
@MapperScan(basePackages = {"com.example.shape.mapper.dcidms"}, sqlSessionFactoryRef = "dcidmsSqlSessionFactory")
public class DcidmsDataSourceConfig extends DruidPasswordCallback {
    @Autowired
    private MybatisConfig mybatisConfig;
    @Value("${spring.datasource.dcidms.password}")
    private String password;
//    @Value("${spring.datasource.workreform.publickey}")
//    private String publickey;

    @Bean(name = "dcidmsDataSource")//注入到这个容器
    @ConfigurationProperties(prefix = "spring.datasource.dcidms")
    public DataSource dcidmsDataSource() throws Exception {
//        return DataSourceBuilder.create().build();
        DruidPasswordCallback druidPasswordCallback = new DruidPasswordCallback();
        druidPasswordCallback.setPassword(password.toCharArray());
        DruidDataSource dataSource = new DruidDataSource();
        //这里是配置密码回调类
        dataSource.setPasswordCallback(druidPasswordCallback);
        dataSource.setUseGlobalDataSourceStat(true);

        return dataSource;
    }

    @Bean(name = "dcidmsSqlSessionFactory")
    public SqlSessionFactory dcidmsSqlSessionFactory(@Qualifier("dcidmsDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfiguration(mybatisConfig.getMybatisDcidMsConfiguration());
        return bean.getObject();
    }

    @Bean(name="dcidmsTransactionManager")//配置事务
    public DataSourceTransactionManager dcidmsTransactionManager(@Qualifier("dcidmsDataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name="dcidmsSqlSessionTemplate")
    public SqlSessionTemplate dcidmsSqlSessionTemplate(@Qualifier("dcidmsSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
