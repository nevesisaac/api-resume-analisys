package org.acme.repository;

import org.acme.domain.entity.ResumeAnalysis;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class ResumeAnalysisRepository implements PanacheRepositoryBase<ResumeAnalysis, UUID> {
    // Métodos customizados podem ser
}