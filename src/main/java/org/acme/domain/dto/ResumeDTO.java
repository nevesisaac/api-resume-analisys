package org.acme.domain.dto;

public record ResumeDTO(
    Long id,
    String fileName,
    String fileUrl,
    Long candidateId
) {}