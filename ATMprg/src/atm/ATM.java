package atm;

import java.util.Scanner;

public class ATM {

	public static void main ( String [] args) {
		
		Scanner input = new Scanner(System.in);
		
		Bank theBank = new Bank("Bank of AITU");
		
		User aUser = theBank.addUser("Magzhan","Amangeldi","2334");
		
		Account newAccount = new Account("Checking", aUser, theBank);
		aUser.addAccount(newAccount);
		theBank.addAccount(newAccount);
		
		
		
		
	}
}
