package de.akquinet.jbosscc.globalev.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "MESSAGE")
public class Message extends AbstractEntity {
  private static final long serialVersionUID = 1L;
  public static final int MAX_TEXT = 1000;

  @Column(name = "TEXT_COL", nullable = false)
  @NotNull
  @Size(min = 1, max = MAX_TEXT)
  private String text;

  @Column(name = "IDENTIFIER_COL", nullable = false)
  @NotNull
  @Size(min = 1, max = 200)
  private String identifier;

  @Column(name = "FROM_COL", nullable = false)
  @NotNull
  @Temporal(TemporalType.TIMESTAMP)
  private Date from;

  public String getText() {
    return text;
  }

  public void setText(final String text) {
    this.text = text;
  }

  public Date getFrom() {
    return from;
  }

  public void setFrom(final Date from) {
    this.from = from;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(final String identifier) {
    this.identifier = identifier;
  }
}