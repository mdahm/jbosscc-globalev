package de.akquinet.jbosscc.globalev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  public static final String VERSION_COLUMN = "VERSION";
  public static final String ID_COLUMN = "ID";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = ID_COLUMN)
  private Long id;

  @Version
  @Column(name = VERSION_COLUMN)
  private Long version;

  public AbstractEntity() {
    // NOP
  }

  public Long getId() {
    return id;
  }

  public Long getVersion() {
    return version;
  }

  /**
   * Never override these methods when using JPA/Hibernate
   */
  @Override
  public final boolean equals(final Object obj) {
    return super.equals(obj);
  }

  /**
   * Never override these methods when using JPA/Hibernate
   */
  @Override
  public final int hashCode() {
    return super.hashCode();
  }
}
