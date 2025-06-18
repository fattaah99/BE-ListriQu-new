package org.acme.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MASTER_MENU")
public class MasterMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // gunakan SERIAL / AUTO_INCREMENT
    @Column(name = "menu_id")
    public Integer menu_id;


    @ManyToOne
    @JoinColumn(name = "parent_id")
    public MasterMenu parent;

    @Column(nullable = false, unique = true)
    public String menu_code;

    @Column(nullable = false)
    public String menu_name;

    public String menu_icon;
    public String menu_url;
    public Integer menu_order;

    @Enumerated(EnumType.STRING)
    public Status is_active = Status.Active;

    public LocalDateTime created_at;
    public LocalDateTime updated_at;

    public Integer created_by;
    public Integer updated_by;

    public enum Status {
        Active, Inactive
    }
}
