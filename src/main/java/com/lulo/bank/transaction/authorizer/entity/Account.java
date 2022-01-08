package com.lulo.bank.transaction.authorizer.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ACCOUNT")
public class Account 
{
	@Id
	@Column(name = "ID", nullable = false, updatable = false)
	private Long id;

	@Column(name = "ACTIVE_CARD", nullable = false)
	private Boolean activeCard;
	
	@Column(name = "AVAILABLE_LIMIT", nullable = false)
	private Integer availableLimit;	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "account", orphanRemoval = true, fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Transaction> transactions;
}