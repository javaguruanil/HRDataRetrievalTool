package com.nisum.dao;

import java.util.List;
import java.util.Map;

public interface DataRetrievalDAO {
	
	public List<Map<String, Object>> getFileterdResponseData(Map<String, List<String>> requestMap);

}
