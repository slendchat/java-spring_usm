package com.example.library.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactory(DataSource dataSource) {
        Properties props = new Properties();
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", true);
        props.put("hibernate.current_session_context_class", 
                  "org.springframework.orm.hibernate5.SpringSessionContext");

        Configuration cfg = new Configuration();
        cfg.setProperties(props);
        cfg.addAnnotatedClass(com.example.library.entity.Book.class);
        cfg.addAnnotatedClass(com.example.library.entity.Author.class);
        cfg.addAnnotatedClass(com.example.library.entity.Publisher.class);
        cfg.addAnnotatedClass(com.example.library.entity.Category.class);
        cfg.addAnnotatedClass(com.example.library.entity.Library.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(props)
                .applySetting("hibernate.connection.datasource", dataSource);

        return cfg.buildSessionFactory(builder.build());
    }

    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}
