/*
 * This is the class that contains the menu functions
 * such as adding, borrowing, returning, modifying or
 * deleting a book, adding a patron, modification and
 * deletion etc. This class is also responsible  for
 * holding books and patrons in memory as well as loading
 * them from the file of records.
 */

package com.sparktech.libsystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Toolkit;


public class Menu {
	//HashMaps of existing books and patrons
	static HashMap<String, BookData> books = new HashMap<String, BookData>();
	static HashMap<String, PatronData> patrons = new HashMap<String, PatronData>();
	static private FileWriter writer;
	static private Scanner s, s2;
	//path of files necessary for the program
	static File $dir = new File(System.getenv("APPDATA"), "LibSystem");
	static File $bookrecs = new File($dir, "BookRec.dat");
	static File $booklist = new File($dir, "BookList.dat");
	static File $logs = new File($dir, "Logs.dat");
	static File $patronlist = new File($dir, "PatronList.dat");
	static File $pass = new File($dir, "password.dat");
	static File $id_ctr = new File($dir, "counter.dat");
	//this is where general size of app components bases on
	static final int UNIVERSAL_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	static final int UNIVERSAL_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	//for limiting the number number of logs visible in Logs
	static final int LOG_ITEMS_LIMIT = 50;
	//Images used within the program
	static ImageIcon icon = new ImageIcon(Menu.class.getResource("Book.png"));
	static ImageIcon art = new ImageIcon(Menu.class.getResource("Lib.png"));
	static ImageIcon bg = new ImageIcon(Menu.class.getResource("Background.png"));
	static ImageIcon frame = new ImageIcon(Menu.class.getResource("Frame.png"));
	static ImageIcon devLogo = new ImageIcon(Menu.class.getResource("Spark.png"));

	Menu() throws IOException {
		logIn();
		loadProgram();
		GUI.refreshAll();
	}
	
	//method called before login if first lunch of the app
	//(sets password as well as create directory and files)
	static void firstLunch() throws IOException {
		String newPass, confirmPass;
		//ask for password + confirmation
		do {
			newPass = JOptionPane.showInputDialog(null, "Create Password:\n(Can be just blank)");
			if(newPass==null) System.exit(0);
			confirmPass = JOptionPane.showInputDialog(null, "Confirm Password:");
			if(!newPass.equals(confirmPass))
				JOptionPane.showMessageDialog(null, "Passwords don't match!");
		}while(!newPass.equals(confirmPass));
		//create direcotry and files
		$dir.mkdirs();
		$bookrecs.createNewFile();
		$booklist.createNewFile();
		$logs.createNewFile();
		$patronlist.createNewFile();
		$pass.createNewFile();
		//write created password to the pass file
		writer = new FileWriter(Menu.$pass);
		writer.write(newPass+"\n");
		writer.close();
	}
	
	//asks for app password, closes app upon failure to enter the correct password
	private static void logIn() throws IOException {
		s = new Scanner($pass);
		final String pass = s.nextLine();
		String inp;
		int ctr = 5;
		do {
			inp = JOptionPane.showInputDialog(null,"Enter Password:\n("+ctr--+" attempts left)");
			if(inp==null) System.exit(0);
			else if(inp.equals(pass)) JOptionPane.showMessageDialog(null, "Login successful.");
			else if(ctr==0) {
				JOptionPane.showMessageDialog(null, "You ran out of attempts. Closing program.");
				System.exit(0);
			}
		}while(!inp.equals(pass));
	}

