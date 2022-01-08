package com.lulo.bank.transaction.authorizer.service;

import com.lulo.bank.transaction.authorizer.dto.ApiResponse;
import com.lulo.bank.transaction.authorizer.dto.TransactionRequest;

public interface ITransactionService {
	
	/**
	 * Process any transaction.
	 * 
	 * @param request Body request.
	 * @return ApiRespose response.
	 */
	ApiResponse processTransaction(TransactionRequest request);

}
