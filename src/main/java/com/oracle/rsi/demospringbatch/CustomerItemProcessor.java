package com.oracle.rsi.demospringbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Simple Processor for educational purposes.
 * It implements the Transform Phase of the ETL.
 * It creates a new Customer upper-casing the String attributes.
 * 
 * @author psilberk
 */
public class CustomerItemProcessor
    implements ItemProcessor<Customer, Customer> {

  private static final Logger log = LoggerFactory
      .getLogger(CustomerItemProcessor.class);

  @Override
  public Customer process(Customer original) throws Exception {

    Customer transformed = new Customer(original.getId(),
        original.getName().toUpperCase(),
        original.getRegion().toUpperCase());

    log.info("Converting " + original + " into " + transformed);

    return transformed;
  }
}