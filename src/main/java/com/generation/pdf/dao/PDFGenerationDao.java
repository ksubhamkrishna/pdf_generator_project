package com.generation.pdf.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.generation.pdf.model.TransactionReceiptModel;

@Repository
@Transactional
public class PDFGenerationDao {

    @Autowired
    private SessionFactory sessionFactory;

    // Fetch all transactions
    public List<TransactionReceiptModel> getAllTransactions() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM TransactionReceiptModel", TransactionReceiptModel.class).getResultList();
    }

    // Save or update a transaction receipt
    public TransactionReceiptModel saveTransactionReceipt(TransactionReceiptModel receiptModel) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(receiptModel);
        return receiptModel;
    }

    // Get total transaction count
    public Long getTransactionCount() {
        Session session = sessionFactory.getCurrentSession();
        return (Long) session.createQuery("SELECT COUNT(*) FROM TransactionReceiptModel").uniqueResult();
    }

    public BigDecimal getCreditTransaction() {
        // Obtain the current session
        Session session = sessionFactory.getCurrentSession();

        // HQL query to sum the transfer amount for credit transactions
        BigDecimal totalCredit = (BigDecimal) session
                .createQuery("SELECT SUM(transferAmount) FROM TransactionReceiptModel WHERE creditType = :creditType")
                .setParameter("creditType", "Credit")
                .uniqueResult();

        return totalCredit != null ? totalCredit : BigDecimal.ZERO;
    }


    public BigDecimal getDebitTransaction() {
        // Obtain the current session
        Session session = sessionFactory.getCurrentSession();

        // HQL query to sum the transfer amount for debit transactions
        BigDecimal totalDebit = (BigDecimal) session
                .createQuery("SELECT SUM(transferAmount) FROM TransactionReceiptModel WHERE creditType = :creditType")
                .setParameter("creditType", "Debit")
                .uniqueResult();

        return totalDebit != null ? totalDebit : BigDecimal.ZERO;
    }

	public Long getdebitTransactionCount() {
		
		Session session = sessionFactory.getCurrentSession();
		Long debitcount = (Long)session.createQuery("Select COUNT(*) from TransactionReceiptModel WHERE creditType = : creditType")
				.setParameter("creditType", "Credit")
				.uniqueResult();
		
		return debitcount;
	}

	public Long getcreditTransactionCount() {
		Session session = sessionFactory.getCurrentSession();
		Long creditcount = (Long)session.createQuery("Select COUNT(*) from TransactionReceiptModel WHERE creditType = : creditType")
				.setParameter("creditType", "Debit")
				.uniqueResult();
		
		return creditcount;
	}

}
