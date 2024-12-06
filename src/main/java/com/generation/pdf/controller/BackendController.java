package com.generation.pdf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.pdf.dao.PDFGenerationDao;
import com.generation.pdf.model.TransactionReceiptModel;

@RestController
@RequestMapping("/")
public class BackendController {
	
	@Autowired
	private PDFGenerationDao pdfGenerationDao;
	
	@GetMapping("/getData")
	public List<TransactionReceiptModel> getAllData() {
		
		return pdfGenerationDao.getAllTransactions();
		
	}
	
	@PostMapping("/postData")
	public TransactionReceiptModel postTransactionReceipt(@RequestBody TransactionReceiptModel receipt) {
		
		TransactionReceiptModel obj = pdfGenerationDao.saveTransactionReceipt(receipt);
		
		return obj;
		
	}
	

}
