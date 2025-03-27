package org.example.projectvoucher.storage.voucher;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {

  Optional<ContractEntity> findByCode(String code);
}
