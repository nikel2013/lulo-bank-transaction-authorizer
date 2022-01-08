package com.lulo.bank.transaction.authorizer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lulo.bank.transaction.authorizer.config.TrasactionProperties;
import com.lulo.bank.transaction.authorizer.domain.Constants;
import com.lulo.bank.transaction.authorizer.dto.AccountResponse;
import com.lulo.bank.transaction.authorizer.dto.ApiResponse;
import com.lulo.bank.transaction.authorizer.dto.TransactionRequest;
import com.lulo.bank.transaction.authorizer.entity.Account;
import com.lulo.bank.transaction.authorizer.entity.Transaction;
import com.lulo.bank.transaction.authorizer.repository.IAccountRepository;
import com.lulo.bank.transaction.authorizer.repository.ITransactionRepository;
import com.lulo.bank.transaction.authorizer.utils.DateUtils;

@Service
public class TransactionService implements ITransactionService{
	
	@Autowired
	private TrasactionProperties transactionProperties;
	
	@Autowired	
	private ITransactionRepository transactionRepository;
	
	@Autowired	
	private IAccountRepository accountRepository;

	@Override
	public ApiResponse processTransaction(TransactionRequest request) {
				
		Account activeAccount = accountRepository.findByActiveCard(true);
		List<String> violations = validateTransaction(request, activeAccount); 
		
		if(violations.isEmpty()) 
		{
			Transaction transaction = new Transaction();
			transaction.setAccount(activeAccount);
			transaction.setMerchant(request.getMerchant());
			transaction.setAmount(request.getAmount());
			transaction.setTime(request.getTime());
	
			activeAccount.setAvailableLimit(activeAccount.getAvailableLimit() - request.getAmount());
			
			accountRepository.save(activeAccount);
			transactionRepository.save(transaction);
		}
		
		AccountResponse accountResponse = new AccountResponse();
		accountResponse.setAvailableLimit(activeAccount != null ? activeAccount.getAvailableLimit() : null);
		accountResponse.setActiveCard(activeAccount != null ? activeAccount.getActiveCard() : null);
					
		ApiResponse response = new ApiResponse();
		response.setAccount(accountResponse);
		response.setViolations(violations);
		
		return response;
	}
	
	public List<String> validateTransaction(TransactionRequest request, Account activeAccount)
	{
		List<String> violations = new ArrayList<>();
		
		if(activeAccount == null) 
		{
			if(accountRepository.countByActiveCard(false) > 0) 
			{
				violations.add(Constants.CARD_NOT_ACTIVE);
			}
			else
			{
				violations.add(Constants.ACCOUNT_NOT_INITIALIZED);	
			}
		}
		else 
		{
			if(activeAccount.getAvailableLimit() < request.getAmount()) 
			{
				violations.add(Constants.INSUFFICIENT_LIMIT);
			}
			
			if(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThan(DateUtils.subtractMinutes(
					request.getTime(), transactionProperties.intervalMinutesByAnyTransaction), request.getTime()) +   
    		   transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanEqual(request.getTime(), DateUtils.addMinutes(
    				request.getTime(), transactionProperties.intervalMinutesByAnyTransaction)) >= 
    				transactionProperties.maxAnyTransactionByInterval)
			{
				violations.add(Constants.HIGH_FREQUENCY_SMALL_INTERVAL);
			}
			
			if(transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanAndAmountAndMerchantIgnoreCase(
					DateUtils.subtractMinutes(request.getTime(), transactionProperties.intervalMinutesBySimilarTransaction), 
					request.getTime(), request.getAmount(), request.getMerchant()) +   
    		   transactionRepository.countByTimeGreaterThanEqualAndTimeLessThanEqualAndAmountAndMerchantIgnoreCase(request.getTime(), 
    				DateUtils.addMinutes(request.getTime(), transactionProperties.intervalMinutesBySimilarTransaction), 
    				request.getAmount(), request.getMerchant()) >= transactionProperties.maxSimiliarTransactionByInterval)
			{
				violations.add(Constants.DOUBLED_TRANSACTION);
			}
		}
				
		return violations;
	}
}
