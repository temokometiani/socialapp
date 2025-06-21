package com.example.social.model.entity;

import com.example.social.model.enums.Visibility;
import com.example.social.model.enums.FileType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "files")
@Getter
@Setter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private FileType fileType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @Embedded
    private MinioInfo minioInfo; // MinIO details embedded here

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SelectedUserFileAccess> userAccessRecords;

    public Long getId() {
        return id;
    }

    public FileType getFileType() {
        return fileType;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public MinioInfo getMinioInfo() {
        return minioInfo;
    }

    public Album getAlbum() {
        return album;
    }

    public User getOwner() {
        return owner;
    }

    public Set<SelectedUserFileAccess> getUserAccessRecords() {
        return userAccessRecords;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public void setMinioInfo(MinioInfo minioInfo) {
        this.minioInfo = minioInfo;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setUserAccessRecords(Set<SelectedUserFileAccess> userAccessRecords) {
        this.userAccessRecords = userAccessRecords;
    }
}
