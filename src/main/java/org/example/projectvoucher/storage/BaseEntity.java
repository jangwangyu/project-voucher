package org.example.projectvoucher.storage;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createAt;

  @LastModifiedDate
  private LocalDateTime updateAt;

  public Long id() {
    return id;
  }

  public LocalDateTime createAt() {
    return createAt;
  }

  public LocalDateTime updateAt() {
    return updateAt;
  }

}
