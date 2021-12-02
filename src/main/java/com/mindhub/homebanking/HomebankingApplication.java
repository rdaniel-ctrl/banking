package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository repository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository) {

		return (args) -> {

			Client client1 = new Client("Melba","Morel","melba@mindhub.com",passwordEncoder.encode("melba"));
			Client client2 = new Client("Renzo","Serrano", "serranorenzo@indhub.com",passwordEncoder.encode("1234"));
			Client ADMIN = new Client("ADMIN","ADMIN","admin@mindhub.com",passwordEncoder.encode("1235"));

			repository.save(client1);
			repository.save(client2);
			repository.save(ADMIN);

			Account cuenta1 =new Account("VIN001",LocalDateTime.now(),5000,AccountType.Checking,client1);
			Account cuenta2 = new Account("VIN002",LocalDateTime.now().plusDays(1),7500,AccountType.Saving,client1);
			Account cuenta3 = new Account("03412",LocalDateTime.now(),1000,AccountType.Saving,client2);
			Account cuenta4 = new Account("1212",LocalDateTime.now().plusDays(2),99999,AccountType.Saving,client2);

			accountRepository.save(cuenta1);
			accountRepository.save(cuenta2);
			accountRepository.save(cuenta3);
			accountRepository.save(cuenta4);


			/*client1.addAccount(cuenta1);
			client1.addAccount(cuenta2);
			client2.addAccount(cuenta3);
			client2.addAccount(cuenta4);*/

			Transaction accion1 = new Transaction(TransactionType.DEBIT,-101.0,"EN EL MAC", LocalDate.now(),cuenta1.getBalance(),cuenta1);
			Transaction accion2 = new Transaction(TransactionType.CREDIT,101.0,"Tranferencia de su amigo juanito",LocalDate.now().plusDays(1),cuenta1.getBalance(),cuenta1);
			Transaction accion3 = new Transaction(TransactionType.DEBIT,-101.0,"EN EL burger king", LocalDate.now(),cuenta2.getBalance(),cuenta2);
			Transaction accion4 = new Transaction(TransactionType.CREDIT,1101.0,"recibe por acciones que invirtio",LocalDate.now().plusDays(1),cuenta2.getBalance(),cuenta2);

			cuenta1.addTranfer(accion1);
			cuenta1.addTranfer(accion2);
			cuenta2.addTranfer(accion3);
			cuenta2.addTranfer(accion4);

			transactionRepository.save(accion1);
			transactionRepository.save(accion2);
			transactionRepository.save(accion3);
			transactionRepository.save(accion4);

			var integerList1 = List.of(12,24,36,48,60);
			var integerList2 = List.of(6,12,24);
			var integerList3 = List.of(6,12,24,36);

			Loan loan1 = new Loan("Mortgage",500000,integerList1);
			Loan loan2 = new Loan("Personal",100000,integerList2);
			Loan loan3 = new Loan("Automotive",300000,integerList3);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000,60,20.00,client1,loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000,12 ,19.00,client1,loan2);

			ClientLoan clientLoan3 = new ClientLoan(100000,24,1.20,client2,loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000,36,10000.00,client2,loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);

			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card(client1.getFirstName()+" "+ client1.getLastName(),CardType.DEBIT,CardColor.GOLD,"1234-5678-9012-3456",220,LocalDateTime.now().plusYears(5),LocalDateTime.now(),client1);
			Card card2 = new Card(client1.getFirstName()+" "+client1.getLastName(),CardType.CREDIT,CardColor.TITANIUM,"3216-5498-7021-3645",300,LocalDateTime.now().plusYears(5),LocalDateTime.now(),client1);
			Card card3 = new Card(client2.getFirstName()+" "+ client2.getLastName(),CardType.CREDIT,CardColor.SILVER,"2123-4569-0233-4567",210,LocalDateTime.now().plusYears(6),LocalDateTime.now(),client2);
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

		};

	}

}
