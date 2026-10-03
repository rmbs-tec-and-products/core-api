package br.com.migracao.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class OdontogramaDenteId implements Serializable {

    @Column(name = "odo_codigo")
    private Integer odontogramaCodigo;

    @Column(name = "dente")
    private Integer dente;
}