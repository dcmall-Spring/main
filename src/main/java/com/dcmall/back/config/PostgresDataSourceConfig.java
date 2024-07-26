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

PreparedStatement 오류 발생 원인(여기선 제대로 sql이 끝나지 않은 상태에서 다시 똑같은 sql문을 실행했을 때 주로 터졌으니 동시성 문제일 가능성이 높음):

리소스 누수: PreparedStatement가 제대로 닫히지 않고 계속 열려있는 경우
이름 충돌: 같은 이름의 PreparedStatement가 중복 생성되는 경우
동시성 문제: 여러 스레드가 동시에 같은 PreparedStatement를 사용하려 할 때
서버 리소스 부족: 너무 많은 PreparedStatement가 동시에 생성되어 서버 리소스를 고갈시키는 경우

각 설정이 오류를 방지하는 방법:

cachePrepStmts (PreparedStatement 캐싱):

작동 원리: PreparedStatement를 재사용 가능한 형태로 캐시합니다.
오류 방지: 동일한 쿼리에 대해 새로운 PreparedStatement를 매번 생성하지 않아 리소스 누수와 이름 충돌을 방지합니다.


prepStmtCacheSize (PreparedStatement 캐시 크기):

작동 원리: 각 커넥션에서 캐시할 수 있는 PreparedStatement의 수를 제한합니다.
오류 방지: 과도한 메모리 사용을 방지하고, 적절한 수의 PreparedStatement만 유지하여 서버 리소스 고갈을 막습니다.


prepStmtCacheSqlLimit (캐시할 SQL 문의 최대 길이):

작동 원리: 너무 긴 SQL 문은 캐시하지 않도록 제한합니다.
오류 방지: 메모리 사용을 최적화하고, 비정상적으로 긴 쿼리로 인한 리소스 문제를 방지합니다.


useServerPrepStmts (서버 사이드 PreparedStatement 사용):

작동 원리: PreparedStatement를 데이터베이스 서버에서 처리하도록 합니다.
오류 방지: 클라이언트 측의 PreparedStatement 관리 부담을 줄이고, 서버에서 더 효율적으로 관리하여 동시성 문제를 완화합니다.


maximumPoolSize (최대 커넥션 풀 크기):

작동 원리: 동시에 유지할 수 있는 최대 데이터베이스 커넥션 수를 제한합니다.
오류 방지: 과도한 커넥션 생성을 방지하여 서버 리소스 고갈을 막고, 각 커넥션이 관리하는 PreparedStatement의 수를 간접적으로 제한합니다.


minimumIdle (최소 유휴 커넥션 수):

작동 원리: 항상 유지할 최소한의 유휴 커넥션 수를 설정합니다.
오류 방지: 갑작스러운 요청 증가 시 새 PreparedStatement 생성에 따른 부하를 줄입니다.


idleTimeout (유휴 커넥션 타임아웃):

작동 원리: 일정 시간 동안 사용되지 않은 커넥션을 제거합니다.
오류 방지: 오래된 커넥션과 그에 연관된 PreparedStatement를 정리하여 리소스 누수를 방지합니다.


maxLifetime (커넥션 최대 수명):

작동 원리: 커넥션의 최대 수명을 제한합니다.
오류 방지: 오래된 커넥션을 주기적으로 갱신하여 PreparedStatement 관련 문제가 누적되는 것을 방지합니다.



이러한 설정들이 조화롭게 작동하면:

PreparedStatement의 생성과 재사용이 효율적으로 관리됩니다.
리소스 사용이 최적화되어 서버 부하가 줄어듭니다.
동시성 문제가 완화되어 PreparedStatement 충돌이 감소합니다.
오래된 리소스가 적절히 정리되어 누수가 방지됩니다.

결과적으로, 이러한 종합적인 접근 방식이 PreparedStatement 관련 오류를 효과적으로 방지하고 전반적인 데이터베이스 연결 성능을 향상시킵니다.
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
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);

        hikariConfig.setMaximumPoolSize(maximumPoolSize);   //동시 유지할 수 있는 최대 커넥션 수
        hikariConfig.setMinimumIdle(minimumIdle);   //유휴(idle) 상태로 유지할 최소 커넥션 수
        hikariConfig.setIdleTimeout(idleTimeout);   //유휴 커넥션이 풀에서 제거 되기까지의 시간
        hikariConfig.setMaxLifetime(maxLifetime);   //커넥션의 최대 수명
        hikariConfig.setConnectionTimeout(connectionTimeout); //새 커넥션을 얻기 위해 대기하는 최대 시간을 지정
        hikariConfig.addDataSourceProperty("cachePrepStmts", cachePrepStmts);   //preparedStatement 캐시 여부
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", prepStmtCacheSize); //캐시할 preparedStatment의 수
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit); //캐시할 SQL 문의 최대 길이를 지정합니다.
        hikariConfig.addDataSourceProperty("useServerPrepStmts", useServerPrepStmts); // 서버 사이드 PreparedStatement 사용 여부를 결정 true로 하면, PreparedStatement가 데이터베이스 서버에서 처리되어 더 효율적인 쿼리 실행이 가능

        return new HikariDataSource(hikariConfig);
    }


//    @Bean(name = "postgresDataSource")
//    @ConfigurationProperties(prefix = "postgres.datasource")
//    public DataSource postgresDataSource() {
//        return DataSourceBuilder.create().build();
//    }
}