package com.generation.pdf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.pdf.service.TransactionReceiptPDFGenerator;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private TransactionReceiptPDFGenerator transactionReceiptPDFGenerator;

    // Endpoint to generate the PDF
    @GetMapping("/generate")
    public ResponseEntity<String> generatePdf() {
        String filePath = "C:/Users/subham.krishna/Desktop/TransactionReceipt.pdf"; // Ensure file ends with .pdf
        transactionReceiptPDFGenerator.generatePdf(filePath);
        return ResponseEntity.ok("PDF generated successfully at " + filePath);
    }
}