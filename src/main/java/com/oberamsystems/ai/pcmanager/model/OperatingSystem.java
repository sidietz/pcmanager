package com.oberamsystems.ai.pcmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class OperatingSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OSFamily osFamily;

    private String name;
    
    private String version;
    private java.time.LocalDate eolDate;

    @Enumerated(EnumType.STRING)
    private ReleaseType releaseType;

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

    public OSFamily getOsFamily() {
        return osFamily;
    }

    public void setOsFamily(OSFamily osFamily) {
        this.osFamily = osFamily;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public java.time.LocalDate getEolDate() {
        return eolDate;
    }

    public void setEolDate(java.time.LocalDate eolDate) {
        this.eolDate = eolDate;
    }

    public ReleaseType getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(ReleaseType releaseType) {
        this.releaseType = releaseType;
    }
}
