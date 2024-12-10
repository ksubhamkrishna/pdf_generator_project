package com.generation.pdf.service;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import com.generation.pdf.model.TransactionReceiptModel;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionReceiptPDFGenerator {

    public void generatePdf(String filepath, List<TransactionReceiptModel> transactions, Long transactionCount, Long creditTransactionCount, Long debitTransactionCount, BigDecimal creditTotal, BigDecimal debitTotal) {
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
            contentStream.showText("Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
            contentStream.endText();

            // Transfer Summary Header
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(50, 710);
            contentStream.showText("Transfer Summary");
            contentStream.endText();

            // Transfer Summary Table
            String[] summaryHeaders = { " ", "Count", "Transfer Amount" };
            drawTableHeader(contentStream, summaryHeaders, 690, 550);
            drawTableRow(contentStream, new String[] { "Credit", String.valueOf(creditTransactionCount), String.valueOf(creditTotal) }, 670, 550);
            drawTableRow(contentStream, new String[] { "Debit", String.valueOf(debitTransactionCount), String.valueOf(debitTotal) }, 650, 550);
            drawTableRow(contentStream, new String[] { "Total", String.valueOf(transactionCount), String.valueOf((creditTotal.add(debitTotal))) }, 630, 550);
            drawTableRow(contentStream, new String[] { "Net Total", String.valueOf(transactionCount), String.valueOf((creditTotal.subtract(debitTotal))) }, 610, 550);

            // Space between tables
            int secondTableStartY = 580;

            // Transaction Details Table
            String[] headers = { "Date", "Payor Account", "Assigned ID", "Description", "Transfer Amount", "Credit Type" };
            drawTableHeader(contentStream, headers, secondTableStartY, 500);

            int yPosition = secondTableStartY - 20; // Start position for data rows
            for (TransactionReceiptModel transaction : transactions) {
                if (yPosition < 50) { // Add a new page if space is insufficient
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = 510;
                    drawTableHeader(contentStream, headers, yPosition, 500);
                    yPosition -= 20;
                }

                drawTableRow(contentStream,
                        new String[] {
                                formatDate(transaction.getDate()),
                                String.valueOf(transaction.getPayorAccount()),
                                transaction.getAssignedId() != null ? transaction.getAssignedId().toString() : "N/A",
                                transaction.getDescription() != null ? transaction.getDescription() : "N/A",
                                transaction.getTransferAmount() != null ? transaction.getTransferAmount().toString() : "N/A",
                                transaction.getCreditType() != null ? transaction.getCreditType() : "N/A"
                        },
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

    // Method to draw table header
    private void drawTableHeader(PDPageContentStream contentStream, String[] headers, int y, int tableWidth)
            throws IOException {
        int xStart = 50; // Left margin
        int colWidth = tableWidth / headers.length;

        // Background for header row
        contentStream.setNonStrokingColor(Color.LIGHT_GRAY);
        contentStream.addRect(xStart, y - 20, tableWidth, 20);
        contentStream.fill();
        contentStream.setNonStrokingColor(Color.BLACK);

        // Draw header text
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        for (int i = 0; i < headers.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(xStart + (colWidth * i) + 5, y - 15);
            contentStream.showText(headers[i]);
            contentStream.endText();
        }

        // Draw vertical lines for columns
        for (int i = 0; i <= headers.length; i++) {
            int xPosition = xStart + (colWidth * i);
            contentStream.moveTo(xPosition, y - 20);
            contentStream.lineTo(xPosition, y);
            contentStream.stroke();
        }

        // Draw horizontal line below header
        contentStream.moveTo(xStart, y - 20);
        contentStream.lineTo(xStart + tableWidth, y - 20);
        contentStream.stroke();
    }

    // Method to draw table row
    private void drawTableRow(PDPageContentStream contentStream, String[] rowData, int y, int tableWidth)
            throws IOException {
        int xStart = 50;
        int colWidth = tableWidth / rowData.length;

        // Draw data for each column
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        for (int i = 0; i < rowData.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(xStart + (colWidth * i) + 5, y - 15);
            contentStream.showText(rowData[i] != null ? rowData[i] : "N/A");
            contentStream.endText();
        }

        // Draw vertical lines for columns
        for (int i = 0; i <= rowData.length; i++) {
            int xPosition = xStart + (colWidth * i);
            contentStream.moveTo(xPosition, y);
            contentStream.lineTo(xPosition, y - 20);
            contentStream.stroke();
        }

        // Draw horizontal line below the row
        contentStream.moveTo(xStart, y - 20);
        contentStream.lineTo(xStart + tableWidth, y - 20);
        contentStream.stroke();
    }

    // Method to format the date
    private String formatDate(Timestamp timestamp) {
        if (timestamp == null) {
            return "N/A";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(timestamp);
    }
}
