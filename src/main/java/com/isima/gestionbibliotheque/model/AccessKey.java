package com.isima.gestionbibliotheque.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "access_keys")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class AccessKey {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private Collection collection;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;
    private Date expiredAt;
    @ElementCollection(targetClass = Permission.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "access_key_permissions", joinColumns = @JoinColumn(name = "access_key_id"))
    @Column(name = "permission")
    private Set<Permission> permissions = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @Transient
    public boolean isExpired() {
        return expiredAt.before(new Date());
    }

}
