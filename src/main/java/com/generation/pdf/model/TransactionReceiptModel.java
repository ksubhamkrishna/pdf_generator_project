package com.generation.pdf.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "Transactions_receipt")
public class TransactionReceiptModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Adjust strategy as per your database
	@Column(name = "Transfer_Reference",nullable = false, unique= true)
	private Long transferReference;

	@Column(name = "Date", updatable = false, insertable = false)
	private Timestamp date;

	@Column(name = "Payor_Account", nullable = false)
	private Long payorAccount;

	@Column(name = "Assigned_id")
	private Integer assignedId;

	@Column(name = "Assigned_Transfer_id")
	private Long assignedTransferId;

	@Column(name = "Description", length = 10000)
	private String description;

	@Column(name = "Transfer_Account", precision = 20, scale = 2)
	private BigDecimal transferAccount;

	@Column(name = "Credit_Type", length = 6)
	private String creditType;

	// Getters and Setters

	public Long getTransferReference() {
		return transferReference;
	}

	public void setTransferReference(Long transferReference) {
		this.transferReference = transferReference;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Long getPayorAccount() {
		return payorAccount;
	}

	public void setPayorAccount(Long payorAccount) {
		this.payorAccount = payorAccount;
	}

	public Integer getAssignedId() {
		return assignedId;
	}

	public void setAssignedId(Integer assignedId) {
		this.assignedId = assignedId;
	}

	public Long getAssignedTransferId() {
		return assignedTransferId;
	}

	public void setAssignedTransferId(Long assignedTransferId) {
		this.assignedTransferId = assignedTransferId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getTransferAccount() {
		return transferAccount;
	}

	public void setTransferAccount(BigDecimal transferAccount) {
		this.transferAccount = transferAccount;
	}

	public String getCreditType() {
		return creditType;
	}

	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}

	
	
	public TransactionReceiptModel() {
		super();
	}

	public TransactionReceiptModel(Long payorAccount, Integer assignedId, Long assignedTransferId, String description,
			BigDecimal transferAccount, String creditType) {
		super();
		this.payorAccount = payorAccount;
		this.assignedId = assignedId;
		this.assignedTransferId = assignedTransferId;
		this.description = description;
		this.transferAccount = transferAccount;
		this.creditType = creditType;
	}
	
	
}
