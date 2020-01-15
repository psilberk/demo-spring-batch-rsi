package com.oracle.rsi.demospringbatch;

import oracle.rsi.StreamEntity;
import oracle.rsi.StreamField;

/**
 * Mapping class, both for the ItemReader and the ItemWriter.
 * For RSI, it maps to the annotated table in tableName and 
 * to the columns using the attribute names.
 * Create a table in the target database using the below ddl.
 * CREATE TABLE CUSTOMERS (
 *   ID NUMBER(20,0), 
 *   NAME VARCHAR2(90 BYTE), 
 *   REGION VARCHAR2(10 BYTE))
 * 
 * @author psilberk
 *
 */
@StreamEntity(tableName = "customers")
public class Customer {

  @StreamField
  private long id;

  @StreamField
  private String name;

  @StreamField
  private String region;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public Customer() {
  }

  public Customer(long id, String name, String region) {
    super();
    this.id = id;
    this.name = name;
    this.region = region;
  }

  @Override
  public String toString() {
    return "Customer [id=" + id + ", name=" + name + ", region=" + region
        + "]";
  }

}