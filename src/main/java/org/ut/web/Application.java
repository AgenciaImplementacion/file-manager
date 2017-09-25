package org.ut.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.ut.entity.User;
import org.ut.repository.UserRepository;

import javax.sql.DataSource;


@SpringBootApplication
@ComponentScan({"org.ut.web"})
@EnableJpaRepositories(basePackages = "org.ut.repository")
@EntityScan(basePackages = {"org.ut.entity"})
@EnableAutoConfiguration
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
        dataSourceBuilder.url("jdbc:sqlite:/tmp/test.db");
        return dataSourceBuilder.build();
    }

    @Bean
    public CommandLineRunner demo(UserRepository repository) {
        return (args) -> {
            // save a couple of customers
            repository.save(new User("Felipe Cano", "felipecanol@gmail.com", "123"));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (User u : repository.findAll()) {
                log.info(u.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            User u = repository.findOne(1L);
            log.info("Customer found with findOne(1L):");
            log.info("--------------------------------");
            log.info(u.toString());
            log.info("");

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            for (User f : repository.findByName("Felipe Cano")) {
                log.info(f.toString());
            }
            log.info("");
        };
    }
}
