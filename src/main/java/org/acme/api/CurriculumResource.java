package org.acme.api;

import org.acme.domain.dto.requests.CandidateSearchRequest;
import org.acme.domain.dto.requests.ResumeUploadRequest;
import org.acme.domain.dto.responses.ResumeAnalysisResponse;
import org.acme.domain.entity.Resume;
import org.acme.service.CurriculumService;
import org.jboss.resteasy.reactive.MultipartForm;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.UUID;

@Path("/resume")
@Produces(MediaType.APPLICATION_JSON)
public class CurriculumResource {

    @Inject
    CurriculumService curriculumService;

    /**
     * Upload de currículo em PDF
     */
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadResume(@MultipartForm ResumeUploadRequest curriculumRequest) {
        Resume existingResume = curriculumService.getResumeByCandidate(curriculumRequest.candidateId());
        if (existingResume != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Candidato já possui um currículo.")
                    .build();
        }
        Resume savedResume = curriculumService.saveResume(curriculumRequest);
        curriculumService.publishResumeToQueue(savedResume);
        return Response.accepted().build();
    }

    /**
     * Download do PDF do currículo pelo ID
     */
    @GET
    @Path("/{id}/download")
    @Produces("application/pdf")
    public Response downloadCurriculum(@PathParam("id") UUID candidateId) {
        InputStream fileStream = curriculumService.getCurriculum(candidateId);
        if (fileStream == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(fileStream)
                .header("Content-Disposition", "attachment; filename=resume.pdf")
                .build();
    }

    /**
     * Remove currículo de um candidato
     */
    @DELETE
    @Path("/{id}")
    public Response deleteResume(@PathParam("id") UUID resumeId) {
        boolean deleted = curriculumService.deleteResume(resumeId);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    /**
     * Consulta análise de um candidato específico
     */
    @GET
    @Path("/{id}/analysis")
    public ResumeAnalysisResponse getAnalysisUser(@PathParam("id") UUID candidateId) {
        return curriculumService.getResumeAnalysis(candidateId);
    }

    /**
     * Busca candidatos por critérios (skills, experiência, score)
     */
    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchCandidates(
            CandidateSearchRequest candidateSearchRequest,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return Response.ok(curriculumService.searchCandidates(candidateSearchRequest, page, size)).build();
    }
}