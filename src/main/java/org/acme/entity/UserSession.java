package org.acme.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_SESSIONS")
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer session_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public MasterUser user;

    @Column(nullable = false, unique = true)
    public String session_token;

    public LocalDateTime login_at;
    public LocalDateTime logout_at;

    public String ip_address;
    public String user_agent;

    @Enumerated(EnumType.STRING)
    public Status status = Status.Active;

    public enum Status {
        Active, Logout
    }
}
