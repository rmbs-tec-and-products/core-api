package br.com.migracao.core.domain.entity;

import br.com.migracao.core.domain.enums.PerfilUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_usuario_login",
                        columnNames = "login"
                ),
                @UniqueConstraint(
                        name = "uk_usuario_email",
                        columnNames = "email"
                )
        }
)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Integer codigo;

    @Column(
            name = "nome",
            nullable = false,
            length = 100
    )
    private String nome;

    @Column(
            name = "login",
            nullable = false,
            length = 80
    )
    private String login;

    @Column(
            name = "email",
            nullable = false,
            length = 150
    )
    private String email;

    @Column(
            name = "senha_hash",
            nullable = false,
            length = 100
    )
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "perfil",
            nullable = false,
            length = 20
    )
    private PerfilUsuario perfil;

    @Column(
            name = "ativo",
            nullable = false
    )
    private boolean ativo = true;

    @Column(
            name = "criado_em",
            nullable = false
    )
    private LocalDateTime criadoEm;

    @Column(
            name = "atualizado_em",
            nullable = false
    )
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        LocalDateTime agora =
                LocalDateTime.now();

        criadoEm =
                agora;

        atualizadoEm =
                agora;
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm =
                LocalDateTime.now();
    }
}