package org.example.projectvoucher.storage.voucher;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import org.example.projectvoucher.storage.BaseEntity;

@Table(name = "contract")
@Entity
public class ContractEntity extends BaseEntity {
  private String code; // 계약의 고유 코드
  private LocalDate validFrom; // 계약의 유효 기간 시작일
  private LocalDate validTo; // 계약의 유효 기간 종료일
  private Integer voucherValidPeriodDayCount; // 상품권 유효기간 일자

  // 2/22일에 상품권을 발행할 수 있는가?
  // voucherValidPeriodDayCount = 180일 (180일 동안 발행 가능)
  // 2/22일에 발행한 상품권은 언제까지 쓸 수 있는가?

  public ContractEntity() {}

  public ContractEntity(String code,LocalDate validFrom, LocalDate validTo, Integer voucherValidPeriodDayCount) {
    this.code = code;
    this.validFrom = validFrom;
    this.validTo = validTo;
    this.voucherValidPeriodDayCount = voucherValidPeriodDayCount;
  }

  public Boolean isExpired() {
    return LocalDate.now().isAfter(validTo);
  }

  public String code() {
    return code;
  }

  public LocalDate validFrom() {
    return validFrom;
  }

  public LocalDate validTo() {
    return validTo;
  }

  public Integer voucherValidPeriodDayCount() {
    return voucherValidPeriodDayCount;
  }
}