	//loads existing data into memory
	private void loadProgram() throws IOException {
		s = new Scanner($bookrecs);
		s2 = new Scanner($booklist);
		//Loading existing BookRecord and BookData objects
		while(s2.hasNext()) {
			BookRecord temp = new BookRecord(s.next(), s.nextInt(), s.nextInt(), s.nextInt());
			String id = s2.next();
			books.put(id, new BookData(id, s2.next().replace("_", " "), s2.next().replace("_", " "), temp));
		}
		s.close();
		s2 = new Scanner($patronlist);
		//loading existing PatronRecord objects
		while(s2.hasNext()) {
			String id = s2.next();
			String title = s2.next().replace("_", " ");
			String author = s2.next().replace("_", " ");
			int unreturned = s2.nextInt();
			ArrayList<String> borrowedBooks = new ArrayList<String>();
			for(int i=0; i<unreturned; i++)
				borrowedBooks.add(s2.next());
			patrons.put(id, new PatronData(id, title, author, unreturned, borrowedBooks));
			s2.nextLine();
		}
		s2.close();
	}
	
	//method for borrowing book - if the required infos are already provided
	private static void borrowBook(BookData book, PatronData patron, int n) throws IOException {
		//Case there are enough books to borrow and Patron does not exceed borrow limit
		if(book.getRecord().getStored()-n>=0 && patron.getUnreturned()+n<=PatronData.BORROW_LIMIT) {
			String title = book.getTitle();
			String name = patron.getName();
			book.getRecord().borrowBook(title, name, n);
			patron.borrowBook(title, book.getID(), n);
			JOptionPane.showMessageDialog(null, "Book borrow recorded!", "Success", JOptionPane.INFORMATION_MESSAGE);
			GUI.refreshAll();
		//case there's not enough copies to borrow
		}else if(book.getRecord().getStored()-n<0 && patron.getUnreturned()+n<=5) {
			JOptionPane.showMessageDialog(null, "Not enough available copies to borrow!", "Borrow Failed", JOptionPane.WARNING_MESSAGE);
		//case patron exceed's borrow limit
		}else if(book.getRecord().getStored()-n>=0 && patron.getUnreturned()+n>5) {
			JOptionPane.showMessageDialog(null, "Patron exceeds Borrow Limit!", "Borrow Failed", JOptionPane.WARNING_MESSAGE);
		//case there's not enough copies and patron also exceeds borrow limit
		}else {
			JOptionPane.showMessageDialog(null, "Not enough copies available and patron exceeds borrow limit!", "Borrow Failed", JOptionPane.WARNING_MESSAGE);
		}
	}

