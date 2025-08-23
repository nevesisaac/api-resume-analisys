package org.acme.service;

import org.acme.domain.dto.CandidateDTO;
import org.acme.domain.entity.Candidate;
import org.acme.repository.CandidateRepository;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CandidateService {

    @Inject
    CandidateRepository candidateRepository;

    /**
     * Cria um novo candidato
     */
    @Transactional
    public void createCandidate(CandidateDTO candidateDTO) {
        try {
            Candidate candidate = new Candidate();
            candidate.fullName = candidateDTO.name();
            candidate.email = candidateDTO.email();
            candidateRepository.persist(candidate);
        } catch (Exception e) {
            Log.error("Error creating candidate", e);
        }
    }

    /**
     * Retorna todos os candidatos
     */
    public List<Candidate> getAllCandidates() {
        return candidateRepository.listAll();
    }

    /**
     * Busca um candidato pelo ID
     */
    public Candidate getCandidateById(UUID id) {
        return candidateRepository.findById(id);
    }

    /**
     * Atualiza um candidato existente
     */
    @Transactional
    public Candidate updateCandidate(UUID id, CandidateDTO candidateDTO) {
        Candidate candidate = candidateRepository.findById(id);
        if (candidate == null) {
            return null;
        }
        candidate.fullName = candidateDTO.name();
        candidate.email = candidateDTO.email();
        candidate.persist();
        return candidate;
    }

    /**
     * Remove um candidato
     */
    @Transactional
    public boolean deleteCandidate(UUID id) {
        Candidate candidate = candidateRepository.findById(id);
        if (candidate == null) {
            return false;
        }
        candidate.delete();
        return true;
    }
}
