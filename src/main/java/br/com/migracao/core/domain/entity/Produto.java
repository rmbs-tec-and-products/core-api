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
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "unidade", nullable = false, length = 100)
    private String unidade;

    @Column(name = "embalagem")
    private Integer embalagem;

    @Column(name = "qtdembalagem")
    private Integer qtdEmbalagem;

    @Column(name = "valor", precision = 19, scale = 4)
    private BigDecimal valor;

    @Column(name = "ultimovalor", precision = 19, scale = 4)
    private BigDecimal ultimoValor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "for_codigo")
    private Fornecedor fornecedor;

    @Column(name = "quantidademin")
    private Integer quantidadeMinima;

    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProcedimentoProduto> procedimentos = new ArrayList<>();
}