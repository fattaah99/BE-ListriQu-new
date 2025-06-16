package org.acme.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MASTER_USER")
public class MasterUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer user_id;

    @Column(nullable = false, unique = true)
    public String username;

    @Column(nullable = false, unique = true)
    public String email;

    @Column(nullable = false)
    public String password;

    public String full_name;
    public String phone;
    public Integer unit_id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    public MasterRole role;

    @Enumerated(EnumType.STRING)
    public Status status = Status.Active;

    public LocalDateTime created_at;
    public LocalDateTime updated_at;

    public Integer created_by;
    public Integer updated_by;

    public enum Status {
        Active, Inactive
    }
}
