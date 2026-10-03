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
@Table(name = "agenda")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pac_codigo", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "den_codigo")
    private Dentista dentista;
}