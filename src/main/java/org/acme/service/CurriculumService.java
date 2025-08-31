package org.acme.service;

import org.acme.domain.dto.requests.CandidateSearchRequest;
import org.acme.domain.dto.requests.ResumeUploadRequest;
import org.acme.domain.dto.responses.ResumeAnalysisResponse;
import org.acme.domain.entity.Candidate;
import org.acme.domain.entity.Resume;
import org.acme.repository.CandidateRepository;
import org.acme.repository.ResumeRepository;
import org.acme.service.rabbitmq.ProducerRabbitMQ;
import org.acme.utils.enums.EducationLevel;
import org.acme.utils.enums.LocationType;
import org.acme.utils.enums.Skill;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CurriculumService {

    private static final String CANDIDATE_ID = "candidate.id";

    @Inject
    CandidateRepository candidateRepository;

    @Inject
    ResumeRepository resumeRepository;

    @Inject
    ProducerRabbitMQ producerRabbitMQ;

    /**
     * Salva o currículo enviado e associa ao candidato
     */
    @Transactional
    public Resume saveResume(ResumeUploadRequest curriculumRequest) {
        // Buscar candidato
        Candidate candidate = candidateRepository.find("id", curriculumRequest.candidateId()).firstResult();
        if (candidate == null) {
            throw new IllegalArgumentException("Candidato não encontrado");
        }
        // Validar arquivo
        if (curriculumRequest.file() == null) {
            throw new IllegalArgumentException("Arquivo não pode ser vazio");
        }

        // Armazenar arquivo em cloudStorage

        // Criar currículo
        Resume resume = new Resume();
        resume.candidate = candidate;
        resume.fileName = curriculumRequest.fileName();
        resume.fileUrl = "url/to/the/uploaded/file"; // Url gerada após armzenamento S3
        resume.uploadedAt = java.time.LocalDateTime.now();

        // Persistir currículo
        resumeRepository.persist(resume);

        return resume;
    }

    public void publishResumeToQueue(Resume resume) {
        try {
            producerRabbitMQ.enviarMensagem(resumeToJson(resume));
        } catch (JsonProcessingException e) {
            Log.error("Erro ao publicar mensagem no RabbitMQ: " + e.getMessage());
        }
    }

    private String resumeToJson(Resume resume) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.findAndRegisterModules();
        return mapper.writeValueAsString(resume);
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
        Resume resume = resumeRepository.find(CANDIDATE_ID, candidateId).firstResult();
        if (resume == null) {
            throw new IllegalArgumentException("Currículo não encontrado para o candidato");
        }

        // 2. Busca registro no BD
        String analysisSummary = "Resumo da análise do currículo para " + candidate.fullName;
        Integer experienceYears = 5; // Exemplo de anos de experiência fictício

        // 3. Retornar objeto ResumeAnalysisResponse.
        return new ResumeAnalysisResponse(candidateId, resume.id, analysisSummary,
                experienceYears, analysisSummary);
    }

    public List<Candidate> searchCandidates(CandidateSearchRequest request, int page, int size) {
        var entityManager = candidateRepository.getEntityManager();
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(Candidate.class);
        var candidateRoot = criteriaQuery.from(Candidate.class);

        List<Predicate> predicates = new ArrayList<>();

        addSkillPredicates(predicates, criteriaBuilder, candidateRoot, request);
        addEducationPredicate(predicates, criteriaBuilder, candidateRoot, request);
        addLocationTypePredicate(predicates, criteriaBuilder, candidateRoot, request);

        // Experiência mínima
        if (request.minExperienceYears() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(candidateRoot.get("experience"),
                    request.minExperienceYears()));
        }

        criteriaQuery.select(candidateRoot).where(predicates.toArray(new Predicate[0]));

        var typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    private void addSkillPredicates(List<Predicate> predicates, CriteriaBuilder criteriaBuilder,
            Root<Candidate> candidateRoot, CandidateSearchRequest request) {
        if (request.skills() != null && !request.skills().isEmpty()) {
            List<String> validSkills = request.skills().values().stream()
                    .filter(skill -> {
                        try {
                            Skill.valueOf(skill);
                            return true;
                        } catch (IllegalArgumentException e) {
                            return false;
                        }
                    })
                    .toList();

            for (String skill : validSkills) {
                predicates.add(criteriaBuilder.like(candidateRoot.get("skills"), "%" + skill + "%"));
            }
        }
    }

    private void addEducationPredicate(List<Predicate> predicates, CriteriaBuilder criteriaBuilder,
            Root<Candidate> candidateRoot, CandidateSearchRequest request) {
        if (request.education() != null && !request.education().isEmpty()) {
            try {
                EducationLevel.valueOf(request.education());
                predicates.add(criteriaBuilder.equal(candidateRoot.get("education"), request.education()));
            } catch (IllegalArgumentException e) {
                // Ignora se não for válido
            }
        }
    }

    private void addLocationTypePredicate(List<Predicate> predicates, CriteriaBuilder criteriaBuilder,
            Root<Candidate> candidateRoot, CandidateSearchRequest request) {
        if (request.locationType() != null && !request.locationType().isEmpty()) {
            try {
                LocationType.valueOf(request.locationType());
                predicates.add(criteriaBuilder.equal(candidateRoot.get("locationType"), request.locationType()));
            } catch (IllegalArgumentException e) {
                // Ignora se não for válido
            }
        }
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
        Resume resume = resumeRepository.find(CANDIDATE_ID, candidateId).firstResult();
        if (resume == null || resume.fileUrl == null) {
            throw new IllegalArgumentException("Arquivo não encontrado");
        }

        // Buscar arquivo no S3
        return new java.io.ByteArrayInputStream(new byte[0]);
    }

    public Resume getResumeByCandidate(UUID candidateId) {
        return resumeRepository.find(CANDIDATE_ID, candidateId).firstResult();
    }
}