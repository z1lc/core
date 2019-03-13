package com.robertsanek.data.etl.remote.google.sheets.budget;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "budget_items")
public class AnnotatedItem {

  @Id
  private Long id;
  private ZonedDateTime date;
  private BigDecimal value;
  @Enumerated(EnumType.STRING)
  @Column(name = "line_item_type")
  private LineItemType lineItemType;
  @Column(length = 1_000)
  private String comment;

  public AnnotatedItem() { }

  public AnnotatedItem(Long id, ZonedDateTime date, BigDecimal value, LineItemType lineItemType, String comment) {
    this.id = id;
    this.date = date;
    this.value = value;
    this.lineItemType = lineItemType;
    this.comment = comment;
  }

  public ZonedDateTime getDate() {
    return date;
  }

  public BigDecimal getValue() {
    return value;
  }

  public LineItemType getLineItemType() {
    return lineItemType;
  }

  public String getComment() {
    return comment;
  }

  @Override
  public String toString() {
    return "com.robertsanek.data.etl.remote.google.sheets.budget.AnnotatedItem{" +
        "date=" + date +
        ", value=" + value +
        ", lineItemType=" + lineItemType +
        ", comment='" + comment + '\'' +
        '}';
  }
}
