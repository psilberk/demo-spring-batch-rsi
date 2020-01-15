package com.oracle.rsi.demospringbatch;

import oracle.rsi.ReactiveStreamsIngestion;
import oracle.rsi.internal.RSIBuilder;

/**
 * Ad-hoc RSIItemWriter builder.
 * 
 * @author psilberk
 */
public class RSIItemWriterBuilder<T> {

  private RSIBuilder rsiBuilder = new RSIBuilder();
  
  public RSIItemWriter<T> build() {
    ReactiveStreamsIngestion rsi = rsiBuilder.build();
    return new RSIItemWriter<T>(rsi);
  }
 
  public RSIItemWriterBuilder<T> url(String url) {
    rsiBuilder.url(url);
    return this;
  }

  public RSIItemWriterBuilder<T> username(String username) {
    rsiBuilder.username(username);
    return this;
  }

  public RSIItemWriterBuilder<T> schema(String schema) {
    rsiBuilder.schema(schema);
    return this;
  }

  public RSIItemWriterBuilder<T> password(String password) {
    rsiBuilder.password(password);
    return this;
  }

  public RSIItemWriterBuilder<T> entity(Class<T> entityClass) {
    rsiBuilder.entity(entityClass);
    return this;
  }
}