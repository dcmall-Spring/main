package com.dcmall.back.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
/*
이 클래스는 Spring Boot 애플리케이션에서 Postgres 데이터 소스를 설정하는 구성 클래스입니다.
@Configuration 어노테이션을 사용하여 설정 클래스로 지정되고,
@Bean과 @ConfigurationProperties 어노테이션을 사용하여 postgres.datasource로 시작하는 속성값들을 자동으로 매핑하여
DataSource 객체를 생성하고 Spring 컨텍스트에 Bean으로 등록합니다.

DataSource : DB와의 연결을 관리하는 객체, JDBC를 통해 DB에 연결하고 SQL 쿼리를 실행하는데 필요한 설정 정보를 캡슐화한다.
주요 기능 :
데이터베이스 연결 풀링: 여러 데이터베이스 연결을 미리 만들어 두고 필요할 때 재사용하여 성능을 향상시킵니다.
데이터베이스 연결 구성: 데이터베이스 URL, 사용자 이름, 비밀번호 등의 정보를 포함하여 데이터베이스에 연결합니다.

구성 이유 :
MyBatis는 자바 애플리케이션에서 SQL, 저장 프로시저 및 고급 매핑을 지원하는 퍼시스턴스 프레임워크이다. MyBatis가 데이터베이스와 상호작용할 때 데이터 소스가 필요
1.데이터베이스 연결 관리
2.연결 풀링
3.중앙 집중식 설정 관리
4.트랜잭션 관리

요약
MyBatis에서 데이터 소스를 구성하는 것은 애플리케이션이 데이터베이스와 효율적으로 상호작용하기 위해 필수적입니다.
데이터 소스는 데이터베이스 연결을 관리하고 성능을 최적화하며 트랜잭션 관리를 지원합니다.
MyBatis는 데이터 소스를 통해 데이터베이스와 연결하고 SQL 쿼리를 실행하여 데이터베이스 작업을 수행합니다.

순서도
1.application.properties의 (postgres.datasource)로 시작하는 설정들을 가져와 DataSource를 생성한다:
    application.properties 파일에 데이터베이스 연결에 필요한 URL, 사용자 이름, 비밀번호, 드라이버 클래스 이름 등의 설정을 정의합니다.
    PostgresDataSourceConfig 클래스에서 @ConfigurationProperties(prefix = "postgres.datasource") 어노테이션을 사용하여 이 설정들을 가져옵니다.
    DataSourceBuilder.create().build() 메서드를 통해 DataSource 객체를 생성합니다.

2.연결에 성공하면 PostgreSqlMyBatisConfig에서 SqlSessionFactory 생성:
    PostgreSqlMyBatisConfig 클래스에서 DataSource를 받아 MyBatis의 SqlSessionFactory를 생성합니다.
    SqlSessionFactoryBean을 사용하여 SqlSessionFactory를 설정하고, MyBatis 설정 파일 경로를 지정합니다.
    생성된 SqlSessionFactory는 MyBatis가 SQL 세션을 생성하고 데이터베이스와 상호작용하는 데 사용됩니다.
    DataSourceTransactionManager와 SqlSessionTemplate도 설정하여 트랜잭션 관리 및 SQL 세션 관리를 지원합니다.
 */
@Configuration
public class PostgresDataSourceConfig {

    @Bean(name = "postgresDataSource")
    @ConfigurationProperties(prefix = "postgres.datasource")
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create().build();
    }
}