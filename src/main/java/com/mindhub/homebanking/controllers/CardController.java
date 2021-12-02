package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardPaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @DeleteMapping("/clients/current/cards/delete/{id}")
    public ResponseEntity<?> deleteBycards(@PathVariable Long id ,Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());

        var cards = client.getCards().stream().filter(card -> card.getId().equals(id)).collect(Collectors.toList());

        if (cards.size() < 1) return new ResponseEntity<>("card does not belong to the customer",HttpStatus.FORBIDDEN);

        cardRepository.deleteById(id);
        return new ResponseEntity<>("erased card",HttpStatus.CREATED);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> Card (@RequestParam CardColor cardColor, @RequestParam CardType cardType , Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());
        //client.getCards().stream().filter(card -> card.getType() == CardType.DEBIT).collect(Collectors.toSet());

        if (cardColor.toString().isEmpty()) return new ResponseEntity<>("Invalid data",HttpStatus.FORBIDDEN);

        if(cardType.toString().isEmpty()) return new ResponseEntity<>("Invalid data",HttpStatus.FORBIDDEN);

        if(client.getCards().stream().filter(card -> card.getType().equals(cardColor)).count() == 1) {
            return new ResponseEntity<>("has the same card color"+" "+cardColor,HttpStatus.FORBIDDEN);
        }

        if(client.getCards().stream().filter(card -> card.getType().equals(cardType)).count() == 3) {
            return new ResponseEntity<>("you have more than 3 cards", HttpStatus.FORBIDDEN);
        }
        int numero1 = (int)((Math.random() * (9999 - 1000)) + 1000);
        int numero2 = (int)((Math.random() * (9999 - 1000)) + 1000);
        int numero3 = (int)((Math.random() * (9999 - 1000)) + 1000);
        int numero4 = (int)((Math.random() * (9999 - 1000)) + 1000);

        int cvv = (int) ((Math.random()* (999 - 100))+ 100);
        Card card =new Card(client.getFirstName() + " " +client.getLastName(),cardType,cardColor,(numero1 + "-" + numero2+"-"+ numero3 +"-"+numero4),cvv, LocalDateTime.now().plusYears(5),LocalDateTime.now(),client);
        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/clients/current/cards/payment")
    public ResponseEntity<?>response(Authentication authentication, @RequestBody CardPaymentDTO cardPaymentDTO){
        Client client = clientRepository.findByEmail(authentication.getName());

        Account account= accountRepository.findBynumber(cardPaymentDTO.getAccount());

        Card card = cardRepository.findBynumber(cardPaymentDTO.getNumber());

        if (cardPaymentDTO.getNumber().isEmpty()||cardPaymentDTO.getAmount() ==0||cardPaymentDTO.getSecuritycode()== 0||
                cardPaymentDTO.getDescription().isEmpty()||cardPaymentDTO.getAccount().isEmpty()){
            return new ResponseEntity<>("invalid data",HttpStatus.FORBIDDEN);
        }
        if(card.getFromDate().isAfter(card.getThruDate())){
            return new ResponseEntity<>("your card is expired",HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() < 0){
            return new ResponseEntity<>("you have no money",HttpStatus.FORBIDDEN);
        }
        if (cardPaymentDTO.getAmount()<0) return new ResponseEntity<>("amount must be more than 0",HttpStatus.FORBIDDEN);

        if (cardPaymentDTO.getAmount()>account.getBalance()){
            return  new ResponseEntity<>("amount is greater than your balance",HttpStatus.FORBIDDEN);
        }
        if(cardPaymentDTO.getSecuritycode() != card.getCvv()) return new ResponseEntity<>("invalid data card",HttpStatus.FORBIDDEN);

        if(account==null){
            return new ResponseEntity<>("account does not exist",HttpStatus.FORBIDDEN);
        }
        if(!client.getAccounts().stream().map(Account::getNumber).collect(Collectors.toList()).contains(cardPaymentDTO.getAccount())){
            return new ResponseEntity<>("destination account does not belong to the customer",HttpStatus.FORBIDDEN);
        }
        double nuevo = account.getBalance() - cardPaymentDTO.getAmount();
        account.setBalance(nuevo);

        Transaction transaction = new Transaction(TransactionType.DEBIT,(double) - cardPaymentDTO.getAmount(),cardPaymentDTO.getDescription(),
                LocalDate.now(),account.getBalance(),account);
        transactionRepository.save(transaction);
        return new ResponseEntity<>("Create payments",HttpStatus.CREATED);
    }

}