	//method for borrowing book - asks for the required infos
	//and pass it as arguments to the other borrowBook method
	public static void borrowBook() throws IOException {
		String book, patron;
		int amount;
		//asking for BookID
		book = JOptionPane.showInputDialog(null, "Enter Book ID: ", "Borrow a Book", JOptionPane.QUESTION_MESSAGE);
		if(book==null) return;
		else if(!Menu.books.containsKey(book)) {
			JOptionPane.showMessageDialog(null, "Invalid Book ID!", "Invalid!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//asking for Patron ID
		patron = JOptionPane.showInputDialog(null, "Enter Patron ID: ", "Borrow a Book", JOptionPane.QUESTION_MESSAGE);
		if(patron==null) return;
		else if(!Menu.patrons.containsKey(patron)) {
			JOptionPane.showMessageDialog(null, "Invalid Patron ID!", "Invalid!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//asking for Quantity
		try{
			amount = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter quantity: ", "Borrow a Book", JOptionPane.QUESTION_MESSAGE));
			if(amount<1) {
				JOptionPane.showMessageDialog(null, "Invalid Quantity!", "Invalid!", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid Quantity!", "Invalid!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//use the input to get BookData and PatronData from their
		//respective HashMap, and pass it to other borrowBook method
		borrowBook(Menu.books.get(book), Menu.patrons.get(patron), amount);
		return;
	}

	//method for returning book - if the required infos are already provided
	private static void returnBook(BookData book, PatronData patron, int n) throws IOException {
		//case the patron does not owe the book being returned
		if(!patron.getBorrowedBook().contains(book.getID())) {
			JOptionPane.showMessageDialog(null, "This patron does not owe this book!", "Does not Owe Book", JOptionPane.WARNING_MESSAGE);
		}
		//Case patron is returning more book than he borrowed
		else if(patron.countBook(book.getID()) < n) {
			JOptionPane.showMessageDialog(null, "This patron does not owe this much of this book!", "Exceeding Book", JOptionPane.WARNING_MESSAGE);
		}
		//Case the book being returned does not exceed the copies he borrowed/does not exceed library owned copy
		else {
			String title = book.getTitle();
			String name = patron.getName();
			book.getRecord().returnBook(title, name, n);
			patron.returnBook(title, book.getID(), n);
			JOptionPane.showMessageDialog(null, "Book return recorded!", "Success", JOptionPane.INFORMATION_MESSAGE);
			GUI.refreshAll();
		}
	}

	//method for returning book - asks for the required infos
	//and pass it as arguments to the other returnBook method
	public static void returnBook() throws IOException {
		String book, patron;
		int amount;
		//asking for Book ID
		book = JOptionPane.showInputDialog(null, "Enter Book ID: ", "Return a Book", JOptionPane.QUESTION_MESSAGE);
		if(book==null) return;
		else if(!Menu.books.containsKey(book)) {
			JOptionPane.showMessageDialog(null, "Invalid Book ID!", "Invalid!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//asking for Patron ID
		patron = JOptionPane.showInputDialog(null, "Enter Patron ID: ", "Return a Book", JOptionPane.QUESTION_MESSAGE);
		if(patron==null) return;
		else if(!Menu.patrons.containsKey(patron)) {
			JOptionPane.showMessageDialog(null, "Invalid Patron ID!", "Invalid!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//asking for Quantity
		try{
			amount = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter quantity: ", "Return a Book", JOptionPane.QUESTION_MESSAGE));
			if(amount<1) {
				JOptionPane.showMessageDialog(null, "Invalid Quantity!", "Invalid!", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid Quantity!", "Invalid!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//use the input to get BookData and PatronData from their
		//respective HashMap, and pass it to other returnBook method
		returnBook(Menu.books.get(book), Menu.patrons.get(patron), amount);
		return;
	}
	
	//method for adding new books to records
	//ask for title, author and quantity then pass it to BookData constructor;
	public static void addBook() throws IOException{
		String title, author;
		int quantity;
		//ask for title
		title = JOptionPane.showInputDialog(null, "Enter Book Title: ", "Add New Book", JOptionPane.QUESTION_MESSAGE);
		if(title==null) return;
		else if(title.isBlank()) {
			JOptionPane.showMessageDialog(null, "That title is uninteresting!", "Cancelled", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//ask for author
		author = JOptionPane.showInputDialog(null, "Enter Book Author: ", "Add New Book", JOptionPane.QUESTION_MESSAGE);
		if(author==null) return;
		else if(author.isBlank()) {
			JOptionPane.showMessageDialog(null, "Can't accept books with mysterious authors!", "Cancelled", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//ask for quantity
		try {
			quantity = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Book Copies: ", "Add New Book", JOptionPane.QUESTION_MESSAGE));
			if(quantity<1) {
				JOptionPane.showMessageDialog(null, "Why add a book if you don't have a copy?", "Cancelled", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}catch(NumberFormatException e) {
			quantity = -1;
			JOptionPane.showMessageDialog(null, "Invalid Amount of Copies!", "Cancelled", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//Pass collected data to BookData constructor and add it to the hashmap 'books'
		String confirmMessage = "Book Title:  " + title + "\nBook Author:  " + author + "\nQuantity: " + quantity;
		int resp = JOptionPane.showConfirmDialog(null, confirmMessage, "Confirm Info", JOptionPane.OK_CANCEL_OPTION);
		if(resp==0) {
			BookData temp = new BookData(title, author, quantity);
			books.put(temp.getID(), temp);
			GUI.refreshAll();
		}else {
			JOptionPane.showMessageDialog(null, "Book not added!", "Cancelled", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	//Method for modifying a book
	//Asks for a valid BookID then opens the ModUI.
	public static void modifyBook(){
		String id = JOptionPane.showInputDialog(null, "Enter Book ID: ", "Modify a Book", JOptionPane.QUESTION_MESSAGE);
		if(id==null) {GUI.getModifyButton().setEnabled(true); return;}
		if(!Menu.books.containsKey(id)) {
			JOptionPane.showMessageDialog(null, "Book ID not found.", "Modification Failed", JOptionPane.INFORMATION_MESSAGE);
			GUI.getModifyButton().setEnabled(true);
			return;
		}else {
			ModUI.make(Menu.books.get(id));
		}
	}

	//Method for deleting a book
	//Asks for a valid BookId then opens the DelUI.
	public static void deleteBook() {
		//get BookData to delete
		String id = JOptionPane.showInputDialog(null, "Enter Book ID: ", "Delete a Book", JOptionPane.QUESTION_MESSAGE);
		if(id==null) {
			//return immediately if user clicks cancel
			GUI.getDelButton().setEnabled(true);
			return;
		}
		if(!Menu.books.containsKey(id)) {
			//return if provided id is invalid
			JOptionPane.showMessageDialog(null, "Book ID not found.", "Deletion Failed", JOptionPane.INFORMATION_MESSAGE);
			GUI.getDelButton().setEnabled(true);
			return;
		}else {
			//Open delete UI, passing the BookData to delete
			DelUI.make(Menu.books.get(id));
		}	
	}
	
	//Method for adding/registering new patrons to records
	public static void addPatron() throws IOException{
		String name, contact;
		//Ask for Patron Name
		name = JOptionPane.showInputDialog(null, "Enter Patron Full Name: ", "Add New Patron", JOptionPane.QUESTION_MESSAGE);
		if(name==null) return;
		else if(name.isBlank()) {
			JOptionPane.showMessageDialog(null, "Mysterious patrons are not allowed!", "Cancelled", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//Ask for Patron Contact Number
		contact = JOptionPane.showInputDialog(null, "Enter Contact Number: ", "Add New Patron", JOptionPane.QUESTION_MESSAGE);
		if(contact==null) return;
		else if(contact.isBlank()) {
			JOptionPane.showMessageDialog(null, "Patrons should be reachable!", "Cancelled", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//Pass collected info to PatronData constructor and put it to hashmap 'Patrons'.
		String confirmMessage = "Name:  " + name + "\nContact Number:  " + contact;
		int resp = JOptionPane.showConfirmDialog(null, confirmMessage, "Confirm Info", JOptionPane.OK_CANCEL_OPTION);
		if(resp==0) {
			PatronData temp = new PatronData(name, contact);
			patrons.put(temp.getID(), temp);
			GUI.refreshAll();
		}else {
			JOptionPane.showMessageDialog(null, "Patron not added!", "Cancelled", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//method for deleting a patron from records
	public static void deletePatron(){
		//get PatronID to delete
		String id = JOptionPane.showInputDialog(null, "Enter Patron ID: ", "Delete a Patron Record", JOptionPane.QUESTION_MESSAGE);
		
		if(id==null) {
			//return immediately if user clicks cancel
			GUI.getDelPatronButton().setEnabled(true);
			return;
		}else if(!patrons.containsKey(id)) {
			//return if provided id is invalid
			JOptionPane.showMessageDialog(null, "Patron ID not found.", "Deletion Failed", JOptionPane.INFORMATION_MESSAGE);
			GUI.getDelPatronButton().setEnabled(true);
			return;
		}else if(patrons.get(id).getUnreturned()>0){
			//return if Patron has unreturned books
			JOptionPane.showMessageDialog(null, "Can't delete patrons with unreturned books.", "Deletion Failed", JOptionPane.INFORMATION_MESSAGE);
			GUI.getDelPatronButton().setEnabled(true);
			return;
		}else {
			//Open delete UI, passing the PatronData to delete
			DelUI.make(Menu.patrons.get(id));
		}	
		
	}
	//methods for modifying a patron from records
	public static void modifyPatron() throws IOException{
		//get PatronID to modify
		String id = JOptionPane.showInputDialog(null, "Enter Patron ID: ", "Modify a Patron Record", JOptionPane.QUESTION_MESSAGE);
		
		if(id==null) {
			//if user clicks cancel, return
			GUI.getDelPatronButton().setEnabled(true);
			return;
		}else if(!patrons.containsKey(id)) {
			//if user enters invalid id, return
			JOptionPane.showMessageDialog(null, "Patron ID not found.", "Modification Failed", JOptionPane.INFORMATION_MESSAGE);
			GUI.getDelPatronButton().setEnabled(true);
			return;
		}else {
			//Open Modify UI
			ModUI.make(Menu.patrons.get(id));
		}	
		
	}
	
	//method for getting logs as a String with LOG_ITEMS_LIMIT as the limiter
	public static String getLogs(String key) throws IOException {
		String result = "";
		String holder;
		Pattern pattern = Pattern.compile(key, Pattern.CASE_INSENSITIVE);
		int ctr=0;
		s = new Scanner($logs);
		while(s.hasNextLine() && ctr<LOG_ITEMS_LIMIT) {
			holder = s.nextLine();
			Matcher matcher = pattern.matcher(holder);
			if(matcher.find())
				result = result.concat(holder + "\n");
		}
		s.close();
		
		return result;
	}
	
	//method for clearing/deleting the content of the logs
	public static void deleteLogs() throws IOException{
		if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete logs?", "Delete Logs", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			writer = new FileWriter($logs);
			writer.write("");
			writer.close();
			JOptionPane.showMessageDialog(null, "All logs are deleted.", "Logs Cleared", JOptionPane.INFORMATION_MESSAGE);
		}else {
			JOptionPane.showMessageDialog(null, "Logs are not deleted.", "Logs Kept", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	//Method for changing the password of the application
	static void changePass() throws IOException {
		//reads the current password
		s = new Scanner($pass);
		final String pass = s.nextLine();
		s.close();
		
		String inp, confirm = "";
		//ask for current password
		inp = JOptionPane.showInputDialog(null, "Enter password:");
		if(inp==null) return;
		else if(!inp.equals(pass)) {
			JOptionPane.showMessageDialog(null, "Wrong Password", "", JOptionPane.ERROR_MESSAGE);
			return;
		}else {
			//ask and confirm new password
			inp = JOptionPane.showInputDialog(null, "New Password:");
			if(inp==null) return;
			confirm = JOptionPane.showInputDialog(null, "Confirm Password:");
			if(!inp.equals(confirm)) {
				JOptionPane.showMessageDialog(null, "Passwords don't match!", "", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(inp.equals(confirm)) {
				writer = new FileWriter($pass);
				writer.write(confirm+"\n");
				writer.close();
				JOptionPane.showMessageDialog(null, "Password changed!");
			}
		}
	}
	
	//Method that opens a list of unreturned books of the user
	static void getBorrowedBooks(PatronData patron) {
		
		String result = "Borrowed Books:\n\n";
		HashMap <String,Integer> books = new HashMap<String,Integer>();
		
		for(String borrowedID: patron.getBorrowedBook()) {
			String title = Menu.books.get(borrowedID).getTitle();
			if(books.containsKey(title))
				books.put(title, books.get(title)+1);
			else
				books.put(title, 1);
		}
		int ctr=1;
		for(String title: books.keySet()) {
			result = result.concat(ctr++ + ". " + title + " - " + books.get(title) + " copy/copies.\n");
			books.put(title, Integer.MIN_VALUE);
		}
		JOptionPane.showMessageDialog(null, result);
	}
	
}

