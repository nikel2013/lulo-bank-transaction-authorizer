package com.lulo.bank.transaction.authorizer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountResponse{
	
	@JsonProperty(value = "active-card")
	private Boolean activeCard;
	
	@JsonProperty(value = "available-limit")
	private Integer availableLimit;
	
}