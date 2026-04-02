package br.rpgguilda.rpg_guilda.audit.repository;

import br.rpgguilda.rpg_guilda.audit.entity.AuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditEntryJpaRepository extends JpaRepository<AuditEntry, Long> {
}
