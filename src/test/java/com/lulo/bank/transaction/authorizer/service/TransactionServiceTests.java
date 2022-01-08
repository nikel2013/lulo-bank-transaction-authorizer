package com.lulo.bank.transaction.authorizer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lulo.bank.transaction.authorizer.config.TrasactionProperties;
import com.lulo.bank.transaction.authorizer.domain.Constants;
import com.lulo.bank.transaction.authorizer.dto.ApiResponse;
import com.lulo.bank.transaction.authorizer.dto.TransactionRequest;
import com.lulo.bank.transaction.authorizer.entity.Account;
import com.lulo.bank.transaction.authorizer.repository.IAccountRepository;
import com.lulo.bank.transaction.authorizer.repository.ITransactionRepository;
import com.lulo.bank.transaction.authorizer.utils.DateUtils;

class TransactionServiceTests {
	
	@InjectMocks
	private TransactionService transactionService;
	
	@Mock	
	private IAccountRepository accountRepository;

	@Mock	
	private ITransactionRepository transactionRepository;
	
	@Mock
	private TrasactionProperties transactionProperties;
	
	private TransactionRequest transactionRequest;
	
	private Account account;
	
	
	@BeforeEach
	void setUp() 
	{ 
		MockitoAnnotations.openMocks(this);
		
		transactionRequest = new TransactionRequest();
		transactionRequest.setAmount(20);
		transactionRequest.setMerchant("test");
		transactionRequest.setTime(LocalDateTime.now());
		
		account = new Account();
		account.setId(1L);
		account.setActiveCard(true);
		account.setAvailableLimit(100);
				
		transactionProperties.intervalMinutesByAnyTransaction = 2;
		transactionProperties.intervalMinutesBySimilarTransaction = 2;
		transactionProperties.maxAnyTransactionByInterval = 3;
		transactionProperties.maxSimiliarTransactionByInterval = 1;
	}

	@Test
	void transactionWithAccountNotinitialized() {
		ApiResponse response = transactionService.processTransaction(transactionRequest); 
		assertTrue(response.getViolations().contains(Constants.ACCOUNT_NOT_INITIALIZED));
	}

	@Test
	void transactionWithCardNoActive() {
		when(accountRepository.countByActiveCard(false)).thenReturn(1);
		ApiResponse response = transactionService.processTransaction(transactionRequest); 
		assertTrue(response.getViolations().contains(Constants.CARD_NOT_ACTIVE));
	}

	@Test
	void transactionWithInsufficientLimit() {
		
		account.setAvailableLimit(0);
		when(accountRepository.findByActiveCard(true)).thenReturn(account);
		
		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThan(DateUtils.subtractMinutes(
			transactionRequest.getTime(), 2), transactionRequest.getTime())).thenReturn(0);
		
		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanEqual(transactionRequest.getTime(), DateUtils.addMinutes(
			transactionRequest.getTime(), 2))).thenReturn(0);

		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanAndAmountAndMerchantIgnoreCase(
			DateUtils.subtractMinutes(transactionRequest.getTime(), 2), 
			transactionRequest.getTime(), transactionRequest.getAmount(), transactionRequest.getMerchant())).thenReturn(0);

		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanEqualAndAmountAndMerchantIgnoreCase(transactionRequest.getTime(), 
			DateUtils.addMinutes(transactionRequest.getTime(), 2), 
			transactionRequest.getAmount(), transactionRequest.getMerchant())).thenReturn(0);
		
		ApiResponse response = transactionService.processTransaction(transactionRequest); 
		assertTrue(response.getViolations().contains(Constants.INSUFFICIENT_LIMIT));
	}

	@Test
	void transactionWithHighFrequencySmallInterval() {
		
		when(accountRepository.findByActiveCard(true)).thenReturn(account);
		
		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThan(DateUtils.subtractMinutes(
			transactionRequest.getTime(), 2), transactionRequest.getTime())).thenReturn(3);
		
		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanEqual(transactionRequest.getTime(), DateUtils.addMinutes(
			transactionRequest.getTime(), 2))).thenReturn(3);

		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanAndAmountAndMerchantIgnoreCase(
			DateUtils.subtractMinutes(transactionRequest.getTime(), 2), 
			transactionRequest.getTime(), transactionRequest.getAmount(), transactionRequest.getMerchant())).thenReturn(0);

		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanEqualAndAmountAndMerchantIgnoreCase(transactionRequest.getTime(), 
			DateUtils.addMinutes(transactionRequest.getTime(), 2), 
			transactionRequest.getAmount(), transactionRequest.getMerchant())).thenReturn(0);
		
		ApiResponse response = transactionService.processTransaction(transactionRequest); 
		assertTrue(response.getViolations().contains(Constants.HIGH_FREQUENCY_SMALL_INTERVAL));
	}
	
	@Test
	void transactionWithDubledTransaction() {
		
		when(accountRepository.findByActiveCard(true)).thenReturn(account);
		
		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThan(DateUtils.subtractMinutes(
			transactionRequest.getTime(), 2), transactionRequest.getTime())).thenReturn(0);
		
		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanEqual(transactionRequest.getTime(), DateUtils.addMinutes(
			transactionRequest.getTime(), 2))).thenReturn(0);

		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanAndAmountAndMerchantIgnoreCase(
			DateUtils.subtractMinutes(transactionRequest.getTime(), 2), 
			transactionRequest.getTime(), transactionRequest.getAmount(), transactionRequest.getMerchant())).thenReturn(1);

		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanEqualAndAmountAndMerchantIgnoreCase(transactionRequest.getTime(), 
			DateUtils.addMinutes(transactionRequest.getTime(), 2), 
			transactionRequest.getAmount(), transactionRequest.getMerchant())).thenReturn(1);
		
		ApiResponse response = transactionService.processTransaction(transactionRequest); 
		assertTrue(response.getViolations().contains(Constants.DOUBLED_TRANSACTION));
	}
	
	@Test
	void transactionSuccess() {
		
		when(accountRepository.findByActiveCard(true)).thenReturn(account);
		
		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThan(DateUtils.subtractMinutes(
			transactionRequest.getTime(), 2), transactionRequest.getTime())).thenReturn(0);
		
		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanEqual(transactionRequest.getTime(), DateUtils.addMinutes(
			transactionRequest.getTime(), 2))).thenReturn(0);

		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanAndAmountAndMerchantIgnoreCase(
			DateUtils.subtractMinutes(transactionRequest.getTime(), 2), 
			transactionRequest.getTime(), transactionRequest.getAmount(), transactionRequest.getMerchant())).thenReturn(0);

		when(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanEqualAndAmountAndMerchantIgnoreCase(transactionRequest.getTime(), 
			DateUtils.addMinutes(transactionRequest.getTime(), 2), 
			transactionRequest.getAmount(), transactionRequest.getMerchant())).thenReturn(0);
		 
		assertNotNull(transactionService.processTransaction(transactionRequest));
		assertEquals(2, transactionProperties.intervalMinutesByAnyTransaction);
		assertEquals(2, transactionProperties.intervalMinutesBySimilarTransaction);
		assertEquals(3, transactionProperties.maxAnyTransactionByInterval);
		assertEquals(1, transactionProperties.maxSimiliarTransactionByInterval);
	}	
}

