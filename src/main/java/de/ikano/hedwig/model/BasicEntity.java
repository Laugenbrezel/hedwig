package de.ikano.hedwig.model;

import java.io.Serializable;

public abstract class BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public abstract Long getId();
    
    public boolean isNew() {
    	return (getId() == null);
    }
}
