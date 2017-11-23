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


		return jdbcTemplate.queryForList(sqlQuery.toString());
	}

}
