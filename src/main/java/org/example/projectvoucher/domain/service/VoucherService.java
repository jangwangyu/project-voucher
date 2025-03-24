package org.example.projectvoucher.domain.service;

import java.time.LocalDate;
import java.util.UUID;
import org.example.projectvoucher.common.type.VoucherAmountType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.voucher.VoucherEntity;
import org.example.projectvoucher.storage.voucher.VoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoucherService {
  private final VoucherRepository voucherRepository;

  public VoucherService(VoucherRepository voucherRepository) {
    this.voucherRepository = voucherRepository;
  }

  // 상품권 발행
  @Transactional
  public String publish(final LocalDate validFrom, final LocalDate validTo, final VoucherAmountType amountType) {
    final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

    VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH,validFrom, validTo, amountType);

    return voucherRepository.save(voucherEntity).code();
  }

  // 상품권 사용 불가 처리
  @Transactional
  public void disable(String code) {
    final VoucherEntity voucherEntity = voucherRepository.findByCode(code)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품권입니다."));

    voucherEntity.disable();
  }

  // 상품권 사용
  @Transactional
  public void use(String code) {
    final VoucherEntity voucherEntity = voucherRepository.findByCode(code)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품권입니다."));

    voucherEntity.use();
  }

  // 상품권 사용
}
