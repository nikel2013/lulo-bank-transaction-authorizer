package com.lulo.bank.transaction.authorizer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountRequest 
{
	
	Long id;

	@JsonProperty(value = "active-card")
	Boolean activeCard;
	
	@JsonProperty(value = "available-limit")
	Integer availableLimit;
	
}