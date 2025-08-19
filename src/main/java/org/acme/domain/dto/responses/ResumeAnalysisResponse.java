package org.acme.domain.dto.responses;

public record ResumeAnalysisResponse(
    Long resumeId,
    String candidateName,
    String skills,
    Integer experienceYears,
    String summary,
    Double score
) {}