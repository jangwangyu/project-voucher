package org.example.projectvoucher.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.voucher.VoucherEntity;
import org.example.projectvoucher.storage.voucher.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VoucherServiceTest {
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
    final Long amount = 10000L;

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
}