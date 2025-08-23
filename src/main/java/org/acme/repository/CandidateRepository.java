package org.acme.repository;

import org.acme.domain.entity.Candidate;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.UUID;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CandidateRepository implements PanacheRepositoryBase<Candidate, UUID> {
}