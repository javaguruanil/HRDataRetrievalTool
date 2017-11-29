package com.nisum.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nisum.dao.DataRetrievalDAO;
import com.nisum.service.EmailAccount;
import com.nisum.service.GettingTableColumns;
import com.nisum.util.DataRetrievalUtil;
import com.nisum.util.MailSender;

@RestController
public class DataRetrievalControlller {

	@Autowired
	DataRetrievalDAO dataRetrievalDAO;

	@Autowired
	private EmailAccount emailAccount;

	@Autowired
	private GettingTableColumns gettingTableColumns;

	@RequestMapping(value = "/getTableColumnInfo")
	public Map<String, List<String>> getTableColumnInfo() {
		// return DataRetrievalUtil.getTableColumnInfo();
		Map<String, List<String>> mapTableCol = gettingTableColumns.getTableCols("hrportaldb");
		return mapTableCol;
	}

	@RequestMapping(value = "/submitColumnInfo")
	public List<Map<String, Object>> submitColumnData(@RequestBody Map<String, List<String>> requestMap) {
		System.out.println("Request>>>>" + requestMap);
		return dataRetrievalDAO.getFileterdResponseData(requestMap);
	}

	/**
	 * sendMail
	 * 
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendMail", method = RequestMethod.POST)
	public ResponseEntity<?> sendMail(@RequestBody Map<String, Object> requestMap) throws Exception {
		JSONObject jsonObj = new JSONObject();
		try {

			String emailId = requestMap.get("email").toString();
			String mailSubject = requestMap.get("subject").toString();
			List<Map<String, Object>> dataList = (List<Map<String, Object>>) requestMap.get("dataList");

			MailSender.sendEmail(emailAccount.getAdminemail(), emailAccount.getAdminpassword(), emailId, null,
					mailSubject, "Thank you for using this tool for generating test data.", dataList);
			System.out.println(requestMap);

			jsonObj.put("result", "Mail Sent to " + emailId);
			ResponseEntity<String> res = new ResponseEntity<String>(jsonObj.toString(), HttpStatus.OK);
			return res;
		} catch (Exception e) {
			System.err.println("Error in DataRetrievalControlller :: sendMail " + e.getMessage());
			e.printStackTrace();
			jsonObj.put("error", e.getMessage());
			ResponseEntity<String> res = new ResponseEntity<String>(jsonObj.toString(),
					HttpStatus.INTERNAL_SERVER_ERROR);
			return res;
		}

	}

}
