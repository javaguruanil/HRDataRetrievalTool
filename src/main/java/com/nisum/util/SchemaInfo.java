package com.nisum.util;

import javax.persistence.Entity;
import javax.persistence.Id;

/*
 * This class is used for passing as first generic argument[input] to  "JpaRepository" interface
 * There is not technical usage with this class.
 */
@Entity
public class SchemaInfo {
	
	@Id
	private long key;

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}
}
