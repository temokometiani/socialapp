package com.example.social.model.entity;

import com.example.social.model.enums.AccessLevel;
import com.example.social.model.enums.SeeAllFiles;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "album_accesses")
@Getter
@Setter
public class AlbumAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    public Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_level", nullable = false)
    private AccessLevel accessLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "see_all_files", nullable = false)
    private SeeAllFiles seeAllFiles;

    @Column(name = "granted_at", nullable = false, updatable = false)
    private LocalDateTime grantedAt;


    public Long getId() {
        return id;
    }

    public Album getAlbum() {
        return album;
    }

    public User getUser() {
        return user;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public SeeAllFiles getSeeAllFiles() {
        return seeAllFiles;
    }

    public LocalDateTime getGrantedAt() {
        return grantedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public void setSeeAllFiles(SeeAllFiles seeAllFiles) {
        this.seeAllFiles = seeAllFiles;
    }

    public void setGrantedAt(LocalDateTime grantedAt) {
        this.grantedAt = grantedAt;
    }
}