package com.nisum.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nisum.util.DataRetrievalUtil;


public class GeneratePdfReport {

	public static ByteArrayOutputStream generatePDFData(List<Map<String,Object>> dataList,String pdfTitle) throws BadElementException, MalformedURLException, IOException {

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		final String IMG = "src/main/resources/static/images/logo.jpg";
		Image image = Image.getInstance(IMG);
		image.scaleToFit(205, 75);
		try {

			PdfPTable table = null;
			Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
			Paragraph header = new Paragraph(pdfTitle, chapterFont);

			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

			if(!dataList.isEmpty()) {
				Map<String,Object> firstRecord=dataList.get(0);
				int tableColumnSize = firstRecord.keySet().size();
				table = new PdfPTable(tableColumnSize);
				table.setSpacingAfter(50);
				table.setSpacingBefore(50);
				table.setWidthPercentage(90);
				for (String key : firstRecord.keySet()) {
					PdfPCell hcell;
					    hcell = new PdfPCell(new Phrase(key.toString(), headFont));
						hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						hcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
						table.addCell(hcell);
				}
			}

			for (Map<String,Object> map : dataList) {
				
				for (Map.Entry<String, Object> entry : map.entrySet()) {
				    Object cellValue = entry.getValue();
				    if(entry.getKey().contains("date_") || entry.getKey().contains("_date")) {
				    	    if(entry.getValue() !=null) {
				    	      	String date = DataRetrievalUtil.getCurrentTimeAsString("yyyy-MM-dd HH:mm:ss.SSS a", Long.parseLong(entry.getValue().toString()));
				    	      	cellValue = date;
				    	    }
				    }
					PdfPCell cell;
				    cell = new PdfPCell(new Phrase(String.valueOf(cellValue)));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

				}

			}

			PdfWriter.getInstance(document, out);
			document.open();
			document.add(image); 
			document.add(header);
			document.add(table);

			document.close();

		} catch (DocumentException ex) {

			ex.printStackTrace();
		}
		return out;
	}
}