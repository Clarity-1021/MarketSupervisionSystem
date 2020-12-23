package fudan.se.hjjjxw.marketsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@SpringBootApplication
public class MarketsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketsystemApplication.class, args);
//        userService.createUser("username");
    }

    @Bean
    PlatformTransactionManager transactionManager(DataSourceProperties dataSourceProperties) {
        return new DataSourceTransactionManager(datasource(dataSourceProperties));
    }

    @Bean
    DataSource datasource(DataSourceProperties dataSourceProperties) {
        return new DriverManagerDataSource(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
    }

}
