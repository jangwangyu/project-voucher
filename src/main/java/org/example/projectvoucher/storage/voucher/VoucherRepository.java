package org.example.projectvoucher.storage.voucher;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Long> {

  Optional<VoucherEntity> findByCode(String code);
}
