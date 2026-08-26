package com.oberamsystems.ai.pcmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "stack_id")
    private SoftwareStack softwareStack;

    private String status;

    private Integer port;

    @Column(name = "installed_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime installedAt;

    @Column(name = "last_updated")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime lastUpdated;

    @ManyToMany
    @JoinTable(name = "service_software", joinColumns = @JoinColumn(name = "service_id"), inverseJoinColumns = @JoinColumn(name = "software_id"))
    private List<Software> softwareList;

    @ManyToMany
    @JoinTable(name = "service_subdatabase", joinColumns = @JoinColumn(name = "service_id"), inverseJoinColumns = @JoinColumn(name = "subdatabase_id"))
    private List<Subdatabase> subdatabaseList;

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

    public SoftwareStack getSoftwareStack() {
        return softwareStack;
    }

    public void setSoftwareStack(SoftwareStack softwareStack) {
        this.softwareStack = softwareStack;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public List<Software> getSoftwareList() {
        return softwareList;
    }

    public void setSoftwareList(List<Software> softwareList) {
        this.softwareList = softwareList;
    }

    public List<Subdatabase> getSubdatabaseList() {
        return subdatabaseList;
    }

    public void setSubdatabaseList(List<Subdatabase> subdatabaseList) {
        this.subdatabaseList = subdatabaseList;
    }
}
