package com.lulo.bank.transaction.authorizer.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionRequest
{

	private String merchant;
	
	private Integer amount;
	
	private LocalDateTime time;
	
}