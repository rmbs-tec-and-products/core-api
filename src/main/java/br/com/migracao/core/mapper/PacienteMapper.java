package br.com.migracao.core.mapper;

import br.com.migracao.core.domain.entity.Anamnese;
import br.com.migracao.core.domain.entity.Paciente;
import br.com.migracao.core.dto.paciente.AnamneseRequest;
import br.com.migracao.core.dto.paciente.AnamneseResponse;
import br.com.migracao.core.dto.paciente.PacienteRequest;
import br.com.migracao.core.dto.paciente.PacienteResponse;
import br.com.migracao.core.dto.paciente.PacienteResumoResponse;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public Paciente toEntity(PacienteRequest request) {

        Paciente paciente = Paciente.builder()
                .nome(request.nome())
                .telefone(request.telefone())
                .celular(request.celular())
                .cpf(request.cpf())
                .dataNascimento(request.dataNascimento())
                .sexo(request.sexo())
                .endereco(request.endereco())
                .numero(request.numero())
                .cep(request.cep())
                .cidade(request.cidade())
                .estado(request.estado())
                .bairro(request.bairro())
                .build();

        Anamnese anamnese = toAnamneseEntity(request.anamnese());

        anamnese.setPaciente(paciente);
        paciente.setAnamnese(anamnese);

        return paciente;
    }

    public PacienteResponse toResponse(Paciente paciente) {

        return new PacienteResponse(
                paciente.getCodigo(),
                paciente.getNome(),
                paciente.getTelefone(),
                paciente.getCelular(),
                paciente.getCpf(),
                paciente.getDataNascimento(),
                paciente.getSexo(),
                paciente.getEndereco(),
                paciente.getNumero(),
                paciente.getCep(),
                paciente.getCidade(),
                paciente.getEstado(),
                paciente.getBairro(),
                toAnamneseResponse(paciente.getAnamnese())
        );
    }

    public PacienteResumoResponse toResumoResponse(Paciente paciente) {

        return new PacienteResumoResponse(
                paciente.getCodigo(),
                paciente.getNome(),
                paciente.getTelefone(),
                paciente.getCelular()
        );
    }

    public void updateEntity(
            Paciente paciente,
            PacienteRequest request
    ) {

        paciente.setNome(request.nome());
        paciente.setTelefone(request.telefone());
        paciente.setCelular(request.celular());
        paciente.setCpf(request.cpf());
        paciente.setDataNascimento(request.dataNascimento());
        paciente.setSexo(request.sexo());
        paciente.setEndereco(request.endereco());
        paciente.setNumero(request.numero());
        paciente.setCep(request.cep());
        paciente.setCidade(request.cidade());
        paciente.setEstado(request.estado());
        paciente.setBairro(request.bairro());

        if (paciente.getAnamnese() == null) {

            Anamnese anamnese = toAnamneseEntity(request.anamnese());

            anamnese.setPaciente(paciente);
            paciente.setAnamnese(anamnese);

        } else {

            updateAnamnese(
                    paciente.getAnamnese(),
                    request.anamnese()
            );
        }
    }

    private Anamnese toAnamneseEntity(
            AnamneseRequest request
    ) {

        return Anamnese.builder()
                .algumTratamento(request.algumTratamento())
                .hospitalizado(request.hospitalizado())
                .motivo(request.motivo())
                .alergiaMedicamentoAnestesico(request.alergiaMedicamentoAnestesico())
                .qualCirurgia(request.qualCirurgia())
                .transfusaoSangue(request.transfusaoSangue())
                .bebidaAlcoolica(request.bebidaAlcoolica())
                .problemaCardiaco(request.problemaCardiaco())
                .febreReumatica(request.febreReumatica())
                .especialidade(request.especialidade())
                .quandoHospitalizado(request.quandoHospitalizado())
                .tomaMedicamento(request.tomaMedicamento())
                .alergia(request.alergia())
                .sangraMuito(request.sangraMuito())
                .fumante(request.fumante())
                .desmaioTontura(request.desmaioTontura())
                .hiv(request.hiv())
                .tuberculose(request.tuberculose())
                .nomeMedico(request.nomeMedico())
                .tempo(request.tempo())
                .medicamento(request.medicamento())
                .cirurgia(request.cirurgia())
                .cirurgiaBucal(request.cirurgiaBucal())
                .cigarroDia(
                        request.cigarroDia() == null
                                ? 0
                                : request.cigarroDia()
                )
                .respiratorio(request.respiratorio())
                .hepatite(request.hepatite())
                .demoraCicatrizar(request.demoraCicatrizar())
                .depressao(request.depressao())
                .diabete(request.diabete())
                .hipertensao(request.hipertensao())
                .reumatismo(request.reumatismo())
                .renal(request.renal())
                .problemaNervoso(request.problemaNervoso())
                .anemia(request.anemia())
                .epilepsia(request.epilepsia())
                .build();
    }

    private void updateAnamnese(
            Anamnese anamnese,
            AnamneseRequest request
    ) {

        anamnese.setAlgumTratamento(request.algumTratamento());
        anamnese.setHospitalizado(request.hospitalizado());
        anamnese.setMotivo(request.motivo());
        anamnese.setAlergiaMedicamentoAnestesico(
                request.alergiaMedicamentoAnestesico()
        );
        anamnese.setQualCirurgia(request.qualCirurgia());
        anamnese.setTransfusaoSangue(request.transfusaoSangue());
        anamnese.setBebidaAlcoolica(request.bebidaAlcoolica());
        anamnese.setProblemaCardiaco(request.problemaCardiaco());
        anamnese.setFebreReumatica(request.febreReumatica());
        anamnese.setEspecialidade(request.especialidade());
        anamnese.setQuandoHospitalizado(request.quandoHospitalizado());
        anamnese.setTomaMedicamento(request.tomaMedicamento());
        anamnese.setAlergia(request.alergia());
        anamnese.setSangraMuito(request.sangraMuito());
        anamnese.setFumante(request.fumante());
        anamnese.setDesmaioTontura(request.desmaioTontura());
        anamnese.setHiv(request.hiv());
        anamnese.setTuberculose(request.tuberculose());
        anamnese.setNomeMedico(request.nomeMedico());
        anamnese.setTempo(request.tempo());
        anamnese.setMedicamento(request.medicamento());
        anamnese.setCirurgia(request.cirurgia());
        anamnese.setCirurgiaBucal(request.cirurgiaBucal());
        anamnese.setCigarroDia(
                request.cigarroDia() == null
                        ? 0
                        : request.cigarroDia()
        );
        anamnese.setRespiratorio(request.respiratorio());
        anamnese.setHepatite(request.hepatite());
        anamnese.setDemoraCicatrizar(request.demoraCicatrizar());
        anamnese.setDepressao(request.depressao());
        anamnese.setDiabete(request.diabete());
        anamnese.setHipertensao(request.hipertensao());
        anamnese.setReumatismo(request.reumatismo());
        anamnese.setRenal(request.renal());
        anamnese.setProblemaNervoso(request.problemaNervoso());
        anamnese.setAnemia(request.anemia());
        anamnese.setEpilepsia(request.epilepsia());
    }

    private AnamneseResponse toAnamneseResponse(
            Anamnese anamnese
    ) {

        if (anamnese == null) {
            return null;
        }

        return new AnamneseResponse(
                anamnese.isAlgumTratamento(),
                anamnese.isHospitalizado(),
                anamnese.getMotivo(),
                anamnese.isAlergiaMedicamentoAnestesico(),
                anamnese.getQualCirurgia(),
                anamnese.isTransfusaoSangue(),
                anamnese.isBebidaAlcoolica(),
                anamnese.isProblemaCardiaco(),
                anamnese.isFebreReumatica(),
                anamnese.getEspecialidade(),
                anamnese.getQuandoHospitalizado(),
                anamnese.isTomaMedicamento(),
                anamnese.getAlergia(),
                anamnese.isSangraMuito(),
                anamnese.isFumante(),
                anamnese.getDesmaioTontura(),
                anamnese.isHiv(),
                anamnese.isTuberculose(),
                anamnese.getNomeMedico(),
                anamnese.getTempo(),
                anamnese.getMedicamento(),
                anamnese.isCirurgia(),
                anamnese.isCirurgiaBucal(),
                anamnese.getCigarroDia(),
                anamnese.isRespiratorio(),
                anamnese.isHepatite(),
                anamnese.isDemoraCicatrizar(),
                anamnese.isDepressao(),
                anamnese.isDiabete(),
                anamnese.isHipertensao(),
                anamnese.isReumatismo(),
                anamnese.isRenal(),
                anamnese.isProblemaNervoso(),
                anamnese.isAnemia(),
                anamnese.getEpilepsia()
        );
    }
}