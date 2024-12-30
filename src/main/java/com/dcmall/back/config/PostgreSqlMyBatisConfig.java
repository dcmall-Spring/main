package com.dcmall.back.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
/*
Spring Boot application과 MyBatis를 구성하는 클래스
classpath : = src/main/resources

@Configuration : 설정 클래스임을 표현
@MapperScan : Mapper Interface가 있는 패키지를 지정 또한 사용할 SqlSessionFactory를 지정
Bean에 name을 붙이는 이유:
1.명확한 식별
2.특정 Bean 참조
@Qualifier 어노테이션은 Spring에서 빈(Bean) 주입 시 어떤 빈을 주입할지 명시적으로 지정하기 위해 사용

postgreSqlSessionFactory 메서드:

@Bean: 이 메서드가 Spring 컨텍스트에 의해 관리되는 Bean을 생성한다는 것을 나타낸다
@Qualifier("postgresDataSource"): 이 데이터 소스를 사용하여 SqlSessionFactory를 생성
SqlSessionFactoryBean: MyBatis의 SqlSessionFactory를 생성하는데 사용
setConfigLocation: MyBatis 설정 파일 경로를 지정합니다. 여기서는 "classpath:mybatis-postgres-config.xml" 파일을 사용

postgresTransactionManager 메서드:

@Bean: 이 메서드가 Spring 컨텍스트에 의해 관리되는 Bean을 생성한다는 것을 나타낸다
@Qualifier("postgresDataSource"): 이 데이터 소스를 사용하여 DataSourceTransactionManager를 생성
DataSourceTransactionManager: 데이터 소스에 대한 트랜잭션 관리를 제공

postgreSqlSessionTemplate 메서드:

@Bean: 이 메서드가 Spring 컨텍스트에 의해 관리되는 Bean을 생성한다는 것을 나타낸다
@Qualifier("postgreSqlSessionFactory"): 이 SqlSessionFactory를 사용하여 SqlSessionTemplate을 생성
SqlSessionTemplate: MyBatis의 SqlSession 구현체로, SQL 실행, 매퍼 인터페이스 호출 등을 처리
 */
@Configuration
@MapperScan(basePackages = "src.main.resources.mapper.postgres", sqlSessionFactoryRef = "postgreSqlSessionFactory")
public class PostgreSqlMyBatisConfig {

    @Bean(name = "postgreSqlSessionFactory")
    public SqlSessionFactory postgreSqlSessionFactory(@Qualifier("postgresDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // MyBatis 설정 파일 경로 설정
        Resource configLocation = new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-postgres-config.xml");
        sessionFactory.setConfigLocation(configLocation);

        return sessionFactory.getObject();
    }

    @Bean(name = "postgresTransactionManager")
    public DataSourceTransactionManager postgresTransactionManager(@Qualifier("postgresDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "postgreSqlSessionTemplate")
    public SqlSessionTemplate postgreSqlSessionTemplate(@Qualifier("postgreSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
