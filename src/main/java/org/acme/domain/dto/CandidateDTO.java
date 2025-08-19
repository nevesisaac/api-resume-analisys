package org.acme.domain.dto;

import java.util.UUID;

public record CandidateDTO(
    UUID id,
    String name,
    String email
) {

    public CandidateDTO(UUID id, String name, String email) {
        this.id =id;
        this.name = name;
        this.email= email;
    }}