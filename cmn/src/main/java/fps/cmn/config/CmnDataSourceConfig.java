package fps.cmn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class CmnDataSourceConfig {

    @Value("${spring.datasource.cmn.url}")
    private String url;

    @Value("${spring.datasource.cmn.username}")
    private String username;

    @Value("${spring.datasource.cmn.password}")
    private String password;

    @Value("${spring.datasource.cmn.driver-class-name}")
    private String driverClassName;

    @Bean(name = "cmnDataSource")
    public DataSource cmnDataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

//    private final Environment env;
//
//    @Autowired
//    public CmnDataSourceConfig(Environment env) {
//        this.env = env;
//    }
//    @Bean(name = "cmnDataSource")
//    public DataSource cmnDataSource() throws Exception {
//        String jndiName = env.getProperty("spring.datasource.cmn.jndi-name");
//        return (DataSource) new JndiTemplate().lookup(jndiName);
//    }

}
