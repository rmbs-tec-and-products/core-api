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
@Table(name = "odontograma_procedimento")
public class OdontogramaProcedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Integer codigo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "odo_codigo", nullable = false)
    private Odontograma odontograma;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pro_codigo", nullable = false)
    private Procedimento procedimento;

    @Column(name = "dente", nullable = false)
    private Integer dente;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "valor", nullable = false, precision = 19, scale = 4)
    private BigDecimal valor;

    @Column(name = "face", length = 200)
    private String face;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "data")
    private LocalDateTime data;
}