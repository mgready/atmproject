package atm;

import java.util.Scanner;

public class ATM{
public static void main ( String [] args) {
		
		Scanner input = new Scanner(System.in);
		
		Bank theBank = new Bank("Bank of AITU");
		
		User aUser = theBank.addUser("Magzhan","Amangeldi","2334");
		
		Account newAccount = new Account("Checking", aUser, theBank);
		aUser.addAccount(newAccount);
		theBank.addAccount(newAccount);
		
		User curUser;
		
		while(true) {
			
			curUser = ATM.mainMenuPrompt(theBank, input);
			
			ATM.printUserMenu(curUser, input);
		}
		
		
	}
public static void printUserMenu(User theUser , Scanner input) {
	
	theUser.printAccountsSummary();
	
	int choice ;
	
	do {
		System.out.printf("Welcome &s, what would you like to do ?", theUser.getFirstName());
		System.out.println(" 1. Show account transactions history");
		System.out.println(" 2. Withdrawl");
		System.out.println(" 3. Deposit");
		System.out.println(" 4. Transfer");
		System.out.println(" 5. Quit");
		System.out.println();
		choice = input.nextInt();
		
		if(choice <1 || choice > 5) {
			System.out.println("Invalid choice. Please choice between 1-5 ");
		}
		}
	while (choice <1 || choice > 5);
		switch(choice) {
		
		case 1: 
			ATM.showTransHistory(theUser, input);
			break;
		case 2: 
			ATM.withdrawlFunds(theUser, input);
			break;
		case 3: 
			ATM.depositFunds(theUser, input);
			break;
		case 4: 
			ATM.transferFunds(theUser,input);
			break;
			
		}
		
		if(choice !=5) {
			ATM.printUserMenu(theUser, input);
		}
			
}
public static User mainMenuPrompt(Bank theBank, Scanner input) {
	
	String userID;
	String pin;
	User authUser;
	
	do {
		System.out.printf("\n\nWelcome to %s\n\n",theBank.getName());
		System.out.print("Enter user ID:");
		userID = input.nextLine();
		System.out.print("Enter pin: ");
		pin = input.nextLine();
		
		
		authUser = theBank.userLogin(userID, pin);
		if(authUser == null) {
			System.out.println("Incorrect user ID or pin code" + "Try again please");
		}
	}while(authUser == null);
	return authUser;
}

	private static void depositFunds(User theUser, Scanner input) {
		int toAcct; 
		double amount;
		double acctBal;
		String memo;
		
		do {
			System.out.printf("Enter the number (1-%d) of the account\n"+
		                      "to transfer from: ");
			toAcct =  input.nextInt()-1;
			if(toAcct <0 || toAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		}while(toAcct <0 || toAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(toAcct); 
		
		do {
			System.out.printf("Enter the amount to transfer (max $%0.2f): $",
					acctBal);
			amount = input.nextDouble();
			if ( amount < 0 ) {
				System.out.println("Amount must be greater than zero.");
			} else if (amount > acctBal) {
				System.out.printf("Amount must not be greater than\n" + 
			            "balance of $%0.2f.\n", acctBal);
				
			}
	}while (amount <0 || amount >acctBal);
		input.nextLine();
		
		System.out.println("Enter a memo: ");
		memo = input.nextLine();
		
		theUser.addAcctTransaction(toAcct, -1*amount, memo);
		
	}
	private static void withdrawlFunds(User theUser, Scanner input) {
		
		int fromAcct;
		double amount;
		double acctBal;
		String memo;
		
		do {
			System.out.printf("Enter the number (1-%d) of the account\n"+
		                      "to transfer from: ");
			fromAcct =  input.nextInt()-1;
			if(fromAcct <0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		}while(fromAcct <0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct); 
		
		do {
			System.out.printf("Enter the amount to transfer (max $%0.2f): $",
					acctBal);
			amount = input.nextDouble();
			if ( amount < 0 ) {
				System.out.println("Amount must be greater than zero.");
			} else if (amount > acctBal) {
				System.out.printf("Amount must not be greater than\n" + 
			            "balance of $%0.2f.\n", acctBal);
				
			}
	}while (amount <0 || amount >acctBal);
		input.nextLine();
		
		System.out.println("Enter a memo: ");
		memo = input.nextLine();
		
		theUser.addAcctTransaction(fromAcct, -1*amount, memo);
		
	}
	private static void transferFunds(User theUser, Scanner input) {
		
		int fromAcct;
		int toAcct;
		double amount;
		double acctBal;
		
		do {
			System.out.printf("Enter the number (1-%d) of the account\n"+
		                      "to transfer from: ");
			fromAcct =  input.nextInt()-1;
			if(fromAcct <0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		}while(fromAcct <0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct); 
		
		do {
			System.out.printf("Enter the number (1-%d) of the account\n"+
		                      "to transfer to: ");
			toAcct =  input.nextInt()-1; 
			if(toAcct <0 || toAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		}while(toAcct <0 || toAcct >= theUser.numAccounts());
		
		do {
			System.out.printf("Enter the amount to transfer (max $%0.2f): $",
					acctBal);
			amount = input.nextDouble();
			if ( amount < 0 ) {
				System.out.println("Amount must be greater than zero.");
			} else if (amount > acctBal) {
				System.out.printf("Amount must not be greater than\n" + 
			            "balance of $%0.2f.\n", acctBal);
				
			}
		}while(amount <0 || amount >acctBal);
		
		theUser.addAcctTransaction(fromAcct, -1*amount , String.format(
				"Transfer to account %s" , theUser.getAcctUUID(toAcct)));
		theUser.addAcctTransaction(toAcct, -1*amount , String.format(
				"Transfer to account %s" , theUser.getAcctUUID(toAcct)));
	}
	private static void showTransHistory(User theUser, Scanner input) {
		
		int theAcct;
		
		do {
			System.out.printf("Enter the number (1-%d) of the account" + "whose transactions you want to see: "
		, theUser.numAccounts());
			theAcct = input.nextInt()-1;
			if(theAcct < 0  || theAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		}while(theAcct < 0  || theAcct >= theUser.numAccounts());
		
		theUser.printAcctTransHistory(theAcct);
	}
}
