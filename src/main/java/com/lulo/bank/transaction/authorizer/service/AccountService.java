package com.lulo.bank.transaction.authorizer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lulo.bank.transaction.authorizer.domain.Constants;
import com.lulo.bank.transaction.authorizer.dto.AccountRequest;
import com.lulo.bank.transaction.authorizer.dto.AccountResponse;
import com.lulo.bank.transaction.authorizer.dto.ApiResponse;
import com.lulo.bank.transaction.authorizer.entity.Account;
import com.lulo.bank.transaction.authorizer.repository.IAccountRepository;

@Service
public class AccountService implements IAccountService{
	
	@Autowired	
	private IAccountRepository accountRepository;

	@Override
	public ApiResponse registerAccount(AccountRequest request) {
		
		// Validate Business rules
		List<String> violations = validateAccount(request);
		
		// In case there are no errors
		AccountResponse accountResponse = new AccountResponse();
		if(violations.isEmpty()) 
		{
			Account account = new Account();
			account.setId(request.getId());
			account.setActiveCard(request.getActiveCard());
			account.setAvailableLimit(request.getAvailableLimit());
			
			accountResponse.setActiveCard(account.getActiveCard());
			accountResponse.setAvailableLimit(account.getAvailableLimit());

			accountRepository.save(account);			
			disableOtherAccounts(account.getId());			
		}
		else 
		{
			accountResponse.setActiveCard(request.getActiveCard());
			accountResponse.setAvailableLimit(request.getAvailableLimit());	
		}
		
		// Prepare response
		ApiResponse response = new ApiResponse();
		response.setAccount(accountResponse);
		response.setViolations(violations);

		
		return response;
	}
		
	/**
	 * Deactivate any of the previous accounts.
	 * @param id Primary key.
	 */
	private void disableOtherAccounts(Long id) 
	{
		accountRepository.updateActiveCard(false, id);
	}
	
	/**
	 * Validation process.
	 * @param request Body request.
	 * @return List<String> response.
	 */
	public List<String> validateAccount(AccountRequest request)
	{
		List<String> violations = new ArrayList<>();
		
		if(accountRepository.existsById(request.getId())) 
		{
			violations.add(Constants.ACCOUNT_ALREADY_INITIALIZED);
		}

		
		return violations;
	}
}
