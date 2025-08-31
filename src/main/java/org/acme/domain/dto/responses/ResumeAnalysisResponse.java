package org.acme.domain.dto.responses;

import java.util.UUID;

public record ResumeAnalysisResponse(
    UUID resumeId,
    UUID candidateId,
    String skills,
    Integer experienceYears,
    String summary
) {}