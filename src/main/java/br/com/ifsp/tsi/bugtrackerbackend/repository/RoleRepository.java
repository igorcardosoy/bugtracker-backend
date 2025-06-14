package br.com.ifsp.tsi.bugtrackerbackend.repository;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Role;
import br.com.ifsp.tsi.bugtrackerbackend.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(UserRole name);
}
