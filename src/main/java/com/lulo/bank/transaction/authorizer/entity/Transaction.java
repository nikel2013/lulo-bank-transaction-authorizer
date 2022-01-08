package com.lulo.bank.transaction.authorizer.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TRANSACTION")
public class Transaction 
{
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne	
	private Account account;

	@Column(name = "MERCHANT", nullable = false)
	private String merchant;
	
	@Column(name = "AMOUNT", nullable = false)
	private Integer amount;
	
	@Column(name = "TIME", nullable = false)
	private LocalDateTime time;
}
