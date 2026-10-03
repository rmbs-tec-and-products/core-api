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
public class ProcedimentoProdutoId implements Serializable {

    @Column(name = "pro_codigo")
    private Integer procedimentoCodigo;

    @Column(name = "pro_codigopro")
    private Integer produtoCodigo;
}