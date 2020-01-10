package com.oracle.rsi.demospringbatch;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DemoSpringBatchApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoSpringBatchApplication.class, args);
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource datasource) {
    return new JdbcTemplate(datasource);
  }
}