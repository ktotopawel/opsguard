package com.ktotopawel.opsguard.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Data
@Entity
@Audited
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;
}
