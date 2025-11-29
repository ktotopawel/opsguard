package com.ktotopawel.opsguard.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Data
@Entity
@Audited
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
