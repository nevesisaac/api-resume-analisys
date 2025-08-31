package org.acme.domain.dto.requests;

import java.io.File;
import java.util.UUID;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;


public record ResumeUploadRequest(
    @RestForm UUID candidateId,
    @RestForm FileUpload file,
    @RestForm String fileName
) {}