package org.acme.domain.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resumes")
public class Resume extends PanacheEntityBase {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    public UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    public Candidate candidate;

    @Column(name = "file_name", nullable = false, length = 255)
    public String fileName;

    @Column(name = "file_url", nullable = false, columnDefinition = "text")
    public String fileUrl;

    @Column(name = "file_type", length = 50)
    public String fileType;

    @Column(name = "uploaded_at", nullable = false)
    public LocalDateTime uploadedAt = LocalDateTime.now();
}