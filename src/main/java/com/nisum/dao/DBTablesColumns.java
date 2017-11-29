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
	public List<String> getColumns(@Param("shema")String schema, @Param("tableName")String tableName);

}
