package org.acme.api;

import org.acme.domain.dto.CandidateDTO;
import org.acme.domain.entity.Candidate;
import org.acme.service.CandidateService;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/candidates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CandidateResource {

    @Inject
    CandidateService candidateService;

    /**
     * Cria um novo candidato
     */
    @POST
    public Response createCandidate(CandidateDTO candidateDTO) {
        candidateService.createCandidate(candidateDTO);
        return Response.status(Response.Status.CREATED).build();

    }

    /**
     * Lista todos os candidatos
     */
    @GET
    public List<CandidateDTO> listCandidates() {
        return candidateService.getAllCandidates()
                .stream()
                .map(candidate -> new CandidateDTO(candidate.id, candidate.fullName, candidate.email))
                .toList();
    }

    /**
     * Busca um candidato específico pelo ID
     */
    @GET
    @Path("/{id}")
    public Response getCandidate(
            @Parameter(description = "UUID do candidato", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathParam("id") String id) {
        Candidate candidate = candidateService.getCandidateById(UUID.fromString(id));
        if (candidate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new CandidateDTO(candidate.id, candidate.fullName, candidate.email)).build();
    }

    /**
     * Remove um candidato
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCandidate(
            @Parameter(description = "UUID do candidato", required = true, example = "123e4567-e89b-12d3-a456-426614174000") 
            @PathParam("id") String id) {
        boolean deleted = candidateService.deleteCandidate(UUID.fromString(id));
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}