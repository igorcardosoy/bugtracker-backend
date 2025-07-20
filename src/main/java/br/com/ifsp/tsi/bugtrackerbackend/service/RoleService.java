package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.role.RoleResponseDTO;
import br.com.ifsp.tsi.bugtrackerbackend.exception.RoleNotFoundException;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Role;
import br.com.ifsp.tsi.bugtrackerbackend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(RoleResponseDTO::fromRole)
                .collect(Collectors.toList());
    }

    public RoleResponseDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id, HttpStatus.NOT_FOUND));
        return RoleResponseDTO.fromRole(role);
    }
}
