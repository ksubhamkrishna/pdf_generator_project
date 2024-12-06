package com.generation.pdf.service;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import com.generation.pdf.dao.PDFGenerationDao;
import com.generation.pdf.model.TransactionReceiptModel;

import java.awt.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class TransactionReceiptPDFGenerator {

	private final PDFGenerationDao pdfGenerationDao;

	public TransactionReceiptPDFGenerator(PDFGenerationDao pdfGenerationDao) {
		this.pdfGenerationDao = pdfGenerationDao;
	}

	public void generatePdf(String filepath) {
		try (PDDocument document = new PDDocument()) {
			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			PDPageContentStream contentStream = new PDPageContentStream(document, page);	

			// Title
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
			contentStream.newLineAtOffset(200, 750);
			contentStream.showText("Transaction Receipt Report");
			contentStream.endText();

			// Subtitle
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA, 12);
			contentStream.newLineAtOffset(200, 730);

			contentStream.showText(
					"Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
			contentStream.endText();
			
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
		    contentStream.newLineAtOffset(25, 710);
		    contentStream.showText("Transfer Summary");
		    contentStream.endText();

			// Table Headers
			String[] headers = { "Date", "Payor Account", "Assigned ID", "Description", "Transfer Amount",
					"Credit Type" };
			drawTableHeader(contentStream, headers, 680, 500);

			// Fetch data from the database
			List<TransactionReceiptModel> transactions = pdfGenerationDao.getAllTransactions();
			int yPosition = 660; // Start position for data rows

			// Iterate through each transaction and write data into the PDF
			for (TransactionReceiptModel transaction : transactions) {
				if (yPosition < 50) { // Add a new page if space is insufficient
					contentStream.close();
					page = new PDPage(PDRectangle.A4);
					document.addPage(page);
					contentStream = new PDPageContentStream(document, page);
					yPosition = 750;
					drawTableHeader(contentStream, headers, yPosition, 500);
					yPosition -= 20;
				}

				drawTableRow(contentStream,
						new String[] { formatDate(transaction.getDate()), String.valueOf(transaction.getPayorAccount()),
								transaction.getAssignedId() != null ? transaction.getAssignedId().toString() : "N/A",
								transaction.getDescription() != null ? transaction.getDescription() : "N/A",
								transaction.getTransferAccount() != null ? transaction.getTransferAccount().toString()
										: "N/A",
								transaction.getCreditType() != null ? transaction.getCreditType() : "N/A" },
						yPosition, 500);

				yPosition -= 20;
			}

			contentStream.close();
			document.save(filepath);

			System.out.println("PDF created successfully at: " + filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void drawTableHeader(PDPageContentStream contentStream, String[] headers, int y, int tableWidth)
			throws IOException {
		int xStart = 50; // Left margin
		int colWidth = tableWidth / headers.length; // Equal column width

		// Background for header row
		contentStream.setNonStrokingColor(Color.LIGHT_GRAY);
		contentStream.addRect(xStart, y - 20, tableWidth, 20);
		contentStream.fill();
		contentStream.setNonStrokingColor(Color.BLACK);

		// Draw header text
		contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
		for (int i = 0; i < headers.length; i++) {
			contentStream.beginText();
			contentStream.newLineAtOffset(xStart + (colWidth * i) + 5, y - 15); // Align text to columns
			contentStream.showText(headers[i]);
			contentStream.endText();
		}
	}

	private void drawTableRow(PDPageContentStream contentStream, String[] rowData, int y, int tableWidth)
			throws IOException {
		int xStart = 50; // Left margin
		int colWidth = tableWidth / rowData.length; // Equal column width

		// Draw data for each column
		contentStream.setFont(PDType1Font.HELVETICA, 10);
		for (int i = 0; i < rowData.length; i++) {
			contentStream.beginText();
			contentStream.newLine();
			contentStream.newLineAtOffset(xStart + (colWidth * i) + 5, y - 15); // Align text to columns
			contentStream.newLine();
			contentStream.showText(rowData[i] != null ? rowData[i] : "N/A");
			contentStream.newLine();
			contentStream.endText();
			
		}
	}

	private String formatDate(Timestamp timestamp) {
		if (timestamp == null) {
			return "N/A";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(timestamp);
	}
}
