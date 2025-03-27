package org.example.projectvoucher.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.UUID;
import org.example.projectvoucher.common.dto.RequestContext;
import org.example.projectvoucher.common.type.RequesterType;
import org.example.projectvoucher.common.type.VoucherAmountType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.voucher.ContractEntity;
import org.example.projectvoucher.storage.voucher.ContractRepository;
import org.example.projectvoucher.storage.voucher.VoucherEntity;
import org.example.projectvoucher.storage.voucher.VoucherHistoryEntity;
import org.example.projectvoucher.storage.voucher.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VoucherServiceV3Test {
  @Autowired
  private VoucherService voucherService;

  @Autowired
  private VoucherRepository voucherRepository;

  @Autowired
  private ContractRepository contractRepository;

  @DisplayName("발행된 상품권은 계약 정보의 voucherValidPeriodDayCount 만큼 유효기간을 가져야 된다")
  @Test
  public void test0() {
    // Given
    final RequestContext requestContext = new RequestContext(RequesterType.USER, UUID.randomUUID().toString());
    final VoucherAmountType amount = VoucherAmountType.KRW_30000;

    final String contractCode = "CT0001";

    // When
    final String code = voucherService.publishV3(requestContext , contractCode, amount);
    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

    // Then
    final ContractEntity contractEntity = contractRepository.findByCode(contractCode).get();
    assertThat(voucherEntity.code()).isEqualTo(code);
    assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);
    assertThat(voucherEntity.validFrom()).isEqualTo(LocalDate.now());
    assertThat(voucherEntity.validTo()).isEqualTo(LocalDate.now().plusDays(contractEntity.voucherValidPeriodDayCount()));
    System.out.println("### voucher validTo= " + voucherEntity.validTo());
    assertThat(voucherEntity.amount()).isEqualTo(amount);

    // history
    final VoucherHistoryEntity voucherHistoryEntity = voucherEntity.histories().get(0);
    assertThat(voucherHistoryEntity.orderid()).isNotNull();
    assertThat(voucherHistoryEntity.requesterType()).isEqualTo(requestContext.requesterType());
    assertThat(voucherHistoryEntity.requestId()).isEqualTo(requestContext.requesterId());
    assertThat(voucherHistoryEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);
    assertThat(voucherHistoryEntity.description()).isEqualTo("발행");
  }

  @DisplayName("유효기간이 지난 계약으로 상품권 발행을 할 수 없습니다.")
  @Test
  public void test1() {
    // Given
    final RequestContext requestContext = new RequestContext(RequesterType.USER, UUID.randomUUID().toString());
    final VoucherAmountType amount = VoucherAmountType.KRW_30000;

    final String contractCode = "CT0010";

    // When
    assertThatThrownBy(() -> voucherService.publishV3(requestContext , contractCode, amount))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("유효기간이 지난 계약입니다.");

    // Then

  }

  @DisplayName("상품권은 발행 요청자만 사용 불가 처리를 할 수 있다.")
  @Test
  public void test2() {
    // Given
    final RequestContext requestContext = new RequestContext(RequesterType.USER, UUID.randomUUID().toString());
    final VoucherAmountType amount = VoucherAmountType.KRW_30000;

    final String contractCode = "CT0001";
    final String code = voucherService.publishV3(requestContext , contractCode, amount);

    final RequestContext otherRequestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
    // When
    assertThatThrownBy(() -> voucherService.disableV3(otherRequestContext, code))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("사용 불가 처리 권한이 없는 상품권 입니다.");

    // Then
    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

    assertThat(voucherEntity.code()).isEqualTo(code);
    assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);
  }
}