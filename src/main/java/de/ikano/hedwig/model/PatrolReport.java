package de.ikano.hedwig.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
public class PatrolReport implements Serializable {

    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="time_stamp", updatable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();
    
    @ManyToOne(optional=false)
    @JoinColumn(name="target_fk", updatable=false)
    private Target target;
    
    @Enumerated(EnumType.STRING)
    private TargetStatus status = TargetStatus.UNKNOWN;

    public PatrolReport() {
    }

    public PatrolReport(Target target) {
        this.target = target;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public TargetStatus getStatus() {
        return status;
    }

    public void setStatus(TargetStatus status) {
        this.status = status;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    
}