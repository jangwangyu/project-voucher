package org.example.projectvoucher.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.example.projectvoucher.common.type.VoucherAmountType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.voucher.VoucherEntity;
import org.example.projectvoucher.storage.voucher.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VoucherDynamicServiceTest {
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

          // When
          final String code = voucherService.publish(validFrom, validTo, amount);
          codes.add(code);
          // Then
          final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
          assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);
        }),
        dynamicTest("[0] 상품권 사용 불가 처리 합니다.", () -> {
          // Given
          final String code = codes.get(0);
          // When
          voucherService.disable(code);
          // Then
          final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
          assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.DISABLE);
        }),
        dynamicTest("[0] 상품권 사용 불가 상태의 상품권은 사용할 수 없습니다.", () -> {
          // Given
          final String code = codes.get(0);
          // When
          assertThatThrownBy(() -> voucherService.use(code))
              .isInstanceOf(IllegalStateException.class)
                  .hasMessage("사용할 수 없는 상태의 상품권입니다.");
          // Then
          final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
          assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.DISABLE);
        }),

        dynamicTest("[1] 상품권을 사용합니다.", () -> {
          // Given
          final LocalDate validFrom = LocalDate.now();
          final LocalDate validTo = LocalDate.now().plusDays(30);
          final VoucherAmountType amount = VoucherAmountType.KRW_30000;

          final String code = voucherService.publish(validFrom, validTo, amount);
          codes.add(code);

          // When
          voucherService.use(code);

          // Then
          final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
          assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.USE);
        }),
        dynamicTest("[1] 사용한 상품권은 사용 불가 처리 할 수 없습니다.", () -> {
          // Given
          final String code = codes.get(1);
          // When
          assertThatThrownBy(() -> voucherService.disable(code))
              .isInstanceOf(IllegalStateException.class)
              .hasMessage("사용 불가 처리할 수 없는 상태의 상품권입니다.");
          // Then
          final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
          assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.USE);
        }),
        dynamicTest("[1] 사용한 상품권은 또 사용할 수 없습니다.", () -> {
          // Given
          final String code = codes.get(1);
          // When
          assertThatThrownBy(() -> voucherService.use(code))
              .isInstanceOf(IllegalStateException.class)
              .hasMessage("사용할 수 없는 상태의 상품권입니다.");

          // Then
          final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
          assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.USE);
        })
    );
  }
}