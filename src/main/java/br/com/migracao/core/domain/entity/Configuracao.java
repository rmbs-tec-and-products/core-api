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
@Table(name = "configuracao")
public class Configuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "caminho", nullable = false, length = 100)
    private String caminho;

    @Column(name = "tempo")
    private Integer tempo;

    @Column(name = "data")
    private LocalDateTime data;
}