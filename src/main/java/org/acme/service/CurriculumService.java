package org.acme.service;

import org.acme.domain.dto.requests.CandidateSearchRequest;
import org.acme.domain.dto.requests.ResumeUploadRequest;
import org.acme.domain.dto.responses.ResumeAnalysisResponse;
import org.acme.domain.entity.Candidate;
import org.acme.domain.entity.Resume;
import org.acme.repository.CandidateRepository;
import org.acme.repository.ResumeRepository;
import org.apache.camel.model.ThrowExceptionDefinition;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import net.bytebuddy.implementation.bytecode.Throw;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CurriculumService {

    @Inject
    CandidateRepository candidateRepository;

    @Inject
    ResumeRepository resumeRepository;

    /**
     * Salva o currículo enviado e associa ao candidato
     */
    @Transactional
    public void saveResume(ResumeUploadRequest curriculumRequest) {
        // Buscar ou criar candidato
        Candidate candidate = candidateRepository.find("id", curriculumRequest.getCandidateId()).firstResult();
        if (candidate == null) {
            throw new IllegalArgumentException("Candidato não encontrado");
        }
        // Validar arquivo
        if (curriculumRequest.getFile() == null || curriculumRequest.getFile().length == 0) {
            throw new IllegalArgumentException("Arquivo não pode ser vazio");
        }

        // Armazenar arquivo em cloudStorage

        // Criar currículo
        Resume resume = new Resume();
        resume.candidate = candidate;
        resume.fileName = curriculumRequest.getFileName();
        resume.fileUrl = "url/to/the/uploaded/file"; // Url gerada após armzenamento S3
        resume.uploadedAt = java.time.LocalDateTime.now();

        // Persistir currículo
        resumeRepository.persist(resume);
    }

    /**
     * Retorna a análise de currículo de um candidato específico
     */
    public ResumeAnalysisResponse getResumeAnalysis(UUID candidateId) {
        // 1. Buscar Candidate e Resume no banco.
        Candidate candidate = candidateRepository.findById(candidateId);
        if (candidate == null) {
            throw new IllegalArgumentException("Candidato não encontrado");
        }
        Resume resume = resumeRepository.find("candidate.id", candidateId).firstResult();
        if (resume == null) {
            throw new IllegalArgumentException("Currículo não encontrado para o candidato");
        }

        // 2. Chamar serviço de IA (simulação)
        // Exemplo: análise fictícia do currículo
        String analysisSummary = "Resumo da análise do currículo para " + candidate.fullName;
        double score = 85.0; // Exemplo de score fictício
        Integer experienceYears = 5; // Exemplo de anos de experiência fictício

        // 3. Retornar objeto ResumeAnalysisResponse.
        ResumeAnalysisResponse response = new ResumeAnalysisResponse(candidateId, resume.id, analysisSummary,
                experienceYears, analysisSummary, score);
        return response;
    }

    /**
     * Busca candidatos com base em critérios de seleção
     */
    public List<Candidate> searchCandidates(CandidateSearchRequest request, int page, int size) {
        var entityManager = candidateRepository.getEntityManager();
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var createQuery = criteriaBuilder.createQuery(Candidate.class);
        var candidate = createQuery.from(Candidate.class);

        List<Predicate> predicates = new ArrayList<>();

        if (request.skills() != null && !request.skills().isEmpty()) {
            predicates.add(criteriaBuilder.like(candidate.get("skills"), "%" + request.skills() + "%"));
        }
        if (request.minExperienceYears() != null) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(candidate.get("experience"), request.minExperienceYears()));
        }
        if (request.education() != null && !request.education().isEmpty()) {
            predicates.add(criteriaBuilder.equal(candidate.get("education"), request.education()));
        }

        createQuery.select(candidate).where(predicates.toArray(new Predicate[0]));

        var query = entityManager.createQuery(createQuery);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    public boolean deleteResume(UUID resumeId) {
        Resume resume = resumeRepository.findById(resumeId);
        if (resume == null) {
            return false;
        }
        resumeRepository.delete(resume);
        return true;
    }

    public InputStream getCurriculum(UUID candidateId) {
        Resume resume = resumeRepository.find("candidate.id", candidateId).firstResult();
        if (resume == null || resume.fileUrl == null) {
            throw new IllegalArgumentException("Arquivo não encontrado");
        }

        // Buscar arquivo no S3
        return new java.io.ByteArrayInputStream(new byte[0]);
    }

    public Resume getResumeByCandidate(UUID candidateId) {
        return resumeRepository.find("candidate.id", candidateId).firstResult();
    }
}