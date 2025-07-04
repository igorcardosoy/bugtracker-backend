package br.com.ifsp.tsi.bugtrackerbackend.repository;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {
    Optional<PasswordResetCode> findByEmailAndCodeAndUsedFalse(String email, String code);
    void deleteByEmailAndUsedTrue(String email);
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
