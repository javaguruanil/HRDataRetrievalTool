package com.nisum.util;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.nisum.service.GeneratePdfReport;

public class MailSender {
	public static  void sendEmail( String sendMail
				       ,String password
				       ,String toMail
				       ,String ccMail
				       ,String emailSubject
				       ,String bodyMsg
				       , List<Map<String,Object>> dataList) throws Exception{

		Properties props = new Properties();
		props.put(Constants.GMAIL_SMTP, Constants.TRUE_FLAG);
		props.put(Constants.GMAIL_START_TLS,  Constants.TRUE_FLAG);
		props.put(Constants.GMAIL_HOST, Constants.GMAIL_HOST_NAME);
		props.put(Constants.GMAIL_PORT, Constants.GMAIL_PORT_NUM);

		Authenticator authenticator = new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sendMail, password);
			}
		};

		Session session = Session.getInstance(props,authenticator);

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sendMail));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toMail));
			if(ccMail!=null && !ccMail.equals("")){
				message.setRecipients(Message.RecipientType.CC,
						InternetAddress.parse(ccMail));
			}
			message.setSubject(emailSubject);
			
//			create MimeBodyPart object and set your message text
			
			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText(bodyMsg);
			
			ByteArrayOutputStream outputStream = null;
			byte[] bytes = new byte[0];
			List<byte[]> pdfContent = new ArrayList<byte[]>();
			outputStream = GeneratePdfReport.generatePDFData(dataList,emailSubject);
			bytes = outputStream.toByteArray();
	        pdfContent.add(bytes);
	        
//			create new MimeBodyPart object and set DataHandler object to this object
	        
			MimeBodyPart messageBodyPart2 = new MimeBodyPart(); 
	        DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
	        messageBodyPart2.setDataHandler(new DataHandler(dataSource));
	        messageBodyPart2.setFileName("TestData.pdf");
	        //construct the mime multi part
	        MimeMultipart mimeMultipart = new MimeMultipart();
	        mimeMultipart.addBodyPart(messageBodyPart1);
	        mimeMultipart.addBodyPart(messageBodyPart2);
			
			message.setContent(mimeMultipart);
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}


	public static String messageBody(String userName){

		StringBuilder sb = new StringBuilder();

		sb.append("<html><head></head><title></title>");
		sb.append("<body style='font-size:12px;font-family:Trebuchet MS;'>");
		sb.append("<i>Hi"+"  "+userName+","+"</i>");
		sb.append("<br><br>");
		sb.append("<i> You have successfully logged into Nisum Portal.</i>");
		sb.append("<br><br>");
		sb.append("<i>This is an auto generated e-mail. Thanking you.</i>");
		sb.append("<br><br>");
		sb.append("<i>Regards,</i>");
		sb.append("<br><br>");
		sb.append("<font color=red><i>Nisum DRTLPortal Application.</i></font>");
		
		return sb.toString();

	}


}  

