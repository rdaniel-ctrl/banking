package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


@Transactional
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @PostMapping("/transactions")
    public ResponseEntity response (Authentication authentication, @RequestParam Double amount,
                                    @RequestParam String description, @RequestParam String numberOrigin,
                                    @RequestParam String numberDesti){

        Client client = clientRepository.findByEmail(authentication.getName());

        Account origen = accountRepository.findBynumber(numberOrigin);

        Account destino =accountRepository.findBynumber(numberDesti);

        if (amount.toString().isEmpty() || description.isEmpty() ) {
            return new ResponseEntity<>("amount or description is empty", HttpStatus.FORBIDDEN);
        }
        if (numberOrigin.isEmpty()||numberDesti.isEmpty()){
            return new ResponseEntity("number is empty",HttpStatus.FORBIDDEN);
        }
        if (origen == null){
            return  new ResponseEntity("source account  does not exist",HttpStatus.FORBIDDEN);
        }
        if (destino == null){
            return new ResponseEntity(" destination account does not exist",HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().stream().map(cuenta -> cuenta.getNumber()).collect(Collectors.toList()).contains(numberOrigin)){
            return new ResponseEntity("source account does not belong to authenticated client",HttpStatus.FORBIDDEN);
        }
        if (origen.getBalance() < amount){
            return new ResponseEntity("account has no amount",HttpStatus.FORBIDDEN);
        }
        if (origen == destino){
            return new ResponseEntity("source account  is the same as the destination account",HttpStatus.FORBIDDEN);
        }


        double origbalance =   origen.getBalance() - amount;

        origen.setBalance(origbalance);
        accountRepository.save(origen);

        double destibalce = destino.getBalance() + amount;

        destino.setBalance(destibalce);
        accountRepository.save(destino);

        Transaction transaction1 = new Transaction(TransactionType.DEBIT,- amount,"Successful transfer" +" "+ destino.getNumber()+" "+ description, LocalDate.now(),origen.getBalance(),origen);

        Transaction transaction2 = new Transaction(TransactionType.CREDIT,amount,"transfer received"+" "+origen.getNumber()+" "+description, LocalDate.now(),destino.getBalance(),destino);

        transactionRepository.save(transaction1);

        transactionRepository.save(transaction2);

        return new ResponseEntity("transaction was successful",HttpStatus.CREATED);
    }

}
