package br.rpgguilda.rpg_guilda.audit.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_roles", schema = "audit")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "fk_ur_user"))
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_ur_role"))
    private Role role;

    @Column(name = "granted_at", nullable = false, updatable = false)
    private OffsetDateTime grantedAt;

    public UserRole() {}

    public UserRoleId getId() { return id; }
    public void setId(UserRoleId id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public OffsetDateTime getGrantedAt() { return grantedAt; }
    public void setGrantedAt(OffsetDateTime grantedAt) { this.grantedAt = grantedAt; }
}
