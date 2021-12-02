package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ADMINDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class LoanController{

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/loans")
    public List<LoanDTO> Getall(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<?>response(Authentication authentication,
                                     @RequestBody LoanApplicationDTO loanApplicationDTO){

        Client cLient = clientService.getdByEmail(authentication.getName());
        Account account1 = accountRepository.findBynumber(loanApplicationDTO.getAccount());

        if(loanApplicationDTO.getName().isEmpty()|| loanApplicationDTO.getAmount() == 0|| loanApplicationDTO.getPayment() == 0 ||loanApplicationDTO.getAccount().isEmpty()){
        return new ResponseEntity<>("fields are empty",HttpStatus.FORBIDDEN);
        }

        if(account1 == null){
            return new ResponseEntity<>("account does not exist",HttpStatus.FORBIDDEN);
        }

        if(!cLient.getAccounts().stream().map(Account::getNumber).collect(Collectors.toList()).contains(loanApplicationDTO.getAccount())){
            return new ResponseEntity<>("destination account does not belong to the customer",HttpStatus.FORBIDDEN);
        }

        if (loanRepository.findByName(loanApplicationDTO.getName()) == null){
            return new ResponseEntity<>("loan does not exist",HttpStatus.FORBIDDEN);
        }

        if (loanRepository.findByName(loanApplicationDTO.getName()).getMaxAmount() < loanApplicationDTO.getAmount()){
            return new ResponseEntity<>("the amount was exceeded",HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() < 0) return new ResponseEntity<>("amount must be greater than 100",HttpStatus.FORBIDDEN);

        if (cLient.getLoans().stream().anyMatch(loan -> loan.getName().equals(loanApplicationDTO.getName()))){
            return new ResponseEntity<>("you have a loan of the same type" + " "+loanApplicationDTO.getName(),HttpStatus.FORBIDDEN);
        }

        if (!loanRepository.findByName(loanApplicationDTO.getName()).getPayments().contains(loanApplicationDTO.getPayment())) {
            return new ResponseEntity<>("amount of payments does not belong to loan", HttpStatus.FORBIDDEN);
        }

        double nuevo = account1.getBalance() + loanApplicationDTO.getAmount();
        account1.setBalance(nuevo);

        ClientLoan clientLoan= new ClientLoan(loanApplicationDTO.getAmount(),loanApplicationDTO.getPayment(),loanApplicationDTO.getPercentage(),cLient,loanRepository.findByName(loanApplicationDTO.getName()));
        clientLoanRepository.save(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT,(double)loanApplicationDTO.getAmount(),loanApplicationDTO.getName()+" " + "loan approved", LocalDate.now(),account1.getBalance(),account1);
        transactionRepository.save(transaction);

        return new ResponseEntity<>("loans controller",HttpStatus.CREATED);
   }

    @PostMapping("/loans/admin")
    public ResponseEntity<?> response(Authentication authentication, @RequestBody ADMINDTO admindto){

        Client cLient = clientService.getdByEmail(authentication.getName());

        if(cLient.getEmail().contains("admin@mindhub.com")){
            Loan loan1 = new Loan(admindto.getName(),admindto.getAmount(),admindto.getPayments());
            loanRepository.save(loan1);
        }


        return new ResponseEntity<>("Loan created",HttpStatus.CREATED);
    }



}
