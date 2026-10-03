package br.com.migracao.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "anamnese")
public class Anamnese {

    @Id
    @Column(name = "pac_codigo")
    private Integer pacienteCodigo;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "pac_codigo")
    private Paciente paciente;

    @Column(name = "algumtratamento", nullable = false)
    private boolean algumTratamento;

    @Column(name = "hospitalizado", nullable = false)
    private boolean hospitalizado;

    @Column(name = "motivo", nullable = false, columnDefinition = "TEXT")
    private String motivo;

    @Column(name = "alergiamedicamentoanestesico", nullable = false)
    private boolean alergiaMedicamentoAnestesico;

    @Column(name = "qualcirurgia", nullable = false, columnDefinition = "TEXT")
    private String qualCirurgia;

    @Column(name = "transfusaosangue", nullable = false)
    private boolean transfusaoSangue;

    @Column(name = "bebidaalcoolica", nullable = false)
    private boolean bebidaAlcoolica;

    @Column(name = "problemacardiaco", nullable = false)
    private boolean problemaCardiaco;

    @Column(name = "febrereumatica", nullable = false)
    private boolean febreReumatica;

    @Column(name = "especialidade", nullable = false, columnDefinition = "TEXT")
    private String especialidade;

    @Column(name = "quandohospitalizado", nullable = false, length = 100)
    private String quandoHospitalizado;

    @Column(name = "tomamedicamento", nullable = false)
    private boolean tomaMedicamento;

    @Column(name = "alergia", nullable = false, columnDefinition = "TEXT")
    private String alergia;

    @Column(name = "sangramuito", nullable = false)
    private boolean sangraMuito;

    @Column(name = "fumante", nullable = false)
    private boolean fumante;

    @Column(name = "desmaiotontura")
    private Boolean desmaioTontura;

    @Column(name = "hiv", nullable = false)
    private boolean hiv;

    @Column(name = "tuberculose", nullable = false)
    private boolean tuberculose;

    @Column(name = "nomemedico", nullable = false, length = 100)
    private String nomeMedico;

    @Column(name = "tempo", nullable = false, length = 100)
    private String tempo;

    @Column(name = "medicamento", nullable = false, length = 100)
    private String medicamento;

    @Column(name = "cirurgia", nullable = false)
    private boolean cirurgia;

    @Column(name = "cirurgiabucal", nullable = false)
    private boolean cirurgiaBucal;

    @Column(name = "cigarrodia", nullable = false)
    private Integer cigarroDia;

    @Column(name = "respiratorio", nullable = false)
    private boolean respiratorio;

    @Column(name = "hepatite", nullable = false)
    private boolean hepatite;

    @Column(name = "demoracicatrizar", nullable = false)
    private boolean demoraCicatrizar;

    @Column(name = "depressao", nullable = false)
    private boolean depressao;

    @Column(name = "diabete", nullable = false)
    private boolean diabete;

    @Column(name = "hipertensao", nullable = false)
    private boolean hipertensao;

    @Column(name = "reumatismo", nullable = false)
    private boolean reumatismo;

    @Column(name = "renal", nullable = false)
    private boolean renal;

    @Column(name = "problemanervoso", nullable = false)
    private boolean problemaNervoso;

    @Column(name = "anemia", nullable = false)
    private boolean anemia;

    @Column(name = "epilepsia")
    private Boolean epilepsia;
}