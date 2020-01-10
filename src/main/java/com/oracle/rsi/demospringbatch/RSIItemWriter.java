package com.oracle.rsi.demospringbatch;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Logger;

import org.springframework.batch.item.ItemWriter;

import oracle.rsi.ReactiveStreamsIngestion;

public class RSIItemWriter<T> implements ItemWriter<T> {

  private ReactiveStreamsIngestion rsi = null;
  private SubmissionPublisher<T> publisher = null;
  private Logger logger = Logger.getLogger(RSIItemWriter.class.getName());

  public RSIItemWriter(ReactiveStreamsIngestion rsi) {
    this.rsi = rsi;
    publisher = new SubmissionPublisher<>();
    publisher.subscribe(rsi.subscriber());
    logger.info("RSI Item Writer Started!");
  }

  @Override
  public void write(List<? extends T> items) throws Exception {
    items.forEach(publisher::submit);
  }
  
  public void close() {
    rsi.close();
  }

}