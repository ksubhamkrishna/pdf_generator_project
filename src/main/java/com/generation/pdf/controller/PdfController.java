package com.generation.pdf.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.pdf.dao.PDFGenerationDao;
import com.generation.pdf.model.TransactionReceiptModel;
import com.generation.pdf.service.TransactionReceiptPDFGenerator;

@RestController
@RequestMapping("/pdf")
public class PdfController {

	@Autowired
	private PDFGenerationDao pdfGenerationDao ;

    // Endpoint to generate the PDF
    @GetMapping("/generate")
    public ResponseEntity<String> generatePdf() {
        String filePath = "C:/Users/subham.krishna/Desktop/TransactionReceipt.pdf"; // Ensure file ends with .pdf
        List<TransactionReceiptModel> transactions = pdfGenerationDao.getAllTransactions();
        Long transactionCount = pdfGenerationDao.getTransactionCount();
        Long debittransactionCount = pdfGenerationDao.getdebitTransactionCount();
        Long credittransactionCount = pdfGenerationDao.getcreditTransactionCount();
        TransactionReceiptPDFGenerator transactionReceiptPDFGenerator = new TransactionReceiptPDFGenerator();
        BigDecimal creditTotal = pdfGenerationDao.getCreditTransaction();
        
        BigDecimal debitTotal = pdfGenerationDao.getDebitTransaction();
        transactionReceiptPDFGenerator.generatePdf(filePath,transactions,transactionCount,debittransactionCount,credittransactionCount,  creditTotal, debitTotal);
        return ResponseEntity.ok("PDF generated successfully at " + filePath);
    }
}