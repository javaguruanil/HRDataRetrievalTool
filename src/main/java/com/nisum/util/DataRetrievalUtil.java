package com.nisum.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRetrievalUtil {
	
	private static Map<String, List<String>> tableColumnInfo = new HashMap<String, List<String>>();
	static {
		tableColumnInfo.put("employee", Arrays.asList("key","emp_id","first_name","last_name","date_of_joining","mobile_telephone_number","emp_isactive"));
		tableColumnInfo.put("emp_project", Arrays.asList("project_id","project_name", "empproj_start_date","empproj_isactive"));
		tableColumnInfo.put("employee_pay_scale_info", Arrays.asList("ctc","finacial_year","monthly_pay","basic"));
		
		tableColumnInfo.put("emp_experience", Arrays.asList("company_name","start_date","end_date","total_exp"));
		
		tableColumnInfo.put("emp_contact_info", Arrays.asList("phone_num","emp_key"));
		
	}
	
	public static Map<String, List<String>> getTableColumnInfo(){
		return tableColumnInfo;
		
	}
	
	public static String getCurrentTimeAsString(String format , long timeInlongFormat) {
		SimpleDateFormat dateFormat=new SimpleDateFormat(format);
		Timestamp timestamp=new Timestamp(timeInlongFormat);
		String dateString=dateFormat.format(timestamp);
		return dateString;
	}

}
