package br.rpgguilda.rpg_guilda.operacoes.repository;

import br.rpgguilda.rpg_guilda.operacoes.entity.AventureiroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface AventureiroJpaRepository extends JpaRepository<AventureiroEntity, Long> {
    List<AventureiroEntity> findByOrganizacaoId(Long organizacaoId);

    @Query("SELECT a FROM AventureiroEntity a WHERE " +
           "(:ativo IS NULL OR a.ativo = :ativo) AND " +
           "(:classe IS NULL OR a.classe = :classe) AND " +
           "(:nivelMinimo IS NULL OR a.nivel >= :nivelMinimo)")
    org.springframework.data.domain.Page<AventureiroEntity> findComFiltros(
            @org.springframework.data.repository.query.Param("ativo") Boolean ativo,
            @org.springframework.data.repository.query.Param("classe") br.rpgguilda.rpg_guilda.operacoes.enums.ClasseAventureiro classe,
            @org.springframework.data.repository.query.Param("nivelMinimo") Integer nivelMinimo,
            org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<AventureiroEntity> findByNomeContainingIgnoreCase(String nome, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT a FROM AventureiroEntity a LEFT JOIN FETCH a.companheiro WHERE a.id = :id")
    java.util.Optional<AventureiroEntity> findByIdWithCompanheiro(@org.springframework.data.repository.query.Param("id") Long id);

}
