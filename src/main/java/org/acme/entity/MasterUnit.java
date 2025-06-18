package org.acme.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MASTER_UNIT")
public class MasterUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unit_id")
    public Integer unit_id;

    @Column(nullable = false, unique = true)
    public String unit_code;

    @Column(nullable = false)
    public String unit_name;

    public String description;

    @ManyToOne
    @JoinColumn(name = "parent_unit_id")
    public MasterUnit parent_unit;

    public String address;
    public String phone;
    public String email;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    public MasterUser manager;

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
