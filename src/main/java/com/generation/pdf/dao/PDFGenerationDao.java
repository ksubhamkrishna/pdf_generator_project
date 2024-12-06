package com.generation.pdf.dao;

import java.util.List;

import javax.transaction.Transaction;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import com.generation.pdf.model.TransactionReceiptModel;

@Repository
@Transactional
public class PDFGenerationDao {

	@Autowired
	private SessionFactory sessionFactory;

	public List<TransactionReceiptModel> getAllTransactions() {

		Session session = sessionFactory.getCurrentSession();
		List<TransactionReceiptModel> list = session
				.createQuery("SELECT u FROM TransactionReceiptModel u", TransactionReceiptModel.class).getResultList();

		return list;

	}

	public TransactionReceiptModel saveTransactionReceipt(@RequestBody TransactionReceiptModel receiptModel) {

		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(receiptModel);

		return receiptModel;

	}

}
