package org.example.model;

import jakarta.persistence.*;
import java.util.Objects;

@SuppressWarnings("unused")
@Entity
@Table(name = "jets")
public class Jet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String manufacturer;
    
    @Column(nullable = false)
    private Double maxSpeed;
    
    @Column(nullable = false)
    private Integer range;
    
    @Column
    private String armament;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    // Getters and setters for all fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    
    public Double getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(Double maxSpeed) { this.maxSpeed = maxSpeed; }
    
    public Integer getRange() { return range; }
    public void setRange(Integer range) { this.range = range; }
    
    public String getArmament() { return armament; }
    public void setArmament(String armament) { this.armament = armament; }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}