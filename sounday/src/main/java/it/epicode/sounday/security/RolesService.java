package it.epicode.sounday.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolesService {

    private final RolesRepository rolesRepository;

    public Roles create(Roles roles){
        return rolesRepository.save(roles);
    }

    public boolean existsByRoleType(String roleType) {
        return rolesRepository.existsById(roleType);
    }
}
