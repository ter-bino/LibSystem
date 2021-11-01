package com.sparktech.libsystem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class BookRecord {
	
	static FileWriter writer;
	static Scanner s;
	//attributes of a BookRecord
	private String bookID;
	private int stored, borrowed, total;

	//Constructor of BookRecord for new books
	BookRecord(String bookID, int quantity) throws IOException {
		this.bookID = bookID;
		this.stored = this.total = quantity;
		this.borrowed = 0;
		newRecord(this);
	}
	
	//Constructor of existing BookRecord loaded from Files
	BookRecord(String bookID, int stored, int borrowed, int total) throws IOException {
		this.bookID = bookID;
		this.stored = stored;
		this.borrowed = borrowed;
		this.total = total;
	}

	//called when creating new Books to add them to file record
	private static void newRecord(BookRecord record) throws IOException {
		writer = new FileWriter(Menu.$bookrecs, true);
		writer.write(record.bookID + "\t" + record.stored + "\t" + record.borrowed + "\t" + record.total + "\n");
		writer.close();
	}
	
	//Record a borrow of book and returns true if successful, false if not
	//(boolean for testing purposes, can work as void)
	public boolean borrowBook(String title, String name, int borrowed) throws IOException {
		//Return immediately if there are not enough books to lend.
		if(borrowed>this.stored)
			return false;
		else {
			//Modify record attributes
			this.stored -= borrowed;
			this.borrowed += borrowed;
			//Modify Record in Files
			s = new Scanner(Menu.$bookrecs);
			//--Load content of "BookRecord.dat" to a String
			String newData = "";
			while(s.hasNextLine()) {
				String current = s.next();
				if(!current.equals(this.bookID))
					newData = newData.concat(current + s.nextLine() + "\n");
				else {
					newData = newData.concat(current + "\t" + this.stored + "\t" + this.borrowed + "\t" + this.total + "\n");
					s.nextLine();
				}
			}
			s.close();
			//--Overwrite "BookRecord.dat" with the loaded string
			writer = new FileWriter(Menu.$bookrecs);
			writer.write(newData);
			writer.close();
			
			//Logs the borrow transaction
			writer = new FileWriter(Menu.$logs, true);
			writer.write(this.bookID + "\t" + title + " - " + borrowed +  " copy/copies borrowed by " + name + " on " + java.time.LocalDate.now() + ".\n");
			writer.close();
		}
		return true;
	}

	//Record a return of book and returns true if successful, false if not
	//(boolean for testing purposes, can work as void)
	public boolean returnBook(String title, String name, int returned) throws IOException {
		//Return immediately if lib-owned copies are exceeded
		if(returned+stored>this.total)
			return false;
		else {
			//Modify record attributes
			this.stored += returned;
			this.borrowed -= returned;
			s = new Scanner(Menu.$bookrecs);
			//--Load content of "BookRecord.dat" to a String
			String newData = "";
			while(s.hasNextLine()) {
				String current = s.next();
				if(!current.equals(this.bookID))
					newData = newData.concat(current + s.nextLine() + "\n");
				else {
					newData = newData.concat(current + "\t" + this.stored + "\t" + this.borrowed + "\t" + this.total + "\n");
					s.nextLine();
				}
			}
			s.close();
			//--Overwrite "BookRecord.dat" with the loaded string
			writer = new FileWriter(Menu.$bookrecs);
			writer.write(newData);
			writer.close();
			//--Overwrite "BookRecord.dat" with the loaded string
			writer = new FileWriter(Menu.$logs, true);
			writer.write(this.bookID + "\t" + title + " - " + returned +  " copy/copies returned by " + name + " on " + java.time.LocalDate.now() + ".\n");
			writer.close();
		}
		return true;
	}
	
	//Method called from BookData Class when modifying a book
	public void modify() throws IOException {
		//Modify Record in Files
		s = new Scanner(Menu.$bookrecs);
		//--Load content of "BookRecord.dat" to a String, replacing line to modify
		String newData = "";
		while(s.hasNextLine()) {
			String current = s.next();
			if(!this.bookID.equals(current)) {
				newData = newData.concat(current + s.nextLine() + "\n");
			}else {
				newData = newData.concat(current + "\t" + this.stored + "\t" + this.borrowed + "\t" + this.total + "\n");
				s.nextLine();
			}
		}
		s.close();
		//--Overwrite "BookRecord.dat" with the loaded string
		writer = new FileWriter(Menu.$bookrecs);
		writer.write(newData);
		writer.close();
	}

	//Method called from BookData Class when deleting a book
	public void delete() throws IOException {
		//Modify Record in Files
		s = new Scanner(Menu.$bookrecs);
		//--Load content of "BookRecord.dat" to a String, skipping line to delete
		String newData = "";
		while(s.hasNextLine()) {
			String current = s.next();
			if(!current.equals(this.bookID))
				newData = newData.concat(current + s.nextLine() + "\n");
			else 
				s.nextLine();
		}
		s.close();
		//--Overwrite "BookRecord.dat" with the loaded string
		writer = new FileWriter(Menu.$bookrecs);
		writer.write(newData);
		writer.close();
	}
	

	
	//getters
	public int getStored() {
		return this.stored;
	}
	public int getBorrowed() {
		return this.borrowed;
	}
	public int getTotal() {
		return this.total;
	}
	//setters
	public void setStored(int n) {
		this.stored = n;
	}
	public void setBorrowed(int n) {
		this.borrowed = n;
	}
	public void setTotal(int n) {
		this.total = n;
	}
}
