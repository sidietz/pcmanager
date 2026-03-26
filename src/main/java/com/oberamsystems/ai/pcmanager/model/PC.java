package com.oberamsystems.ai.pcmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class PC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String macAddress;
    private BigDecimal basePrice;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime boughtAt;

    @OneToMany(mappedBy = "pc")
    private List<Component> components;

    @ManyToMany
    @JoinTable(name = "pc_appliance", joinColumns = @JoinColumn(name = "pc_id"), inverseJoinColumns = @JoinColumn(name = "appliance_id"))
    private List<Appliance> appliances;

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

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public LocalDateTime getBoughtAt() {
        return boughtAt;
    }

    public void setBoughtAt(LocalDateTime boughtAt) {
        this.boughtAt = boughtAt;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<Appliance> getAppliances() {
        return appliances;
    }

    public void setAppliances(List<Appliance> appliances) {
        this.appliances = appliances;
    }

    /**
     * Calculates the price dynamicaly based on sum of component prices.
     * If no components exist, returns the base price.
     */
    public BigDecimal getPrice() {
        if (components != null && !components.isEmpty()) {
            return components.stream()
                    .map(c -> c.getPrice() != null ? c.getPrice() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return basePrice != null ? basePrice : BigDecimal.ZERO;
    }
}
