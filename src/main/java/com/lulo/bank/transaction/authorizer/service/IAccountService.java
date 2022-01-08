package com.lulo.bank.transaction.authorizer.service;

import com.lulo.bank.transaction.authorizer.dto.AccountRequest;
import com.lulo.bank.transaction.authorizer.dto.ApiResponse;

public interface IAccountService {

	/**
	 * It is responsible for registering an account and leaving it active for later use.
	 * 
	 * @param request Body request.
	 * @return ApiRespose response.
	 */
	ApiResponse registerAccount(AccountRequest request);
	
}
