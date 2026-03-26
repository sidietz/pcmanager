package com.oberamsystems.ai.pcmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import org.hibernate.annotations.Formula;

@Entity
public class Software {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @jakarta.persistence.Column(name = "version")
    private String installedVersion;

    @jakarta.persistence.Column(name = "available_version")
    private String availableVersion;
    private String url;

    @Enumerated(EnumType.STRING)
    private SoftwareOrigin origin;

    @Enumerated(EnumType.STRING)
    private InstallationType installationType;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime installedAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime lastUpdated;

    @Formula(value = "version = available_version")
    private boolean isUpToDate;

    public boolean isUpToDate() {
        return isUpToDate;
    }

    public void setUpToDate(boolean isUpToDate) {
        this.isUpToDate = isUpToDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstalledVersion() {
        return installedVersion;
    }

    public void setInstalledVersion(String installedVersion) {
        this.installedVersion = installedVersion;
    }

    public String getAvailableVersion() {
        return availableVersion;
    }

    public void setAvailableVersion(String availableVersion) {
        this.availableVersion = availableVersion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SoftwareOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(SoftwareOrigin origin) {
        this.origin = origin;
    }

    public InstallationType getInstallationType() {
        return installationType;
    }

    public void setInstallationType(InstallationType installationType) {
        this.installationType = installationType;
    }

    public LocalDateTime getInstalledAt() {
        return installedAt;
    }

    public void setInstalledAt(LocalDateTime installedAt) {
        this.installedAt = installedAt;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
