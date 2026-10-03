package br.com.migracao.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "caixa")
public class Caixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "dtabertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "dtfechamento")
    private LocalDateTime dataFechamento;

    @OneToMany(
            mappedBy = "caixa",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<CaixaHistorico> historicos = new ArrayList<>();
}