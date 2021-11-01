package com.sparktech.libsystem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PatronData {

	static FileWriter writer;
	static Scanner s;
	//attributes of PatronData
	private String patronID;
	private String fullName; 						
	private String contactNo; 				
	private ArrayList<String> borrowedBooks; 						
	private int unreturnedBooks; 				
	static final int BORROW_LIMIT = 5;
	
	//Constructor for Newly Added Patrons
	PatronData(String fullName, String contactNo) throws IOException{
		/*
		 * Each new patron are automatically assigned
		 * with a PatronID. These IDs are assigned with
		 * numbers starting from 1000, tracked by a
		 * counter file called "counter.dat".
		 */
		if(!Menu.$id_ctr.exists()) {
			//Case where it's first time opening the counter file
			this.patronID = "LIB-1000";
			Menu.$id_ctr.createNewFile();
			writer = new FileWriter(Menu.$id_ctr);
			writer.write("10000\n");//counter for books registered
			writer.write("1001");//counter for patrons registered
			writer.close();
		}else{
			//Case where the counter file is already created
			s = new Scanner(Menu.$id_ctr);
			int temp = Integer.parseInt(s.nextLine());
			int ctr = Integer.parseInt(s.next());
			this.patronID = "LIB-" + ctr++;
			s.close();
			writer = new FileWriter(Menu.$id_ctr);
			writer.write(Integer.toString(temp)+ "\n" +Integer.toString(ctr));
			writer.close();
		}
		//Initializing Object attributes and calling newPatron()
		this.fullName = fullName;
		this.contactNo = contactNo;
		this.unreturnedBooks = 0;
		newPatron(this);
		//Logging the addition of a new patron.
		writer = new FileWriter(Menu.$logs, true);
		writer.write(this.patronID + "\t" + "Added " + this.fullName + " on " + java.time.LocalDate.now() + ".\n");
		writer.close();
	}
	
	//Constructor for books loaded from file into memory
	PatronData(String patronID, String fullName, String contactNo, int unreturnedBooks, ArrayList<String> borrowedBooks) throws IOException{
		this.patronID = patronID;
		this.fullName = fullName;
		this.contactNo = contactNo;
		this.unreturnedBooks = unreturnedBooks;
		this.borrowedBooks = borrowedBooks;
	}
	

	//getters!
	public String getID() {
		return this.patronID;
	}
	public String getName() {
		return this.fullName;
	}
	public String getContact() {
		return this.contactNo;
	}
	public int getUnreturned() {
		return this.unreturnedBooks;
	}
	public ArrayList<String> getBorrowedBook(){
		return this.borrowedBooks;
	}
	//setters are unnecessary because we use a method for modification.
	
	//Method called when a book is deleted to make
	//sure it's also removed from patron record
	public void deleteBook(String delBook) throws IOException {
		int ctr=0;
		//Removes all occurence of book being
		//deleted from the ArrayList of borrowed
		//books and counts it.
		while(this.borrowedBooks.contains(delBook)) {
			this.borrowedBooks.remove(delBook);
			ctr++;
		}
		//Subtracts the previously counted amount
		//from the recorded amount of borrowed
		//books
		this.unreturnedBooks -= ctr;
		//Modify the patron record from files
		s = new Scanner(Menu.$patronlist);
		//--store content of PatronList.dat to a String, modify lines affected
		String newData = "";
		while(s.hasNextLine()) {
			String current = s.next();
			if(!current.equals(this.patronID))
				newData = newData.concat(current + s.nextLine() + "\n");
			else {
				newData = newData.concat(current + "\t"
										+ this.fullName.replace(" ", "_") + "\t"
										+ this.contactNo.replace(" ", "_") + "\t"
										+ this.unreturnedBooks);
				for(int i=0; i<this.unreturnedBooks; i++)
					newData = newData.concat("\t" + this.borrowedBooks.get(i));
				newData = newData.concat("\n");
				s.nextLine();
			}
		}
		s.close();
		//--Replacing the content of file with the loaded String
		writer = new FileWriter(Menu.$patronlist);
		writer.write(newData);
		writer.close();
	}
	
	//Method called when a patron borrows a book
	//(boolean for testing purposes, can be turned to void)
	public boolean borrowBook(String title, String borrowedBook, int borrowed) throws IOException{
		//Make sure patron does not exceed limit
		if(this.unreturnedBooks+borrowed > BORROW_LIMIT)
			return false;
		else {
			//updates patron record in file
			this.unreturnedBooks += borrowed;
			for(int i=0; i<borrowed; i++)
				this.borrowedBooks.add(borrowedBook);
			s = new Scanner(Menu.$patronlist);
			//Modify the patron record from files
			String newData = "";
			//--store content of PatronList.dat to a String, modify lines affected
			while(s.hasNextLine()) {
				String current = s.next();
				if(!current.equals(this.patronID))
					newData = newData.concat(current + s.nextLine() + "\n");
				else {
					newData = newData.concat(current + "\t"
											+ this.fullName.replace(" ", "_") + "\t"
											+ this.contactNo.replace(" ", "_") + "\t"
											+ this.unreturnedBooks);
					for(int i=0; i<this.unreturnedBooks; i++)
						newData = newData.concat("\t" + this.borrowedBooks.get(i));
					newData = newData.concat("\n");
					s.nextLine();
				}
			}
			s.close();
			//--Replacing the content of file with the loaded String
			writer = new FileWriter(Menu.$patronlist);
			writer.write(newData);
			writer.close();
			//logs the borrow transaction
			writer = new FileWriter(Menu.$logs, true);
			writer.write(this.patronID + "\t" + this.fullName + " borrowed " + borrowed + " copy/copies of " + title + " on " + java.time.LocalDate.now() + ".\n");
			writer.close();
		}
		return true;
	}
	//Method called when Patron returns a Book
	//(boolean for testing purposes, can be turned to void)
	public boolean returnBook(String title, String returnedBook, int returned) throws IOException {
		//Make sure Patron does not return more books than he owe
		if(this.unreturnedBooks-returned<0)
			return false;
		else {
			//update record in files
			this.unreturnedBooks -= returned;
			for(int i=0; i<returned; i++)
				this.borrowedBooks.remove(returnedBook);
			s = new Scanner(Menu.$patronlist);
			//Modify the patron record from files
			String newData = "";
			//--store content of PatronList.dat to a String, modify lines affected
			while(s.hasNextLine()) {
				String current = s.next();
				if(!current.equals(this.patronID))
					newData = newData.concat(current + s.nextLine() + "\n");
				else {
					newData = newData.concat(current + "\t"
											+ this.fullName.replace(" ", "_") + "\t" 
											+ this.contactNo.replace(" ", "_") + "\t"
											+ this.unreturnedBooks);
					for(int i=0; i<this.unreturnedBooks; i++)
						newData = newData.concat("\t" + this.borrowedBooks.get(i));
					newData = newData.concat("\n");
					s.nextLine();
				}
			}
			s.close();
			//--Replacing the content of file with the loaded String
			writer = new FileWriter(Menu.$patronlist);
			writer.write(newData);
			writer.close();
			//Logs the return transaction
			writer = new FileWriter(Menu.$logs,true);
			writer.write(this.patronID + "\t" + this.fullName + " returned " + returned + " copy/copies of " + title + " on " + java.time.LocalDate.now() + ".\n");
			writer.close();
		}
		return true;
	}
	
	//Function called whenever a new patron is added
	//(writes the new book to file records)
	private static void newPatron(PatronData patron) throws IOException {
		writer = new FileWriter(Menu.$patronlist, true);
		writer.write(patron.patronID + "\t" + patron.fullName.replace(" ", "_") + "\t" + patron.contactNo.replace(" ", "_") + "\t" + patron.unreturnedBooks +"\n");
		writer.close();
	}

	//Necessary function when returning a book
	//To count the amount of a specific unreturned book
	public int countBook(String id) {
		int ctr = 0;
		for(String s: this.borrowedBooks) {
			if(s.equals(id)) ctr++;
		}
		return ctr;
	}
	
	//Mehod Called when Deleting A Patron (NON STATIC)
	public void deletePatron() throws IOException {
		s = new Scanner(Menu.$patronlist);
		//Modify the patron record from files
		String newData = "";
		//--store content of PatronList.dat to a String, skip lines to delete
		while(s.hasNextLine()) {
			String current = s.next();
			if(!current.equals(this.patronID))
				newData = newData.concat(current + s.nextLine() + "\n");
			else 
				s.nextLine();
		}
		s.close();
		//--Replacing the content of file with the loaded String
		writer = new FileWriter(Menu.$patronlist);
		writer.write(newData);
		writer.close();
		//logs deleting the Patron
		writer = new FileWriter(Menu.$logs, true);
		writer.write(this.patronID + "\t" + "Deleted " + this.fullName + " on " + java.time.LocalDate.now() + ".\n");
		writer.close();

		//Removing the patron from the HashMap in memory
		Menu.patrons.remove(this.patronID);
		/*
		 * Setting stuffs to null, to make them eligible for
		 * garbage collection. Not sure if that's how it 
		 * works. Delete this if I'm wrong.
		 */
		this.patronID = null;
		this.contactNo = null;
		this.borrowedBooks = null;
	}

	//Method Called when Modifying a Patron (NON STATIC)
	public void modify(String name, String contact) throws IOException {
		//Modifying the Object Attributes
		this.fullName = name;
		this.contactNo = contact;
		//Modifying the record from file
		s = new Scanner(Menu.$patronlist);
		//--Loading the new content to a string, replacing line to modify
		String newData = "";
		while(s.hasNextLine()) {
			String current = s.next();
			if(!current.equals(this.patronID))
				newData = newData.concat(current + s.nextLine() + "\n");
			else {
				newData = newData.concat(current + "\t" + this.fullName.replace(" ", "_") + "\t" + this.contactNo.replace(" ", "_") + "\t" + this.unreturnedBooks);
				for(int i=0; i<this.unreturnedBooks; i++)
					newData = newData.concat("\t" + this.borrowedBooks.get(i));
				newData = newData.concat("\n");
				s.nextLine();
			}
		}
		s.close();
		//--Replacing the content of file with the loaded String
		writer = new FileWriter(Menu.$patronlist);
		writer.write(newData);
		writer.close();
		//Writing Logs for modifying the Book
		writer = new FileWriter(Menu.$logs, true);
		writer.write(this.patronID + "\t" + "Modified '" + fullName + "' on " + java.time.LocalDate.now() + ".\n");
		writer.close();
	}
}
