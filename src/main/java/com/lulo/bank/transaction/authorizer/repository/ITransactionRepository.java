package com.lulo.bank.transaction.authorizer.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lulo.bank.transaction.authorizer.entity.Transaction;

public interface ITransactionRepository extends JpaRepository<Transaction, Long>{

	int countByTimeGreaterThanEqualAndTimeLessThan(LocalDateTime startDate, LocalDateTime endDate);
	
	int countByTimeGreaterThanEqualAndTimeLessThanAndAmountAndMerchantIgnoreCase(LocalDateTime startDate, LocalDateTime endDate, Integer amount, String merchant);
	
	int countByTimeGreaterThanEqualAndTimeLessThanEqual(LocalDateTime startDate, LocalDateTime endDate);
	
	int countByTimeGreaterThanEqualAndTimeLessThanEqualAndAmountAndMerchantIgnoreCase(LocalDateTime startDate, LocalDateTime endDate, Integer amount, String merchant);
}