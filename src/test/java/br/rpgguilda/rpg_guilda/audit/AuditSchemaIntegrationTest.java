package br.rpgguilda.rpg_guilda.audit;

import br.rpgguilda.rpg_guilda.audit.entity.*;
import br.rpgguilda.rpg_guilda.audit.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Testes de Integração - Schema Audit")
public class AuditSchemaIntegrationTest {

    @Autowired private OrganizacaoJpaRepository organizacaoRepository;
    @Autowired private UsuarioJpaRepository usuarioRepository;
    @Autowired private RoleJpaRepository roleRepository;
    @Autowired private PermissionJpaRepository permissionRepository;
    @Autowired private ApiKeyJpaRepository apiKeyRepository;
    @Autowired private AuditEntryJpaRepository auditEntryRepository;

    @Test
    @DisplayName("Deve carregar todas as organizações existentes")
    void deveCarregarOrganizacoes() {
        List<Organizacao> orgs = organizacaoRepository.findAll();
        assertEquals(2, orgs.size());
        List<String> nomes = orgs.stream().map(Organizacao::getNome).collect(Collectors.toList());
        assertTrue(nomes.contains("Guilda do Norte"));
        assertTrue(nomes.contains("Guilda do Sul"));
    }

    @Test
    @DisplayName("Deve carregar usuário com organização associada")
    void deveCarregarUsuarioComOrganizacao() {
        Optional<Usuario> opt = usuarioRepository.findById(1L);
        assertTrue(opt.isPresent());
        assertEquals("Guilda do Norte", opt.get().getOrganizacao().getNome());
        assertEquals("ATIVO", opt.get().getStatus());
    }

    @Test
    @DisplayName("Deve carregar roles do usuário")
    void deveCarregarRolesDoUsuario() {
        Usuario usuario = usuarioRepository.findById(1L).orElseThrow();
        Set<String> nomeRoles = usuario.getRoles().stream().map(Role::getNome).collect(Collectors.toSet());
        assertTrue(nomeRoles.contains("ADMIN"));
    }

    @Test
    @DisplayName("Deve carregar permissions via roles")
    void deveCarregarPermissionsViaRoles() {
        Role role = roleRepository.findById(1L).orElseThrow();
        Set<String> codes = role.getPermissions().stream().map(Permission::getCode).collect(Collectors.toSet());
        assertFalse(codes.isEmpty());
        assertTrue(codes.contains("USUARIO_READ"));
    }

    @Test
    @DisplayName("Deve persistir novo usuário associado a organização existente")
    void devePersistirNovoUsuario() {
        Organizacao org = organizacaoRepository.findById(1L).orElseThrow();
        Usuario novo = new Usuario();
        novo.setOrganizacao(org);
        novo.setNome("Novo Aventureiro");
        novo.setEmail("novo@guildadonorte.com");
        novo.setSenhaHash("$2a$10$hash");
        novo.setStatus("ATIVO");
        novo.setCreatedAt(OffsetDateTime.now());
        novo.setUpdatedAt(OffsetDateTime.now());

        Usuario salvo = usuarioRepository.saveAndFlush(novo);
        assertNotNull(salvo.getId());
        assertTrue(salvo.getId() > 0);
    }

    @Test
    @DisplayName("Deve carregar todas as permissions")
    void deveCarregarPermissions() {
        assertEquals(6, permissionRepository.findAll().size());
    }

    @Test
    @DisplayName("Deve carregar API Keys")
    void deveCarregarApiKeys() {
        assertEquals(2, apiKeyRepository.findAll().size());
    }

    @Test
    @DisplayName("Deve carregar audit entries")
    void deveCarregarAuditEntries() {
        assertEquals(2, auditEntryRepository.findAll().size());
    }

    @Test
    @DisplayName("Relacionamento completo: Org → Usuário → Roles → Permissions")
    void deveNavegarRelacionamentoCompleto() {
        Organizacao org = organizacaoRepository.findById(1L).orElseThrow();
        List<Usuario> usuarios = usuarioRepository.findByOrganizacaoId(org.getId());
        assertFalse(usuarios.isEmpty());
        for (Usuario u : usuarios) {
            for (Role r : u.getRoles()) {
                assertEquals(org.getId(), r.getOrganizacao().getId());
                assertNotNull(r.getPermissions());
            }
        }
    }
}
