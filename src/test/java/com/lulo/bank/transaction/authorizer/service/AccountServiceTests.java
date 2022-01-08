package com.lulo.bank.transaction.authorizer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lulo.bank.transaction.authorizer.domain.Constants;
import com.lulo.bank.transaction.authorizer.dto.AccountRequest;
import com.lulo.bank.transaction.authorizer.dto.AccountResponse;
import com.lulo.bank.transaction.authorizer.dto.ApiResponse;
import com.lulo.bank.transaction.authorizer.repository.IAccountRepository;

class AccountServiceTests {
	
	@InjectMocks
	private AccountService accountService;
	
	@Mock	
	private IAccountRepository accountRepository;
	
	private AccountRequest accountRequest;
	
	private AccountResponse accountResponse;
	
	@BeforeEach
	void setUp() 
	{ 
		MockitoAnnotations.openMocks(this);
		
		accountRequest = new AccountRequest();
		accountRequest.setId(1L);
		accountRequest.setActiveCard(true);
		accountRequest.setAvailableLimit(100);
		
		accountResponse = new AccountResponse();
		accountResponse.setActiveCard(true);
		accountResponse.setAvailableLimit(100);
	}

	@Test
	void registerAccount() {
		ApiResponse response = accountService.registerAccount(accountRequest); 
		assertNotNull(response);
		assertEquals(accountResponse.getActiveCard(), response.getAccount().getActiveCard());
		assertEquals(accountResponse.getAvailableLimit(), response.getAccount().getAvailableLimit());
	}
	
	@Test
	void registerExistingAccount() {
		
		when(accountRepository.existsById(1L)).thenReturn(true);
		ApiResponse response = accountService.registerAccount(accountRequest);
				
		assertTrue(response.getViolations().contains(Constants.ACCOUNT_ALREADY_INITIALIZED));
	}
}
