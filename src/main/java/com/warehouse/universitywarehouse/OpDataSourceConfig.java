@Configuration
@EnableJpaRepositories(
        basePackages = "com.warehouse.universitywarehouse.repository.op",
        entityManagerFactoryRef = "opEntityManagerFactory",
        transactionManagerRef = "opTransactionManager"
)
public class OpDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.op")
    public DataSource opDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "opEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean opEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(opDataSource())
                .packages("com.warehouse.universitywarehouse.model.op")
                .persistenceUnit("op")
                .build();
    }

    @Bean
    public PlatformTransactionManager opTransactionManager(
            @Qualifier("opEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
