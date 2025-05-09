package org.example.projectvoucher.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.example.projectvoucher.common.dto.RequestContext;
import org.example.projectvoucher.common.type.RequesterType;
import org.example.projectvoucher.common.type.VoucherAmountType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.voucher.VoucherEntity;
import org.example.projectvoucher.storage.voucher.VoucherRepository;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VoucherDynamicServiceV2Test {
  @Autowired
  private VoucherService voucherService;

  @Autowired
  private VoucherRepository voucherRepository;

  @TestFactory
  Stream<DynamicTest> test(){
    final List<String> codes = new ArrayList<>();
    return Stream.of(
        dynamicTest("[0] 상품권 발행합니다.", () -> {
          // Given
          final LocalDate validFrom = LocalDate.now();
          final LocalDate validTo = LocalDate.now().plusDays(30);
          final VoucherAmountType amount = VoucherAmountType.KRW_30000;
          final RequestContext requestContext = new RequestContext(RequesterType.USER, UUID.randomUUID().toString());

          // When
          final String code = voucherService.publishV2(requestContext,validFrom, validTo, amount);
          codes.add(code);
          // Then
          final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
          assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);
        }),
        dynamicTest("[0] 상품권 사용 불가 처리 합니다.", () -> {
          // Given
          final String code = codes.get(0);
          final RequestContext requestContext = new RequestContext(RequesterType.USER, UUID.randomUUID().toString());
          // When
          voucherService.disableV2(requestContext, code);
          // Then
          final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
          assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.DISABLE);
        }),
        dynamicTest("[0] 상품권 사용 불가 상태의 상품권은 사용할 수 없습니다.", () -> {
          // Given
          final String code = codes.get(0);
          final RequestContext requestContext = new RequestContext(RequesterType.USER, UUID.randomUUID().toString());
          // When
          assertThatThrownBy(() -> voucherService.useV2(requestContext,code))
              .isInstanceOf(IllegalStateException.class)
                  .hasMessage("사용할 수 없는 상태의 상품권입니다.");
          // Then
          final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
          assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.DISABLE);
        }),

        dynamicTest("[1] 상품권을 사용합니다.", () -> {
          // Given
          final RequestContext requestContext = new RequestContext(RequesterType.USER, UUID.randomUUID().toString());
          final LocalDate validFrom = LocalDate.now();
          final LocalDate validTo = LocalDate.now().plusDays(30);
          final VoucherAmountType amount = VoucherAmountType.KRW_30000;

          final String code = voucherService.publishV2(requestContext,validFrom, validTo, amount);
          codes.add(code);

          // When
          voucherService.useV2(requestContext,code);

          // Then
          final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
          assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.USE);
        }),
        dynamicTest("[1] 사용한 상품권은 사용 불가 처리 할 수 없습니다.", () -> {
          // Given
          final RequestContext requestContext = new RequestContext(RequesterType.USER, UUID.randomUUID().toString());
          final String code = codes.get(1);
          // When
          assertThatThrownBy(() -> voucherService.disableV2(requestContext,code))
              .isInstanceOf(IllegalStateException.class)
              .hasMessage("사용 불가 처리할 수 없는 상태의 상품권입니다.");
          // Then
          final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
          assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.USE);
        }),
        dynamicTest("[1] 사용한 상품권은 또 사용할 수 없습니다.", () -> {
          // Given
          final String code = codes.get(1);
          final RequestContext requestContext = new RequestContext(RequesterType.USER, UUID.randomUUID().toString());
          // When
          assertThatThrownBy(() -> voucherService.useV2(requestContext,code))
              .isInstanceOf(IllegalStateException.class)
              .hasMessage("사용할 수 없는 상태의 상품권입니다.");

          // Then
          final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
          assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.USE);
        })
    );
  }
}