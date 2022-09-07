package com.interchecks.review.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data

/**
 * Baseline class for all tables that have an identity / "id" field 
 *
 */
public class IdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    public boolean isNew() { 
    	return (null==id);
    }
    
}
