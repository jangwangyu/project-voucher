package org.example.projectvoucher.storage.voucher;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.BaseEntity;

@Table(name = "voucher")
@Entity
public class VoucherEntity extends BaseEntity {
  private String code;
  private VoucherStatusType status;
  private LocalDate validFrom;
  private LocalDate validTo;
  private Long amount;

  public VoucherEntity() {}

  public VoucherEntity(String code, VoucherStatusType status, LocalDate validFrom, LocalDate validTo, Long amount) {
    this.code = code;
    this.status = status;
    this.validFrom = validFrom;
    this.validTo = validTo;
    this.amount = amount;
  }

  public String code() {
    return code;
  }
  public VoucherStatusType status() {
    return status;
  }
  public LocalDate validFrom() {
    return validFrom;
  }
  public LocalDate validTo() {
    return validTo;
  }
  public Long amount() {
    return amount;
  }


  public void disable() {
    this.status = VoucherStatusType.DISABLE;
  }
}
