package de.ikano.hedwig.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"host","port","objectName","accessMethod","accessProperty"}))
public class Target extends BasicEntity implements Serializable {

    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false, unique=true)
    @NotNull 
    @Size(min = 1, max = 25)
    private String name;
    
    @NotNull
    @NotEmpty
    private String objectName;

    @NotNull
    @NotEmpty
    private String host;
    
    @NotNull @Min(0) @Max(9999)
    private Integer port;
    
    @NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z0-9]*", message = "must contain only letters and numbers")
    private String registryName;
    
    private String accessMethod;
    
    private String accessProperty;
    
    @Enumerated(EnumType.STRING)
    private TargetStatus status = TargetStatus.UNKNOWN;
    

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public TargetStatus getStatus() {
        return status;
    }

    public void setStatus(TargetStatus status) {
        this.status = status;
    }

    public String getAccessMethod() {
        return accessMethod;
    }

    public void setAccessMethod(String accessMethod) {
        this.accessMethod = accessMethod;
    }

    public String getAccessProperty() {
        return accessProperty;
    }

    public void setAccessProperty(String accessProperty) {
        this.accessProperty = accessProperty;
    }
    
    @Transient
    public boolean isAccessViaMethod() {
        return this.accessMethod != null;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
    
    public String getRegistryName() {
		return registryName;
	}
    public void setRegistryName(String registryName) {
		this.registryName = registryName;
	}
    
    @Override
    public String toString() {
    	return this.id + "_" + this.name; 
    }
}