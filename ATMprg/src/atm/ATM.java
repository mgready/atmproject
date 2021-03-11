package atm;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ATM{
public static void main ( String [] args) {
	try {
		Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users","root","root");
		
		Statement myStmt = myConn.createStatement();
		
		ResultSet myRs = myStmt.executeQuery("SELECT * FROM `userslist");
		
		while (myRs.next()) {
			System.out.println(myRs.getInt("user_id") +" "+ myRs.getString("first_name") + ", " + myRs.getString("second_name"));
			
		}
	} catch(Exception e) {
		e.printStackTrace();
	}
		
		Scanner input = new Scanner(System.in);
		
		Bank theBank = new Bank("Bank of AITU");
		
		User aUser = theBank.addUser("Magzhan","Amangeldi","3222");
		
		User bUser = theBank.addUser("Andrew", "Mikryakov", "2333");
		
		User cUser = theBank.addUser("John", "Doe", "3222");
		
		User dUser = theBank.addUser("Michael", "Forester", "2222");
		
		User eUser = theBank.addUser("Dias", "Zheksenbayev", "4567");
		
		User fUser = theBank.addUser("Ablay", "Zhantore", "2451");
		
		Account newAccount3 = new Account("Checking", dUser, theBank);
		bUser.addAccount(newAccount3);
		theBank.addAccount(newAccount3);

		Account newAccount4 = new Account("Checking", eUser, theBank);
		bUser.addAccount(newAccount4);
		theBank.addAccount(newAccount4);
		
		Account newAccount5 = new Account("Checking", fUser, theBank);
		bUser.addAccount(newAccount5);
		theBank.addAccount(newAccount5);
		
		Account newAccount2 = new Account("Checking", cUser, theBank);
		bUser.addAccount(newAccount2);
		theBank.addAccount(newAccount2);
		
		Account newAccount1 = new Account("Checking", bUser, theBank);
		bUser.addAccount(newAccount1);
		theBank.addAccount(newAccount1);
		
		Account newAccount = new Account("Checking", aUser, theBank);
		aUser.addAccount(newAccount);
		theBank.addAccount(newAccount);
		
		
		User curUser ;
		do {
            curUser = ATM.mainMenuPrompt(theBank, input);
			
			ATM.printUserMenu(curUser, input);
		}
		while(true);
	
		
	}
public static void printUserMenu(User theUser , Scanner input) {
	
	theUser.printAccountsSummary();
	
	int choice ;
	
	do {
		System.out.println("Welcome " + theUser.getFirstName() + " what would you like to do ?" );
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
		                      "to deposit in: ", theUser.numAccounts());
			toAcct =  input.nextInt()-1;
			if(toAcct <0 || toAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		}while(toAcct <0 || toAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(toAcct); 
		
		do {
			System.out.printf("Enter the amount to transfer (max $%.02f): $",
					acctBal);
			amount = input.nextDouble();
			if ( amount < 0 ) {
				System.out.println("Amount must be greater than zero.");
			} 
	}while (amount < 0 );
		input.nextLine();
		
		System.out.print("Enter a memo: ");
		memo = input.nextLine();
		
		theUser.addAcctTransaction(toAcct, -1*amount, memo);
		try {
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users","root","root");
			
			Statement myStmt = myConn.createStatement();
			
			String sql="update ATM set atm_balance = atm_balance +"+amount+" where atm_id=1";
			
			myStmt.execute(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	private static void withdrawlFunds(User theUser, Scanner input) {
		
		int fromAcct;
		double amount;
		double acctBal;
		String memo;
		
		do {
			System.out.printf("Enter the number (1-%d) of the account\n"+
		                      "to withdraw from: ", theUser.numAccounts());
			fromAcct =  input.nextInt()-1;
			if(fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		}while(fromAcct <0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct); 
		
		do {
			System.out.printf("Enter the amount to transfer (max $%.02f): $",
					-1*acctBal);
			amount = input.nextDouble();
			if ( amount < 0 ) {
				System.out.println("Amount must be greater than zero.");
			} else if (amount > -1*acctBal) {
				System.out.printf("Amount must not be greater than\n" + 
			            "balance of $%.02f.\n", -1*acctBal);
				
			}
	}while (amount <0 || amount > -1*acctBal);
		input.nextLine();
		
		System.out.println("Enter a memo: ");
		memo = input.nextLine();
		
		theUser.addAcctTransaction(fromAcct, amount, memo);
		
		try {
			
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users","root","root");
			
			Statement myStmt = myConn.createStatement();
			
			Statement stmt = myConn.createStatement();
			
			
			String sql="update ATM set atm_balance = atm_balance -"+amount+" where atm_id=1";
			
			myStmt.execute(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	private static void transferFunds(User theUser, Scanner input) {
		
		int fromAcct;
		int toAcct;
		double amount;
		double acctBal;
		
		do {
			System.out.printf("Enter the number (1-%d) of the account\n"+
		                      "to transfer from: ",theUser.numAccounts());
			fromAcct =  input.nextInt()-1;
			if(fromAcct <0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		}while(fromAcct <0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct); 
		
		do {
			System.out.printf("Enter the number (1-%d) of the account\n"+
		                      "to transfer to: ", theUser.numAccounts());
			toAcct =  input.nextInt()-1; 
			if(toAcct <0 || toAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		}while(toAcct <0 || toAcct >= theUser.numAccounts());
		
		do {
			System.out.printf("Enter the amount to transfer (max $%.02f): $",
					-1*acctBal);
			amount = input.nextDouble();
			if ( amount < 0 ) {
				System.out.println("Amount must be greater than zero.");
			} else if (amount > -1*acctBal) {
				System.out.printf("Amount must not be greater than\n" + 
			            "balance of $%.02f.\n", -1*acctBal);
				
			}
		}while(amount <0 || amount >-1*acctBal);
		
		theUser.addAcctTransaction(fromAcct, amount , String.format(
				"Transfer to account %s" , theUser.getAcctUUID(toAcct)));
		theUser.addAcctTransaction(toAcct, amount , String.format(
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
	public static void UpdateAccBalance(double balance ) {
		try {
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users","root","root");
			
			Statement myStmt = myConn.createStatement();
			
			ResultSet myRs = myStmt.executeQuery("SELECT * FROM `userslist");
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
