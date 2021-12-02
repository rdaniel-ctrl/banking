package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.implement.PDFExporterImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PDFController {

    @Autowired
    private PDFExporterImplement pdfExporterImplement;

    @Autowired
    private ClientRepository  clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
   private TransactionRepository transactionRepository;

    @PostMapping("/transaction/export/pdf")
    public void exportTOPDF(HttpServletResponse response,
                            Authentication authentication,
                            @RequestParam String number,
                            @RequestParam String from,
                            @RequestParam String to)throws IllegalArgumentException, IOException{

        LocalDate Fromdate = LocalDate.parse(from);
        LocalDate Todate = LocalDate.parse(to);

        Client client = clientRepository.findByEmail(authentication.getName());

       // Account account = accountRepository.findBynumber(number);// para buscar por numero de cuenta

        Set <Transaction> transactions = transactionRepository.findAllByDateBetween(Fromdate,Todate).stream().filter(cuenta-> cuenta.getAccount().getNumber().equals(number)).collect(Collectors.toSet());

        response.setContentType("application/pdf");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

        String concentrate = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";

        String headerValue = "attachment;filename=Account status_" + number +" "+ concentrate + ".pdf";

        response.setHeader(headerKey,headerValue);

        //Set <Transaction> transactionSet =  account.getTransfer(); //las transaciones de cuentas en total

        this.pdfExporterImplement.exports(response,transactions,client);
    }
}
