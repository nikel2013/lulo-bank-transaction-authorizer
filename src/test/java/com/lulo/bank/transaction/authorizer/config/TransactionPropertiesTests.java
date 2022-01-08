package com.lulo.bank.transaction.authorizer.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TransactionPropertiesTests {
		
	@Mock
	private TrasactionProperties transactionProperties;
	
	@BeforeEach
	void setUp() 
	{ 
		MockitoAnnotations.openMocks(this);
						
		transactionProperties.intervalMinutesByAnyTransaction = 2;
		transactionProperties.intervalMinutesBySimilarTransaction = 2;
		transactionProperties.maxAnyTransactionByInterval = 3;
		transactionProperties.maxSimiliarTransactionByInterval = 1;
	}
	
	@Test
	void testTransactionProperties() 
	{
		TrasactionProperties transactionProperties = new TrasactionProperties();
		transactionProperties = this.transactionProperties;
		assertNotNull(transactionProperties);
	}
}

