package com.ktotopawel.opsguard.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Audited
@Table(name = "incidents")
@SQLDelete(sql = "UPDATE incidents SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotAudited
    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.OPEN;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "reported_by_id", nullable = false)
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "closed_by_id")
    private User closedBy;

    @ManyToMany
    @JoinTable(
            name = "incident_tags",
            joinColumns = @JoinColumn(name = "incident_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
