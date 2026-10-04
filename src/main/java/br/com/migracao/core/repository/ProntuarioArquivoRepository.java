package br.com.migracao.core.repository;

import br.com.migracao.core.domain.entity.ProntuarioArquivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProntuarioArquivoRepository
        extends JpaRepository<ProntuarioArquivo, Integer> {

    List<ProntuarioArquivo>
    findByPaciente_CodigoOrderByCriadoEmDesc(
            Integer pacienteCodigo
    );

    Optional<ProntuarioArquivo>
    findByCodigoAndPaciente_Codigo(
            Integer codigo,
            Integer pacienteCodigo
    );
}