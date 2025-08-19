package org.acme.domain.dto.requests;


import org.jboss.resteasy.reactive.PartType;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

public class ResumeUploadRequest {

    @FormParam("candidateId")
    private Long candidateId;

    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private byte[] file;

    @FormParam("fileName")
    private String fileName;

    public Long getCandidateId() {
        return candidateId;
    }

    public byte[] getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }
}