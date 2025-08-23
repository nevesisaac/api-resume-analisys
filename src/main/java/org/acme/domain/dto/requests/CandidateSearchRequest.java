package org.acme.domain.dto.requests;

import java.util.HashMap;

public record CandidateSearchRequest(
    HashMap<String,String> skills,          // Lista de skills desejadas
    Integer minExperienceYears,   // Experiência mínima
    Integer maxExperienceYears,   // Experiência máxima
    String locationType,              // Localização (cidade, estado, remoto)
    String education,             // Nível educacional (ex: Ensino Superior, Mestrado)
    Double minScore               // Score mínimo (baseado na análise)
) {}
