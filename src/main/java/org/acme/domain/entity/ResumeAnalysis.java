package org.acme.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resume_analysis")
public class ResumeAnalysis extends PanacheEntityBase {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    public UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resume_id", nullable = false)
    public Resume resume;

    @Column(columnDefinition = "jsonb")
    public String skills;

    @Column(name = "experience_years")
    public Integer experienceYears;

    @Column(columnDefinition = "jsonb")
    public String education;

    @Column(columnDefinition = "jsonb")
    public String languages;

    @Column(columnDefinition = "text")
    public String summary;

    @Column(precision = 5, scale = 2)
    public BigDecimal score;

    @Column(name = "analyzed_at", nullable = false)
    public LocalDateTime analyzedAt = LocalDateTime.now();
}