package com.dcmall.back.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
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

기본 DataSource는 기본적으로 HikariCP의 기본 설정을 따라간다.
hikariConfig로 설정을 좀 더 구체적으로 해준 것에 불과 PreparedStatement 문제 해결에 직접적인 영향은 줄 수 없다.
설정을 안 해도 그만이지만, 조금 더 나은 환경을 위해 설정 해준 것.
나중에 불필요하면 날리자...
 */
@Configuration
public class PostgresDataSourceConfig {
    @Value("${postgres.datasource.jdbc-url}")
    private String jdbcUrl;
    @Value("${postgres.datasource.username}")
    private String username;
    @Value("${postgres.datasource.password}")
    private String password;
    @Value("${postgres.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${postgres.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;
    @Value("${postgres.datasource.hikari.minimum-idle}")
    private int minimumIdle;
    @Value("${postgres.datasource.hikari.idle-timeout}")
    private long idleTimeout;
    @Value("${postgres.datasource.hikari.max-lifetime}")
    private long maxLifetime;
    @Value("${postgres.datasource.hikari.connection-timeout}")
    private long connectionTimeout;
    @Value("${postgres.datasource.hikari.data-source-properties.cachePrepStmts}")
    private boolean cachePrepStmts;
    @Value("${postgres.datasource.hikari.data-source-properties.prepStmtCacheSize}")
    private int prepStmtCacheSize;
    @Value("${postgres.datasource.hikari.data-source-properties.prepStmtCacheSqlLimit}")
    private int prepStmtCacheSqlLimit;
    @Value("${postgres.datasource.hikari.data-source-properties.useServerPrepStmts}")
    private boolean useServerPrepStmts;

    @Bean(name = "postgresDataSource")
    public DataSource postgresDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);        // 데이터베이스 연결 URL 설정
        hikariConfig.setUsername(username);      // 데이터베이스 접속 사용자명
        hikariConfig.setPassword(password);      // 데이터베이스 접속 비밀번호
        hikariConfig.setDriverClassName(driverClassName);  // JDBC 드라이버 클래스명

        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        // 동시에 유지할 수 있는 최대 커넥션 수. 높으면 동시 처리 능력 향상, 너무 높으면 DB 부하 증가

        hikariConfig.setMinimumIdle(minimumIdle);
        // 유휴(idle) 상태로 유지할 최소 커넥션 수. 커넥션 생성 오버헤드 감소, 너무 높으면 리소스 낭비

        hikariConfig.setIdleTimeout(idleTimeout);
        // 유휴 커넥션이 풀에서 제거되기까지의 시간(ms). 리소스 효율성과 연결 가용성 사이의 균형

        hikariConfig.setMaxLifetime(maxLifetime);
        // 커넥션의 최대 수명(ms). 오래된 연결 제거로 안정성 향상, 너무 짧으면 불필요한 재연결 발생

        hikariConfig.setConnectionTimeout(connectionTimeout);
        // 새 커넥션을 얻기 위해 대기하는 최대 시간(ms). 긴 대기 시간 방지, 너무 짧으면 불필요한 오류 발생

        hikariConfig.addDataSourceProperty("cachePrepStmts", cachePrepStmts);
        // PreparedStatement 캐시 사용 여부. 활성화 시 반복 쿼리 성능 향상, 메모리 사용량 증가

        hikariConfig.addDataSourceProperty("prepStmtCacheSize", prepStmtCacheSize);
        // 캐시할 PreparedStatement의 수. 높으면 더 많은 쿼리 캐싱, 메모리 사용량 증가

        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit);
        // 캐시할 SQL 문의 최대 길이. 긴 쿼리도 캐시 가능, 너무 크면 메모리 사용량 증가

        hikariConfig.addDataSourceProperty("useServerPrepStmts", useServerPrepStmts);
        // 서버 사이드 PreparedStatement 사용 여부. 활성화 시 DB 서버에서 쿼리 최적화, 네트워크 부하 감소

        return new HikariDataSource(hikariConfig);
    }


//    @Bean(name = "postgresDataSource")
//    @ConfigurationProperties(prefix = "postgres.datasource")
//    public DataSource postgresDataSource() {
//        return DataSourceBuilder.create().build();
//    }
}