package org.acme.service;

import org.acme.domain.dto.requests.CandidateSearchRequest;
import org.acme.domain.dto.requests.ResumeUploadRequest;
import org.acme.domain.dto.responses.ResumeAnalysisResponse;
import org.acme.domain.entity.Resume;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.io.InputStream;
import java.util.List;

@ApplicationScoped
public class CurriculumService {

    /**
     * Salva o currículo enviado e associa ao candidato
     */
    @Transactional
    public void saveResume(ResumeUploadRequest curriculumRequest) {
        // 1. Extrair informações do arquivo PDF (nome, skills, experiências).
        // 2. Criar/atualizar Candidate.
        // 3. Persistir Resume vinculado ao Candidate.
        // 4. Opcional: enviar para fila de processamento assíncrono (ex: Kafka).
    }

    /**
     * Retorna a análise de currículo de um candidato específico
     */
    public ResumeAnalysisResponse getResumeAnalysis(Long candidateId) {
        // 1. Buscar Candidate e Resume no banco.
        // 2. Chamar serviço de IA (ex: API externa ou módulo local).
        // 3. Retornar objeto ResumeAnalysisResponse.
        return null;
    }

    /**
     * Busca candidatos com base em critérios de seleção
     */
    public List<CandidateSearchRequest> searchCandidates(CandidateSearchRequest request, int page, int size) {
        // 1. Montar query dinâmica (skills, experiência mínima, formação).
        // 2. Paginar resultados.
        // 3. Calcular "score" de aderência.
        // 4. Retornar lista de CandidateSearchResult.
        return List.of();
    }

    public boolean deleteResume(Long resumeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteResume'");
    }

    public InputStream getResumeFile(Long resumeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getResumeFile'");
    }

    public Resume getResumeByCandidate(Long candidateId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getResumeByCandidate'");
    }
}