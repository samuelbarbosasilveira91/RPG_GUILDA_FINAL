package br.rpgguilda.rpg_guilda.audit.repository;

import br.rpgguilda.rpg_guilda.audit.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Long> {
    List<Role> findByOrganizacaoId(Long organizacaoId);
}
