package com.lulo.bank.transaction.authorizer.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {
	
	private AccountResponse account;
	
	private List<String> violations;

}
