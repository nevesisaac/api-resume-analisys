package org.acme.repository;

import org.acme.domain.entity.Resume;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class ResumeRepository implements PanacheRepositoryBase<Resume, UUID> {
    // Métodos customizados podem ser adicionados aqui
}