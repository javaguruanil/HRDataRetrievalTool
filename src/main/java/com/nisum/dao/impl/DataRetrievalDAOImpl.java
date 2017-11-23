package com.nisum.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.nisum.dao.DataRetrievalDAO;

@Service
public class DataRetrievalDAOImpl implements DataRetrievalDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> getFileterdResponseData(Map<String, List<String>> requestMap) {

		StringBuffer sqlQuery = new StringBuffer(" SELECT ");
		// for(Entry<String, List<String>> tableColumnEntryInfo :
		// requestMap.entrySet()){
		// for(String columnName : tableColumnEntryInfo.getValue()){
		// sqlQuery.append(tableColumnEntryInfo.getKey()+'.'+columnName + ", ");
		// }
		// }
		//
		//
		// Set<String> tableNames = requestMap.keySet();
		//
		//
		// StringBuffer whereCondition = new StringBuffer(" WHERE ");
		// for(String tableName : tableNames){
		// sqlQuery.append(tableName + " as "+ tableName + ", ");
		// if(!tableName.equals("employee"))
		// whereCondition.append(tableName+".emp_key = employee.key AND ");
		// }
		//
		// index = sqlQuery.lastIndexOf(",");
		// sqlQuery = new StringBuffer(sqlQuery.substring(0, index));
		//
		// index = whereCondition.lastIndexOf(" AND ");
		// whereCondition = new StringBuffer(whereCondition.substring(0,
		// index));
		// sqlQuery.append(whereCondition);
		// System.out.println(sqlQuery);

		for (Entry<String, List<String>> tableColumnEntryInfo : requestMap.entrySet()) {
			for (String columnName : tableColumnEntryInfo.getValue()) {
				sqlQuery.append(tableColumnEntryInfo.getKey() + '.' + columnName + ", ");
			}
		}

		int index = sqlQuery.lastIndexOf(",");
		sqlQuery = new StringBuffer(sqlQuery.substring(0, index));

		Set<String> tableNames = requestMap.keySet();

		String joinTable = "employee";
		String commonColumnName = ".emp_key";

		for (String tableName : tableNames) {

			if (tableName.equals("employee"))
				sqlQuery.append(" from " + tableName + " as " + tableName);
			else {

				sqlQuery.append(" left join " + tableName + " as " + tableName);

				if (joinTable.equals("employee"))
					sqlQuery.append(" ON " + tableName + commonColumnName + " = " + joinTable + ".key ");
				else
					sqlQuery.append(" ON " + tableName + commonColumnName + " =" + joinTable + commonColumnName);

				joinTable = tableName;
			}
		}

		System.out.println(sqlQuery);

		// sqlQuery.append("
		// exp.company_name,exp.skills,e.emp_id,e.first_name,ec.p_zip_code from"
		// + " employee as e left join emp_experience as exp on exp.emp_key =
		// e.key "
		// + "left join emp_contact_info as ec on exp.emp_key= ec.emp_key or
		// ec.emp_key=exp.emp_key");

		return jdbcTemplate.queryForList(sqlQuery.toString());
	}

}
