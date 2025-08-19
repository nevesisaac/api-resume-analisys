package org.acme.service;

import org.acme.domain.dto.CandidateDTO;
import org.acme.domain.entity.Candidate;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CandidateService {

    /**
     * Cria um novo candidato
     */
    @Transactional
    public Candidate createCandidate(CandidateDTO candidateDTO) {
        Candidate candidate = new Candidate();
        candidate.fullName = candidateDTO.name();
        candidate.email = candidateDTO.email();
        // Persistir no banco
        candidate.persist();
        return candidate;
    }

    /**
     * Retorna todos os candidatos
     */
    public List<Candidate> getAllCandidates() {
        return Candidate.listAll(); // Assuming Panache Entity
    }

    /**
     * Busca um candidato pelo ID
     */
    public Candidate getCandidateById(Long id) {
        return Candidate.findById(id);
    }

    /**
     * Atualiza um candidato existente
     */
    @Transactional
    public Candidate updateCandidate(Long id, CandidateDTO candidateDTO) {
        Candidate candidate = Candidate.findById(id);
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
    public boolean deleteCandidate(Long id) {
        Candidate candidate = Candidate.findById(id);
        if (candidate == null) {
            return false;
        }
        candidate.delete();
        return true;
    }
}
