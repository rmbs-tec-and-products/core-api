package br.com.migracao.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "procedimento")
public class Procedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "valor", precision = 19, scale = 4)
    private BigDecimal valor;

    @OneToMany(mappedBy = "procedimento", fetch = FetchType.LAZY)
    @Builder.Default
    private List<OdontogramaProcedimento> odontogramaProcedimentos = new ArrayList<>();

    @OneToMany(
            mappedBy = "procedimento",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<ProcedimentoProduto> produtos = new ArrayList<>();
}