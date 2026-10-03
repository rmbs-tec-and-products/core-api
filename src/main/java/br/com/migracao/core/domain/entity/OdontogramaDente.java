package br.com.migracao.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "odontograma_dente")
public class OdontogramaDente {

    @EmbeddedId
    private OdontogramaDenteId id;

    @MapsId("odontogramaCodigo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "odo_codigo", nullable = false)
    private Odontograma odontograma;
}