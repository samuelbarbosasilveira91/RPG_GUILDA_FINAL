package br.rpgguilda.rpg_guilda.audit.repository;

import br.rpgguilda.rpg_guilda.audit.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyJpaRepository extends JpaRepository<ApiKey, Long> {
}
