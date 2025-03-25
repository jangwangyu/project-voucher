package org.example.projectvoucher.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.example.projectvoucher.common.type.VoucherAmountType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.voucher.VoucherEntity;
import org.example.projectvoucher.storage.voucher.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VoucherServiceV2Test {
  @Autowired
  private VoucherService voucherService;

  @Autowired
  private VoucherRepository voucherRepository;

  @DisplayName("발행된 상품권은 code로 조회할 수 있어야 한다.")
  @Test
  public void test1() {
    // Given
    final LocalDate validFrom = LocalDate.now();
    final LocalDate validTo = LocalDate.now().plusDays(30);
    final VoucherAmountType amount = VoucherAmountType.KRW_30000;

    final String code = voucherService.publish(validFrom, validTo, amount);
    // When
    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

    // Then
    assertThat(voucherEntity.code()).isEqualTo(code);
    assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);
    assertThat(voucherEntity.validFrom()).isEqualTo(validFrom);
    assertThat(voucherEntity.validTo()).isEqualTo(validTo);
    assertThat(voucherEntity.amount()).isEqualTo(amount);
  }

  @DisplayName("발행된 상품권은 사용 불가처리 할 수 있다.")
  @Test
  public void test2() {
    // Given
    final LocalDate validFrom = LocalDate.now();
    final LocalDate validTo = LocalDate.now().plusDays(30);
    final VoucherAmountType amount = VoucherAmountType.KRW_30000;

    final String code = voucherService.publish(validFrom, validTo, amount);
    // When
    voucherService.disable(code);
    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
    // Then
    assertThat(voucherEntity.code()).isEqualTo(code);
    assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.DISABLE);
    assertThat(voucherEntity.validFrom()).isEqualTo(validFrom);
    assertThat(voucherEntity.validTo()).isEqualTo(validTo);
    assertThat(voucherEntity.amount()).isEqualTo(amount);
    assertThat(voucherEntity.updateAt()).isNotEqualTo(voucherEntity.createAt());

    System.out.println(voucherEntity.createAt());
    System.out.println("업데이트: " + voucherEntity.updateAt());
  }

  @DisplayName("발행된 상품권은 사용할 수 있다.")
  @Test
  public void test3() {
    // Given
    final LocalDate validFrom = LocalDate.now();
    final LocalDate validTo = LocalDate.now().plusDays(30);
    final VoucherAmountType amount = VoucherAmountType.KRW_30000;

    final String code = voucherService.publish(validFrom, validTo, amount);
    // When
    voucherService.use(code);
    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

    // Then
    assertThat(voucherEntity.code()).isEqualTo(code);
    assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.USE);
    assertThat(voucherEntity.validFrom()).isEqualTo(validFrom);
    assertThat(voucherEntity.validTo()).isEqualTo(validTo);
    assertThat(voucherEntity.amount()).isEqualTo(amount);
    assertThat(voucherEntity.updateAt()).isNotEqualTo(voucherEntity.createAt());

    System.out.println(voucherEntity.createAt());
    System.out.println("업데이트: " + voucherEntity.updateAt());
  }
}