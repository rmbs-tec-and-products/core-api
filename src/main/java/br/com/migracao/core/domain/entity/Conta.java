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
@Table(name = "conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "tipo", nullable = false)
    private Integer tipo;

    @Column(name = "descricao", nullable = false, length = 100)
    private String descricao;

    @Column(name = "formapagamento", nullable = false, length = 100)
    private String formaPagamento;

    @Column(name = "valor", nullable = false, precision = 19, scale = 4)
    private BigDecimal valor;

    @Column(name = "dtpagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "dtvencimento", nullable = false)
    private LocalDateTime dataVencimento;

    @Column(name = "observacao", nullable = false, columnDefinition = "TEXT")
    private String observacao;
}