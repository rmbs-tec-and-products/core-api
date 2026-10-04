package br.com.migracao.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "prontuario_arquivo",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_prontuario_arquivo_storage_key",
                        columnNames = "storage_key"
                )
        }
)
public class ProntuarioArquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Integer codigo;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "paciente_codigo",
            nullable = false
    )
    private Paciente paciente;

    @Column(
            name = "titulo",
            nullable = false,
            length = 150
    )
    private String titulo;

    @Column(
            name = "descricao",
            columnDefinition = "TEXT"
    )
    private String descricao;

    @Column(
            name = "nome_original",
            nullable = false,
            length = 255
    )
    private String nomeOriginal;

    @Column(
            name = "content_type",
            nullable = false,
            length = 100
    )
    private String contentType;

    @Column(
            name = "tamanho",
            nullable = false
    )
    private Long tamanho;

    @Column(
            name = "storage_key",
            nullable = false,
            length = 500
    )
    private String storageKey;

    @Column(
            name = "criado_em",
            nullable = false
    )
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {

        if (criadoEm == null) {

            criadoEm =
                    LocalDateTime.now()
                            .withNano(0);
        }
    }
}