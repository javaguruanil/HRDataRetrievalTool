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
import org.springframework.web.bind.annotation.RequestParam;

import com.nisum.dao.DBTablesColumns;

@Configuration
public class GettingTableColumns {

	@Autowired
	private DBTablesColumns dbTablesColumns;

	public Map<String, List<String>> getTableCols(@RequestParam("shcemaName") String sName) {

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
				s1 = s1.trim();
				if (tableNames.contains(s1)) {

					// removing "particular table" from "tableNames"
					tableNames.remove(s1);
				} // if(tablesNames
			} // for(String
		}//if(ignoring
		
		Map<String, List<String>> usersMap = new TreeMap<String, List<String>>();
		for (String tN : tableNames) {

			// Getting all "Columns" from Particular "table"
			List<String> listColumns = dbTablesColumns.getColumns(sName, tN);

			// Getting "keys" from enumeration object
			while (enumeration.hasMoreElements()) {
				String tableName = (String) enumeration.nextElement();
				tableName = tableName.trim();
				if (tableName.equals(tN)) {
					String col = p.getProperty(tableName);
					if(col != null) {
					String ignoreCol[] = col.split(",");
					 for (String s : ignoreCol) {

					 	// removing columns from "listColumns"
					 	listColumns.remove(s);
					 } // for(String s:ignoreCol
				   }//if(col != null
				} // if(tableName

			} // while(e.hasMoreElements

			// Adding "TableName" and "columns" to "usersMap"
			usersMap.put(tN, listColumns);

			// Setting cursor for keys
			enumeration = p.propertyNames();

		} // for(String
		return usersMap;
	}
}
