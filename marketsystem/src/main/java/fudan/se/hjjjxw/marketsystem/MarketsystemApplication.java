package fudan.se.hjjjxw.marketsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarketsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketsystemApplication.class, args);
//        userService.createUser("username");
    }

//    @Bean
//    PlatformTransactionManager transactionManager(DataSourceProperties dataSourceProperties) {
//        return new DataSourceTransactionManager(datasource(dataSourceProperties));
//    }
//
//    @Bean
//    DataSource datasource(DataSourceProperties dataSourceProperties) {
//        return new DriverManagerDataSource(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
//    }

}
