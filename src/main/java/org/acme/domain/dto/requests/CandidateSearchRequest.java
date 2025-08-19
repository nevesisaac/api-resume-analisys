package org.acme.domain.dto.requests;

import java.util.HashMap;

public record CandidateSearchRequest(
    String name,                  // Nome parcial do candidato (opcional)
    String email,                 // E-mail (opcional, mais raro)
    HashMap<String,String> skills,          // Lista de skills desejadas
    Integer minExperienceYears,   // Experiência mínima
    Integer maxExperienceYears,   // Experiência máxima
    String location,              // Localização (cidade, estado, remoto)
    Boolean presential,
    Double minScore               // Score mínimo (baseado na análise)
) {}
