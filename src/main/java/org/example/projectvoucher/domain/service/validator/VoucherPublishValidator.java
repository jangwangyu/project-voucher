package org.example.projectvoucher.domain.service.validator;

import org.example.projectvoucher.storage.voucher.ContractEntity;
import org.springframework.stereotype.Component;

@Component
public class VoucherPublishValidator {

  public static void validate(ContractEntity contractEntity) {
    상품권_발행을_위한_계약_유효기간이_만료되었는지_확인(contractEntity);
  }

  private static void 상품권_발행을_위한_계약_유효기간이_만료되었는지_확인(ContractEntity contractEntity) {
    // 상품권 발행을 위한 계약 유효기간이 만료되었는지 확인
    if (contractEntity.isExpired()) {
      throw new IllegalStateException("유효기간이 지난 계약입니다.");
    }
  }
}
