package com.oracle.rsi.demospringbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Simple callback to query the target database after job completion.
 * 
 * @author psilberk
 */
@Component
public class JobCompletionNotificationListener
    extends JobExecutionListenerSupport {

  private static final Logger log = LoggerFactory
      .getLogger(JobCompletionNotificationListener.class);

  private final JdbcTemplate jdbcTemplate;
  private final RSIItemWriter<Customer> writer;

  @Autowired
  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate,
      RSIItemWriter<Customer> writer) {
    this.jdbcTemplate = jdbcTemplate;
    this.writer = writer;
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    
    writer.close();
    
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB FINISHED! Time to verify the results");

      jdbcTemplate
          .query("SELECT id, name, region FROM customers",
              (rs, row) -> new Customer(
                  rs.getLong(1),
                  rs.getString(2),
                  rs.getString(3)))
          .forEach(
              customer -> log
                  .info("Found <" + customer + "> in the database."));
    }
  }
}