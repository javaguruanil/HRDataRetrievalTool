package com.nisum.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.nisum.dao.DBTablesColumns;

@Configuration
public class GettingTableColumns {
	@Autowired
	private DBTablesColumns dbTablesColumns;

	public Map<String, Map<String, List<String>>> getTableCols(String sName) {

		// Store final tables like parent and child 
		Map<String,Map<String, List<String>>> userMap = new TreeMap<String,Map<String, List<String>>>();
		
		// Store or contains Referenced Tables or Parent Tables 
		Map<String,List<String>> parentTables = new TreeMap<String, List<String>>();
		
		// Store or contains child Tables 
		Map<String,List<String>> childTables  = new TreeMap<String, List<String>>();
		
		
		// Getting all "Tables" from Particular "schema"

		List<String> tableNames = dbTablesColumns.getTables(sName);

		// Preparing "Properties" object to get ignoring.properties file all "keys"
		Properties p = new Properties();
		Enumeration enumeration = null;
		try {
			p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("ignoring.properties"));
			enumeration = p.propertyNames();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Getting ignoring table names from "xxxxx.properties" file
		String ignoringTableName = (String) p.get("table");

		// checking whether "table" key exists or not
		if (ignoringTableName != null) {
			String itn[] = ignoringTableName.split(",");
			for (String s1 : itn) {
				
				// removing spaces if exists
				s1 = s1.trim();
				if (tableNames.contains(s1)) {

					// removing "particular table" from "tableNames" except "employee" table [i.e
					// but not "employee" table]
					if (!s1.equals("employee")) {
						tableNames.remove(s1);
					}
				}
			}
		}

		
		for (String tN : tableNames) {

			// Getting all "Columns" from Particular "table"
			List<String> listColumns = dbTablesColumns.getColumns(sName, tN);

			// Getting "keys" from enumeration object
			while (enumeration.hasMoreElements()) {
				String tableName = (String) enumeration.nextElement();
				tableName = tableName.trim();
				if (tableName.equals(tN)) {
					String col = p.getProperty(tableName);
					if (col != null) {
						String ignoreCol[] = col.split(",");
						for (String str : ignoreCol) {
							
							// removing spaces if exists
							str = str.trim();
							// removing columns from "listColumns" except "employee" table's "emp_id"
							if (!(tN.equals("employee") && str.equals("emp_id"))) {
								listColumns.remove(str);
							}
						}
					}
				}

			}

			
			// Checking 'tN' : has parent table or not, if no parent then it is Referenced or Parent table O.W child table
			List<Object[]>  parentsOrChildren =dbTablesColumns.getParentTables("hrportaldb_drt", tN);
			
			if(parentsOrChildren.size() > 0) {   //<---- Parent table(s) exists so this table is not parent i.e it is child table
				childTables.put(tN, listColumns);
			}
			else {
				parentTables.put(tN, listColumns);
			}
			
			// Setting cursor for keys
			enumeration = p.propertyNames();

		} // for(String tN:
		
		userMap.put("parent", parentTables);
		userMap.put("child", childTables);
		return userMap;
	}
}
