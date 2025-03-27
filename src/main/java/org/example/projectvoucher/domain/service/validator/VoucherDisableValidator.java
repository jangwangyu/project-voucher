package org.example.projectvoucher.domain.service.validator;

import org.example.projectvoucher.common.dto.RequestContext;
import org.example.projectvoucher.storage.voucher.VoucherEntity;
import org.springframework.stereotype.Component;

@Component
public class VoucherDisableValidator {

  public static void validate(VoucherEntity voucherEntity, RequestContext requestContext) {
    isValidAuth(voucherEntity, requestContext);
  }

  private static void isValidAuth(VoucherEntity voucherEntity, RequestContext requestContext) {
    // 상품권 사용 불가 처리 권한이 있는지 확인
    if(voucherEntity.publishHistory().requesterType() != requestContext.requesterType()
        || voucherEntity.publishHistory().requestId().equals(requestContext.requesterId()) ){
      throw new IllegalArgumentException("사용 불가 처리 권한이 없는 상품권 입니다.");
    }
  }

}
