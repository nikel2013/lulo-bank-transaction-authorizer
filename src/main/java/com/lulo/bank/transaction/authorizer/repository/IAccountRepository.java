package com.lulo.bank.transaction.authorizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.lulo.bank.transaction.authorizer.entity.Account;

public interface IAccountRepository extends JpaRepository<Account, Long> 
{
	@Modifying
	@Transactional
	@Query("UPDATE Account a SET a.activeCard = :activeCard WHERE a.id != :id")
	void updateActiveCard(@Param(value = "activeCard") Boolean activeCard, @Param(value = "id") Long id);
	
	Account findByActiveCard(Boolean activeCard);
	
	boolean existsById(Long id);
	
	int countByActiveCard(Boolean activeCard);
}