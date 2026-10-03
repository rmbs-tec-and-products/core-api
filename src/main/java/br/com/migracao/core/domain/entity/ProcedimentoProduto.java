package br.com.migracao.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "procedimento_produto")
public class ProcedimentoProduto {

    @EmbeddedId
    private ProcedimentoProdutoId id;

    @MapsId("procedimentoCodigo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pro_codigo", nullable = false)
    private Procedimento procedimento;

    @MapsId("produtoCodigo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pro_codigopro", nullable = false)
    private Produto produto;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;
}