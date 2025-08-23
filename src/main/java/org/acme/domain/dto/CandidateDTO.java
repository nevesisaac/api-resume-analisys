package org.acme.domain.dto;

import java.util.UUID;

public record CandidateDTO(
    UUID id,
    String name,
    String email
) {}