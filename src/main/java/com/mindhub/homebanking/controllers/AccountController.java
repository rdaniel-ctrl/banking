package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/account")
    public List<AccountDTO>getAccount(){
        return this.accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }
    @GetMapping("/account/{id}")
    public AccountDTO getId(@PathVariable Long id) {
        return this.accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @DeleteMapping("/clients/current/accounts/delete")
    public ResponseEntity<?>deleteByacco(Authentication authentication,@RequestParam String number){

        Client client =clientRepository.findByEmail(authentication.getName());

        Account account = accountRepository.findBynumber(number);

        Set<Transaction> transactionSet =  account.getTransfer();

       if(client.getAccounts().stream().noneMatch(cliente -> cliente.getNumber().equals(number))) return new ResponseEntity<>("account does not belong to the authenticated client",HttpStatus.FORBIDDEN);

        transactionRepository.deleteAll(transactionSet);

       accountRepository.delete(account);

        return new ResponseEntity<>("erased account ",HttpStatus.CREATED);
    }


    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> verifica(Authentication authentication,@RequestParam AccountType type){

    if (clientRepository.findByEmail(authentication.getName()).getAccounts().size() == 3) {
        return new ResponseEntity<>("you have 3 account",HttpStatus.FORBIDDEN);
    } else {
           int numero = (int)((Math.random() * (99999999 - 20000000)) + 20000000);
        Account account1 = new Account("VIN-" + numero, LocalDateTime.now(),0,type,clientRepository.findByEmail(authentication.getName()));
        accountRepository.save(account1);
        return new ResponseEntity<>("cuenta creada",HttpStatus.CREATED);
    }

    }


}
