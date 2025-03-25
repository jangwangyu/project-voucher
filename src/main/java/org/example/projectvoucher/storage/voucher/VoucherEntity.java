package org.example.projectvoucher.storage.voucher;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.example.projectvoucher.common.type.VoucherAmountType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.BaseEntity;

@Table(name = "voucher_history")
@Entity
public class VoucherEntity extends BaseEntity {
  private String code;
  @Enumerated(EnumType.STRING)
  private VoucherStatusType status;
  private LocalDate validFrom;
  private LocalDate validTo;

  @Enumerated(EnumType.STRING)
  private VoucherAmountType amount;

  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  @JoinColumn(name = "voucher_id")
  private List<VoucherHistoryEntity> histories = new ArrayList<>();

  public VoucherEntity() {}

  public VoucherEntity(String code, VoucherStatusType status, LocalDate validFrom, LocalDate validTo, VoucherAmountType amount,VoucherHistoryEntity voucherHistory) {
    this.code = code;
    this.status = status;
    this.validFrom = validFrom;
    this.validTo = validTo;
    this.amount = amount;
    this.histories.add(voucherHistory);
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
  public VoucherAmountType amount() {
    return amount;
  }

  public List<VoucherHistoryEntity> histories() {
    return histories;
  }

  public void disable(final VoucherHistoryEntity voucherHistoryEntity) {
    if (!this.status.equals(VoucherStatusType.PUBLISH)) {
      throw new IllegalStateException("사용 불가 처리할 수 없는 상태의 상품권입니다.");
    }
    this.status = VoucherStatusType.DISABLE;
    this.histories.add(voucherHistoryEntity);
  }

  public void use(final VoucherHistoryEntity voucherHistoryEntity) {
    if (!this.status.equals(VoucherStatusType.PUBLISH)) {
      throw new IllegalStateException("사용할 수 없는 상태의 상품권입니다.");
    }
      this.status = VoucherStatusType.USE;
    this.histories.add(voucherHistoryEntity);
  }

}
