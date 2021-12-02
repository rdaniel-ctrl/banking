package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController  {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    public List<ClientDTO> getClient(){
    return this.clientService.getAll().stream().map(ClientDTO::new).collect(Collectors.toList());
            //findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList()); forma mas larga
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getID(@PathVariable long id){
    return this.clientService.getById(id).map(ClientDTO::new).orElse(null);
    }

    @GetMapping("/clients/current")
    public ClientDTO getEmail(Authentication authentication){
       return new ClientDTO(this.clientService.getdByEmail(authentication.getName()));
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName,
                                           @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientService.getdByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client1= new Client(firstName, lastName, email, this.passwordEncoder.encode(password));
        this.clientService.save(client1);
        int numero = (int)((Math.random() * (99999999 - 20000000)) + 20000000);
        Account account1 = new Account("VIN-"+ numero, LocalDateTime.now(),0, AccountType.Saving,client1);
        this.accountRepository.save(account1);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
