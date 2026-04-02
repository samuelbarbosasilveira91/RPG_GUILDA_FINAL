package br.rpgguilda.rpg_guilda.operacoes.repository;

import br.rpgguilda.rpg_guilda.operacoes.entity.CompanheiroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanheiroJpaRepository extends JpaRepository<CompanheiroEntity, Long> {
}
