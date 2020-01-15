package com.oracle.rsi.demospringbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * This class represents the configuration for the ETL process
 * Defines the beans for:
 * 
 * 1) The ItemReader (Extract Phase) as a csv flat file reader.
 * 2) The ItemProcesor (Transform Phase) as a custom processor.
 * 3) The ItemWriter (Load Phase) as a custom writer (using RSI).
 * 
 * It hooks all the steps into step1, creates the job and adds a callback to
 * be executed upon completion.
 * 
 * @author psilberk
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  /**
   * Extract Phase of the ETL.
   * Reads from the csv file and maps into a Customer Object
   * using FlatFileItemReader (Spring Batch).
   */
  @Bean
  public FlatFileItemReader<Customer> reader() {
    return new FlatFileItemReaderBuilder<Customer>()
        .name("customerItemReader")
        .resource(new FileSystemResource(
            "customers.csv"))
        .delimited()
        .names(new String[] {
            "id", "name", "region"
        })
        .fieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {
          {
            setTargetType(Customer.class);
          }
        })
        .build();
  }

  /**
   * Transform Phase of the ETL.
   * Custom Processor for educational purposes.
   * It maps a Customer into a new Customer with upper-cased values. 
   */
  @Bean
  public CustomerItemProcessor processor() {
    return new CustomerItemProcessor();
  }

  /**
   * Load Phase of the ETL.
   * Custom ItewWriter, using RSI to stream the transformed data
   * into an Oracle Database. 
   */
  @Bean
  public RSIItemWriter<Customer> writer() {
    return new RSIItemWriterBuilder<Customer>()
        .url(
            "jdbc:oracle:thin:@nshe02cn01.us.oracle.com:5521/db2demo.regress.rdbms.dev.us.oracle.com")
        .username("scott")
        .schema("scott")
        .password("tiger")
        .entity(Customer.class)
        .build();
  }

  /**
   * Job Definition, it defines the step and the callback.
   */
  @Bean
  public Job importUserJob(JobCompletionNotificationListener listener,
      Step step1) {
    return jobBuilderFactory
        .get("importCustomerRSIItemWriter")
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .flow(step1)
        .end()
        .build();
  }

  /**
   * Step Definition, it hooks the 3 phases of the ETL.
   */
  @Bean
  public Step step1(RSIItemWriter<Customer> writer) {
    return stepBuilderFactory
        .get("step1")
        .<Customer, Customer>chunk(10)
        .reader(reader())
        .processor(processor())
        .writer(writer)
        .build();
  }

}