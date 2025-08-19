package org.acme.domain.dto.responses;


public record CandidateSearchResult(
    Long candidateId,
    String name,
    String email,
    String location,
    Double matchScore   // Calculado dinamicamente pela API
) {}