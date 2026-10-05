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
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Integer codigo;

    @Column(
            name = "nome",
            nullable = false,
            length = 300
    )
    private String nome;

    @Column(
            name = "telefone",
            length = 15
    )
    private String telefone;

    @Column(
            name = "celular",
            nullable = false,
            length = 15
    )
    private String celular;

    @Column(
            name = "cpf",
            length = 18
    )
    private String cpf;

    @Column(name = "datanascimento")
    private LocalDateTime dataNascimento;

    @Column(
            name = "sexo",
            length = 10
    )
    private String sexo;

    @Column(
            name = "endereco",
            length = 200
    )
    private String endereco;

    @Column(
            name = "numero",
            length = 20
    )
    private String numero;

    @Column(
            name = "cep",
            length = 10
    )
    private String cep;

    @Column(
            name = "cidade",
            length = 100
    )
    private String cidade;

    @Column(
            name = "estado",
            length = 2
    )
    private String estado;

    @Column(
            name = "bairro",
            length = 50
    )
    private String bairro;

    @OneToOne(
            mappedBy = "paciente",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Anamnese anamnese;

    @OneToMany(
            mappedBy = "paciente",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Agenda> agendas =
            new ArrayList<>();

    @OneToMany(
            mappedBy = "paciente",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Odontograma> odontogramas =
            new ArrayList<>();
}