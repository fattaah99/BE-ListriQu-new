package org.acme.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MASTER_ROLE")
public class MasterRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer role_id;

    @Column(nullable = false, unique = true)
    public String role_code;

    @Column(nullable = false)
    public String role_name;

    public String description;

    @Enumerated(EnumType.STRING)
    public Status status = Status.Active;

    public LocalDateTime created_at;
    public LocalDateTime updated_at;

    public enum Status {
        Active, Inactive
    }
}
