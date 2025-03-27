package org.example.projectvoucher.domain.service.validator;

import org.example.projectvoucher.storage.voucher.ContractEntity;
import org.springframework.stereotype.Component;

@Component
public class VoucherPublishValidator {

  public static void validate(ContractEntity contractEntity) {
    if (contractEntity.isExpired()) {
      throw new IllegalStateException("유효기간이 지난 계약입니다.");
    }
  }
}
