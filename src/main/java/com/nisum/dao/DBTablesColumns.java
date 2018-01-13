package com.nisum.dao;   

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nisum.util.SchemaInfo;

@Repository
@Transactional
public interface DBTablesColumns extends JpaRepository<SchemaInfo, Integer>{
	
	@Query(value = "SELECT table_name FROM information_schema.tables where table_schema=?1", nativeQuery = true)
	public List<String> getTables(@Param("schema") String schema);
	
	@Query(value="select column_name from information_schema.columns where table_schema =?1  and table_name =?2",nativeQuery = true) 
	public List<String> getColumns(@Param("schema")String schema, @Param("tableName")String tableName);

	@Query(value="SELECT constraint_name, TABLE_NAME, REFERENCED_TABLE_NAME " + 
			" FROM information_schema.REFERENTIAL_CONSTRAINTS " + 
			" WHERE CONSTRAINT_SCHEMA = ?1 " + 
			" AND TABLE_NAME = ?2 " ,nativeQuery= true)
	public List<Object[]> getParentTables(@Param("schema")String schema, @Param("tableName")String tableName);
	
	@Query(value ="SELECT " + 
			"	  TABLE_NAME,COLUMN_NAME,CONSTRAINT_NAME, REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME" + 
			"	  FROM	" + 
			"	  INFORMATION_SCHEMA.KEY_COLUMN_USAGE" + 
			"	  WHERE" + 
			"	  REFERENCED_TABLE_SCHEMA = ?1   AND " + 
			"	  REFERENCED_TABLE_NAME   = ?2   AND CONSTRAINT_NAME=?3", nativeQuery =true)
	public List<Object[]>  getChildParentColumns(@Param("schema")String schema, @Param("tableName")String tableName, @Param("constraint_name")String constraint_name);
}
