package com.gog.civilregistry.adoption.entity;
import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@MappedSuperclass
@Data
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

	/*@Version
	@Column(name = "ROW_VERSION")
	private int rowVersion = 0; */

	@PrePersist
	void preInsert() {
		if (this.createdOn == null) {
			this.createdOn = LocalDateTime.now();
		}
		if (this.updatedOn == null) {
			this.updatedOn = LocalDateTime.now();
		}
		
	}

	@PreUpdate
	void preUpdate() {
		this.updatedOn = LocalDateTime.now();

	}
	

	/*public int getRowVersion() {
		return rowVersion;
	}

	public void setRowVersion(int rowVersion) {
		this.rowVersion = rowVersion;
	}*/
	
	
    

	

}