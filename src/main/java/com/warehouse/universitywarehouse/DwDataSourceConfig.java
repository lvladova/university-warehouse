import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.warehouse.universitywarehouse.repository.dw",
        entityManagerFactoryRef = "dwEntityManagerFactory",
        transactionManagerRef = "dwTransactionManager"
)
public class DwDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.dw")
    public DataSource dwDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dwEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean dwEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dwDataSource())
                .packages("com.warehouse.universitywarehouse.model.dw")
                .persistenceUnit("dw")
                .build();
    }

    @Bean
    public PlatformTransactionManager dwTransactionManager(
            @Qualifier("dwEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
