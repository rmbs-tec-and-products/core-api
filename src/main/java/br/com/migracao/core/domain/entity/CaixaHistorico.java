package br.com.migracao.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "caixa_historico")
public class CaixaHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "tipo", nullable = false, length = 100)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cai_codigo", nullable = false)
    private Caixa caixa;

    @Column(name = "observacao", nullable = false, columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "descricao", length = 100)
    private String descricao;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @Column(name = "valor", precision = 19, scale = 4)
    private BigDecimal valor;
}