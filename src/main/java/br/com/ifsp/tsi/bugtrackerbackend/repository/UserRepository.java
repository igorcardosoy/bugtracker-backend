package br.com.ifsp.tsi.bugtrackerbackend.repository;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRolesName(UserRole name);
}