package cafe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cafe.repository.AccountRepository;

@Service
public class AccountServices {
	@Autowired
	AccountRepository accountRepository;
	
	
}
