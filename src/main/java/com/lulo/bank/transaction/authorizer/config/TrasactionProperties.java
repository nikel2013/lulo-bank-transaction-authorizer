package com.lulo.bank.transaction.authorizer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrasactionProperties {
	
	@Value("${max-any-transaction-by-interval}")
	public Integer maxAnyTransactionByInterval;
	
	@Value("${interval-minutes-by-any-transaction}")
	public Integer intervalMinutesByAnyTransaction;
	
	@Value("${max-similiar-transaction-by-interval}")
	public Integer maxSimiliarTransactionByInterval;
	
	@Value("${interval-minutes-by-similar-transaction}")
	public Integer intervalMinutesBySimilarTransaction;

}
