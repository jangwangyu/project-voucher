package org.example.projectvoucher.storage.voucher;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import org.example.projectvoucher.common.type.RequesterType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.BaseEntity;

@Table(name = "voucher_history")
@Entity
public class VoucherHistoryEntity extends BaseEntity {
  private String orderid;
  @Enumerated(EnumType.STRING)
  private RequesterType requesterType;
  private String requestId;
  @Enumerated(EnumType.STRING)
  private VoucherStatusType status;
  private String description;



  public VoucherHistoryEntity() {}

  public VoucherHistoryEntity(String orderid, RequesterType requesterType, String requestId,VoucherStatusType status, String description) {
    this.orderid = orderid;
    this.requesterType = requesterType;
    this.requestId = requestId;
    this.status = status;
    this.description = description;
  }

  public String orderid() {
    return orderid;
  }

  public RequesterType requesterType() {
    return requesterType;
  }

  public String requestId() {
    return requestId;
  }

  public VoucherStatusType status() {
    return status;
  }

  public String description() {
    return description;
  }
}
