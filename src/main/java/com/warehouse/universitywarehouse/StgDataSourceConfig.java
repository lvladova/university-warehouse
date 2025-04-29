@Configuration
@EnableJpaRepositories(
        basePackages = "com.warehouse.universitywarehouse.repository.stg",
        entityManagerFactoryRef = "stgEntityManagerFactory",
        transactionManagerRef = "stgTransactionManager"
)
public class StgDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.stg")
    public DataSource stgDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "stgEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean stgEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(stgDataSource())
                .packages("com.warehouse.universitywarehouse.model.stg")
                .persistenceUnit("stg")
                .build();
    }

    @Bean
    public PlatformTransactionManager stgTransactionManager(
            @Qualifier("stgEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
