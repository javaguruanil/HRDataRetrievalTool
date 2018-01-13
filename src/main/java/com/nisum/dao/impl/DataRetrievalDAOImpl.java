package com.nisum.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.nisum.dao.DBTablesColumns;
import com.nisum.dao.DataRetrievalDAO;

@Service
public class DataRetrievalDAOImpl implements DataRetrievalDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DBTablesColumns dbTablesColumns;

	@Override
	public List<Map<String, Object>> getFileterdResponseData(Map<String, List<String>> requestMap) {
		List<Map<String,Object>> output = new ArrayList<Map<String, Object>>();
		String sqlQuery = null;
		List<String> joinParticipatedTables = new ArrayList<String>();  //  ---> stores join participated tables for writing "Join query's 'selectClause' "
		Set<String> queryParticipatedTables = new HashSet<String>();    //  ---> stores tables participating in join query [hidden logic of this is to find "queryNotParticipatedTables" and write plain select queries] 
		Set<String> onClauseJoinClauseTables = new HashSet<String>();   //  ---> stores onClause and JoinClause tables used for writing 'whereClause' to join columns i.e Left = Right
        List<Object[]> noOnClauseJoinClauseTables = new ArrayList<Object[]>();// ---> stores tables which are eligible for join query but these tables are not in proper order for writing join 'whereClause' and 'selectClause' 
		String joinPhrase1 = " left join ";
		String joinPhrase2 = " on ";
		String queryWhereClause ="";
		String querySelectClause="";
		String finalJoinQuery       ="";
		String finalNonJoinQuery    ="";
		String joinColumnPhrase     = "";
		int lastIndex =0;
		Set set = requestMap.entrySet();
		Iterator itr = set.iterator();
		List<String> uiTables= new ArrayList<String>();
		while(itr.hasNext()) {
			Entry<String, List<String>> entry =(Entry<String, List<String>>)itr.next();
			String tableName=entry.getKey();
			uiTables.add(tableName);
		}
		
		// making onClauseJoinClauseTable initial value as "employee"
		onClauseJoinClauseTables.add("employee");
		for(String tab: uiTables) {   //---> Considering all UI selected tables
                  List<Object[]>  listObj =dbTablesColumns.getParentTables("hrportaldb_drt", tab);
                  if(listObj.size() > 0) {   // ---> Parent Table exists
                	  for(Object[] ob :listObj) {
                		  if(uiTables.contains(ob[2].toString())) {  // ---> considering only ui selected Parent Tables
	                		  queryParticipatedTables.add(ob[2].toString());
	                		  
	                		  // Logic to write "join condition columns" for string format query
	                		  
	                		  // here we use constraint_name to get columns
	                		  List<Object[]>  listObjArr =dbTablesColumns.getChildParentColumns("hrportaldb_drt", ob[2].toString(), ob[0].toString());
	                		  String childCol=null,parentCol=null;
	                		  
	                		  Object[] obj1 =listObjArr.get(0);
	                		  childCol =obj1[1].toString();
                			  parentCol=obj1[4].toString();
                			  
                			  
                			  if(obj1[1].toString().equals("key")) {
                				  childCol= "`"+obj1[1].toString()+"`";
                			  }
                			  if(obj1[4].toString().equals("key")) {
                				  parentCol= "`"+obj1[4].toString()+"`";
                			  }
                			  
                			  if(obj1[1].toString().equals("type")) {
                				  childCol = "`"+obj1[1].toString()+"`";
                			  }
                			  
                			  if(obj1[4].toString().equals("type")) {
                				  parentCol= "`"+obj1[4].toString()+"`";
                			  }
                			  
                    		  joinColumnPhrase="";
                    		  String columnPreparation = "";
	                		  if((onClauseJoinClauseTables.contains(obj1[0])== true && onClauseJoinClauseTables.contains(obj1[3]) == false ) ||(onClauseJoinClauseTables.contains(obj1[0])== false && onClauseJoinClauseTables.contains(obj1[3]) == true )) {
	                			  onClauseJoinClauseTables.add(obj1[0].toString());
	                			  onClauseJoinClauseTables.add(obj1[3].toString());
	                			  
	                			  // join clause preparation
	                			  joinColumnPhrase=  joinPhrase1+" "+obj1[0]+"   "+joinPhrase2+"  "+obj1[0]+"."+childCol+" = "+obj1[3]+"."+parentCol;
	                			  
	                			  if(!joinParticipatedTables.contains(obj1[0])) {    // ---> querySelectClause preparation for child tables
	                			    joinParticipatedTables.add(obj1[0].toString());
	                			    List<String> listColumn=requestMap.get(obj1[0]);
		                			   for(String col:listColumn) {
		                				   if(col.equals("key")) {
		                					   col = "`"+col+"`";
		                				   }
		                				   
		                				   if(col.equals("type")) {
		                					   col = "`"+col+"`";
		                				   }
		                				   
		                				   columnPreparation=columnPreparation+" "+obj1[0].toString()+"."+col+" , ";
		                			   }
		                			   querySelectClause =querySelectClause+" "+columnPreparation;
	                			  }
	                			  
	                			  columnPreparation = "";
	                			  if(!joinParticipatedTables.contains(ob[2])) {     // ---> querySelectClause preparation for Parent tables
		                			    joinParticipatedTables.add(ob[2].toString());
		                			    joinParticipatedTables.add(ob[2].toString());
			                			   List<String> listColumn=requestMap.get(ob[2]);
		                                   for(String col:listColumn) {
		                                	   if(col.equals("key")) {
			                					   col = "`"+col+"`";
			                				   }
			                				   
			                				   if(col.equals("type")) {
			                					   col = "`"+col+"`";
			                				   }
			                				   
		                                	   columnPreparation=columnPreparation+" "+ob[2].toString()+"."+col+" , ";
			                			   }
		                                   
		                                   if(ob[2].toString().equals("employee")) {  // ----> making "employee" columns to at beginning
		                                	   querySelectClause = " "+columnPreparation+" "+ querySelectClause;
		                                   }
		                                   else {
		                                	   querySelectClause =querySelectClause+" "+columnPreparation;   
		                                   }
		                                   
		                		  }
		                			  
	                			 
	                		  }
	                		  else {

	                			  Object [] objArr= new Object[4];
                                  objArr[0]=obj1[0];   // ----> contains child table
                              
                                  objArr[1]=obj1[0]+"."+childCol;
                                  objArr[2]=obj1[3];   // ----> contains parent table
                                  
                                  objArr[3]=obj1[3]+"."+parentCol;

                                  noOnClauseJoinClauseTables.add(objArr);
	                		  }
	                		  
	                		  queryWhereClause=queryWhereClause+" "+joinColumnPhrase;

                		  }
                	  }
                	  queryParticipatedTables.add(tab);
                  }
		}

		
        // Finding "queryNotParticipatedTables" using "queryParticipatedTables"		
		Set<String>  queryNotParticipatedTables = new HashSet<String>();
		for(String table :uiTables ) {
			if(!queryParticipatedTables.contains(table)) {
				queryNotParticipatedTables.add(table);
			}
		}
		
		// removing , [comma] at end from "querySelectClause"
		
		if(querySelectClause.length() > 0) {
			lastIndex=querySelectClause.lastIndexOf(",");
			querySelectClause =querySelectClause.substring(0, lastIndex);
		}
		
		
		//No sequence tables in Join query from UI tables [i.e These are eligible for join query but they are not in proper sequence in join query that's y we are doing below logic]
				for(Object objArr[] :noOnClauseJoinClauseTables) {
				    if(onClauseJoinClauseTables.contains(objArr[0].toString())) {
		               queryWhereClause = queryWhereClause+" "+joinPhrase1+" "+objArr[2]+"  "+joinPhrase2+"  "+objArr[3]+" = "+objArr[1];  // --> preparing 'whereclause' for joinQuery
		               querySelectClause= querySelectClause+" , "+objArr[3];
				    }
				    else if(onClauseJoinClauseTables.contains(objArr[2].toString())) {
				    	queryWhereClause = queryWhereClause+" "+joinPhrase1+" "+objArr[0]+" "+joinPhrase2+"  "+objArr[1]+" = "+objArr[3];  // --> preparing 'whereclause' for joinQuery
			            querySelectClause= querySelectClause+" , "+objArr[1];
				    }
				}
		
		// Preparing final query for parent and child tables
		if(!queryParticipatedTables.isEmpty()) {
			finalJoinQuery = "select "+querySelectClause+"  from  employee "+queryWhereClause;
			
		}
		
		// Preparing query for Independent tables [i.e no parent and child relation tables or "queryNotParticipatedTables" 
		// or writing plain select queries for UI tables but not participated in the join query]
		if(!queryNotParticipatedTables.isEmpty()) {   
			for(String table : queryNotParticipatedTables) {  // --> Getting each table
						String query="select  ";
						List<String> listCol=requestMap.get(table);   // --> Getting UI tables["queryNotParticipatedTables"] corresponding columns using 
						                                              //     method input or argument. 
						for(String col :listCol) {
							query =query+" "+table+"."+col+" ,";
						}
						
						// removing comma at end
						lastIndex =query.lastIndexOf(',');
						query =query.substring(0, lastIndex);
						
						// preparing from clause
						query = query+"  from "+table;
						
						List<Map<String, Object>> independentOutput = null;
						
						// Logic to get only Referenced Tables data 
						if(queryParticipatedTables.isEmpty() && queryNotParticipatedTables.size() > 1) {
							   if(!table.equals("employee")) {	
								   System.out.println(query);
								   independentOutput=jdbcTemplate.queryForList(query);
							   }
							   
						}
						else if(queryParticipatedTables.isEmpty() && queryNotParticipatedTables.size() == 1 && table.equals("employee")) {
							   System.out.println(query);
							   independentOutput=jdbcTemplate.queryForList(query);
						}
						
						if(independentOutput != null) {
							   for(Map<String, Object>  m:independentOutput) {
					            	output.add(m);
					           }
						}	   
			}
		}
		else {
			System.out.println(finalJoinQuery);
			List<Map<String, Object>> joinOutput=jdbcTemplate.queryForList(finalJoinQuery);
			for(Map<String, Object>  m:joinOutput) {
            	output.add(m);
            }
		}
	
		/*List<String> tableNamesFromUi = new ArrayList<String>();
		StringBuffer sqlQuery = new StringBuffer(" SELECT ");
		for (Entry<String, List<String>> tableColumnEntryInfo : requestMap.entrySet()) {

			// Storing Ui table names into List
			String tName = tableColumnEntryInfo.getKey();
			tableNamesFromUi.add(tName);

			for (String columnName : tableColumnEntryInfo.getValue()) {
				sqlQuery.append(tName + '.' + columnName + ", ");
			}
		}

		int index = sqlQuery.lastIndexOf(",");
		sqlQuery = new StringBuffer(sqlQuery.substring(0, index));

		String joinBaseTable = " employee ";
		String joinPhrase = " left join ";

		sqlQuery.append(" from  " + joinBaseTable);

		// Common Tables from DB and UI
		List<String> commonTables = new ArrayList<String>();

		// finding child tables for Ui tables
		for (String uiTable : tableNamesFromUi) {
			ReferencedTablesColumns referencedTablesColumns = null;
			List<ReferencedTablesColumns> listOfReferenceTablesColumns = new ArrayList<ReferencedTablesColumns>();

			List<Object[]> obj = dbTablesColumns.getChildColumnUsageForParticularTable("hrportaldb_drt", uiTable);

			for (Object obj1[] : obj) {
				referencedTablesColumns = new ReferencedTablesColumns();
				referencedTablesColumns.setChildTable(obj1[0].toString());
				referencedTablesColumns.setChildColumn(obj1[1].toString());
				referencedTablesColumns.setConstraintName(obj1[2].toString());
				referencedTablesColumns.setParentTable(obj1[3].toString());
				referencedTablesColumns.setParentColumn(obj1[4].toString());

				// Adding to the List
				// By checking whether "CHILD TABLES" are present in the UI tables or not
				if (tableNamesFromUi.contains(referencedTablesColumns.getChildTable())) {
					listOfReferenceTablesColumns.add(referencedTablesColumns);
				}

				if (tableNamesFromUi.contains(referencedTablesColumns.getParentTable())) {

				}
			}

			// Preparing "left join" columns
			for (ReferencedTablesColumns ref : listOfReferenceTablesColumns) {
				sqlQuery.append("  " + joinPhrase + "  " + ref.getChildTable() + " on " + uiTable + "."
						+ ref.getParentColumn() + "=" + ref.getChildTable() + "." + ref.getChildColumn());
			}
		}

		
		 int index = sqlQuery.lastIndexOf(","); sqlQuery = new
		 StringBuffer(sqlQuery.substring(0, index));
		
		 Set<String> tableNames = requestMap.keySet();
		 
		 String joinTable = "employee"; String commonColumnName = ".emp_key";
		 
		 for (String tableName : tableNames) {
		 
		 if (tableName.equals("employee")) sqlQuery.append(" from " + tableName +
		 " as " + tableName); else {
		 
		 sqlQuery.append(" left join " + tableName + " as " + tableName);
		 
		 if (joinTable.equals("employee")) sqlQuery.append(" ON " + tableName +
		 commonColumnName + " = " + joinTable + ".key "); else sqlQuery.append(" ON "
		 + tableName + commonColumnName + " =" + joinTable + commonColumnName);
		 
		 joinTable = tableName; } } System.out.println(sqlQuery); return
		 jdbcTemplate.queryForList(sqlQuery.toString());
		 
		 
		for (String s : commonTables) {
			if (!tableNamesFromUi.contains(s)) {
				System.out.println(s + " table not refered at DB");
			}
		}*/

        for(Map<String, Object> m:output) {
        	System.out.println(m);
        }
		return output;
	}

}
