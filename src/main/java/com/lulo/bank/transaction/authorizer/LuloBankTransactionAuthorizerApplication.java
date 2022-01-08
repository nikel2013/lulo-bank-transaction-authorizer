package com.lulo.bank.transaction.authorizer;

import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lulo.bank.transaction.authorizer.domain.Constants;
import com.lulo.bank.transaction.authorizer.dto.AccountRequest;
import com.lulo.bank.transaction.authorizer.dto.ApiResponse;
import com.lulo.bank.transaction.authorizer.dto.TransactionRequest;
import com.lulo.bank.transaction.authorizer.service.AccountService;
import com.lulo.bank.transaction.authorizer.service.TransactionService;
import com.lulo.bank.transaction.authorizer.utils.DateUtils;


@SpringBootApplication
public class LuloBankTransactionAuthorizerApplication implements CommandLineRunner{
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransactionService transactionService;
	
	private static Logger log = LoggerFactory
	        .getLogger(LuloBankTransactionAuthorizerApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(LuloBankTransactionAuthorizerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Scanner in = new Scanner(System.in);
		String request;
		
		log.info("**********************************");
		log.info("LISTENER TRANSACTION AUTHORIZER UP");
		log.info("**********************************");
		
		do{
			
			log.info("ENTER ANY JSON FROM TRANSACTION (Or Write 'exit' and press Enter to finish)");
				
			ObjectMapper mapper = new ObjectMapper();
			request = in.nextLine();
			
			if(!request.trim().equalsIgnoreCase("exit")) 
			{
				try 
				{					
					Map<String, Map<String, Object>> map = mapper.readValue(request, new TypeReference<Map<String, Map<String, Object>>>() {});
					if (map.containsKey("account")) 
					{
						Map<String, Object> account = map.get("account");
						AccountRequest accountRequest = new AccountRequest();
						accountRequest.setId(Long.parseLong(account.get("id").toString()));
						accountRequest.setActiveCard(Boolean.parseBoolean(account.get("active-card").toString()));
						accountRequest.setAvailableLimit(Integer.parseInt(account.get("available-limit").toString()));
						
						ApiResponse response = accountService.registerAccount(accountRequest);
						String stringResponse = String.format("Response: %s", mapper.writeValueAsString(response));
						log.info(stringResponse);
					}
					else if (map.containsKey("transaction")) 
					{
						Map<String, Object> transaction = map.get("transaction");
						TransactionRequest transactionRequest = new TransactionRequest();
						
						transactionRequest.setMerchant(transaction.get("merchant").toString());
						transactionRequest.setAmount(Integer.parseInt(transaction.get("amount").toString()));
						transactionRequest.setTime(DateUtils.getLocalDateTime(transaction.get("time").toString()));
						
						ApiResponse response = transactionService.processTransaction(transactionRequest);
						String stringResponse = String.format("Response: %s", mapper.writeValueAsString(response));
						log.info(stringResponse);
					}
				}
				catch (JsonParseException ex)
				{
					log.error(String.format("Error code: %s. Description: %s", Constants.JSON_STRUCTURE_UNRECOGNIZED, ex.getMessage()));
				}				
			}
				
		} while (!request.trim().equalsIgnoreCase("exit"));
		
		log.info("**************************************");
		log.info("LISTENER TRANSACTION AUTHORIZER DOWNED");
		log.info("**************************************");

		in.close();
	}
}
