package org.acme.api;

import org.acme.domain.dto.CandidateDTO;
import org.acme.domain.entity.Candidate;
import org.acme.service.CandidateService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

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
        Candidate candidate = candidateService.createCandidate(candidateDTO);
        return Response.status(Response.Status.CREATED)
                .entity(new CandidateDTO(candidate.id, candidate.fullName, candidate.email))
                .build();
    }

    /**
     * Lista todos os candidatos
     */
    @GET
    public List<CandidateDTO> listCandidates() {
        return candidateService.getAllCandidates()
                .stream()
                .map(candidate -> new CandidateDTO(candidate.id, candidate.fullName, candidate.email))
                .collect(Collectors.toList());
    }

    /**
     * Busca um candidato específico pelo ID
     */
    @GET
    @Path("/{id}")
    public Response getCandidate(@PathParam("id") Long id) {
        Candidate candidate = candidateService.getCandidateById(id);
        if (candidate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new CandidateDTO(candidate.id, candidate.fullName, candidate.email)).build();
    }

    /**
     * Atualiza um candidato existente
     */
    @PUT
    @Path("/{id}")
    public Response updateCandidate(@PathParam("id") Long id, CandidateDTO candidateDTO) {
        Candidate updatedCandidate = candidateService.updateCandidate(id, candidateDTO);
        if (updatedCandidate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new CandidateDTO(updatedCandidate.id, updatedCandidate.fullName, updatedCandidate.email)).build();
    }

    /**
     * Remove um candidato
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCandidate(@PathParam("id") Long id) {
        boolean deleted = candidateService.deleteCandidate(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}