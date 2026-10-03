package br.com.migracao.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "odontograma")
public class Odontograma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "tipo", nullable = false)
    private Integer tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pac_codigo")
    private Paciente paciente;

    @Column(name = "valor", precision = 19, scale = 4)
    private BigDecimal valor;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @Column(name = "status")
    private Integer status;

    @Column(name = "nome", length = 100)
    private String nome;

    @Column(name = "telefone", length = 15)
    private String telefone;

    @Column(name = "odontopediatria")
    private Boolean odontopediatria;

    @OneToMany(
            mappedBy = "odontograma",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<OdontogramaDente> dentes = new ArrayList<>();

    @OneToMany(
            mappedBy = "odontograma",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<OdontogramaProcedimento> procedimentos = new ArrayList<>();
}