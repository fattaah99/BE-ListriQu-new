package org.acme.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ROLE_MENU")
public class RoleMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_menu_id")
    public Integer role_menu_id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    public MasterRole role;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    public MasterMenu menu;

    @Enumerated(EnumType.STRING)
    public Status is_active = Status.Active;

    public LocalDateTime created_at;
    public LocalDateTime updated_at;

    public enum Status {
        Active, Inactive
    }
}
