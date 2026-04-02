package br.rpgguilda.rpg_guilda.audit.repository;

import br.rpgguilda.rpg_guilda.audit.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByOrganizacaoId(Long organizacaoId);
}
