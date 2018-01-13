package com.nisum.util;

public class ReferencedTable {
	private String childTable;
	private String constraintName;
	private String parentTable;
	public String getChildTable() {
		return childTable;
	}
	public void setChildTable(String childTable) {
		this.childTable = childTable;
	}
	public String getConstraintName() {
		return constraintName;
	}
	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}
	public String getParentTable() {
		return parentTable;
	}
	public void setParentTable(String parentTable) {
		this.parentTable = parentTable;
	}
	@Override
	public String toString() {
		return "[childTable=" + childTable + ", constraintName=" + constraintName + ", parentTable="
				+ parentTable + "]";
	}

}
