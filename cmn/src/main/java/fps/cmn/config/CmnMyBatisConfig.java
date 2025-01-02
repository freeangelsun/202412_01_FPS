package fps.cmn.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "fps.cmn") // fps.cmn 패키지 전체 빈 스캔
@MapperScan(basePackages = "fps.cmn.db.mapper", sqlSessionFactoryRef = "cmnSqlSessionFactory") // 매퍼 인터페이스 패키지 지정
public class CmnMyBatisConfig {

    private final DataSource cmnDataSource;

    // DataSource 명확하게 지정
    public CmnMyBatisConfig(@Qualifier("cmnDataSource") DataSource cmnDataSource) {
        this.cmnDataSource = cmnDataSource;
    }

    @Bean(name = "cmnSqlSessionFactory")
    public SqlSessionFactory cmnSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(cmnDataSource);

        // MyBatis 설정 파일 지정
        sqlSessionFactoryBean.setConfigLocation(
                new ClassPathResource("mybatis/config/cmn-mybatis-config.xml")
        );

        // 매퍼 XML 파일 경로 지정
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mybatis/mapper/cmn/**/*.xml")
        );

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "cmnSqlSessionTemplate")
    public SqlSessionTemplate cmnSqlSessionTemplate(
            @Qualifier("cmnSqlSessionFactory") SqlSessionFactory cmnSqlSessionFactory) {
        return new SqlSessionTemplate(cmnSqlSessionFactory);
    }
}
