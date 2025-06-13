package br.com.ifsp.tsi.bugtrackerbackend.repository;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
