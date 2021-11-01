package com.sparktech.libsystem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class BookData {
	
	static FileWriter writer;
	static Scanner s;
	//attributes of a BookData
	private String bookID;
	private String title;
	private String author;
	private BookRecord record;
	
	//constructor for new Books
	BookData(String title, String author, int quantity) throws IOException{
		/*
		 * Each new book are automatically assigned
		 * with a BookID. These IDs are assigned with
		 * numbers starting from 10000, tracked by
		 * a counter file called "counter.dat".
		 */
		if(!Menu.$id_ctr.exists()) {
			//Case where it's first time opening the counter file
			this.bookID = "2021-10000";
			Menu.$id_ctr.createNewFile();
			writer = new FileWriter(Menu.$id_ctr);
			writer.write("10001\n");//counter for books registered
			writer.write("1000");//counter for patrons registered
			writer.close();
		}else{
			//Case where the counter file is already created
			s = new Scanner(Menu.$id_ctr);
			int ctr = Integer.parseInt(s.nextLine());
			int temp = Integer.parseInt(s.next());
			this.bookID = "2021-" + ctr++;
			s.close();
			writer = new FileWriter(Menu.$id_ctr);
			writer.write(Integer.toString(ctr) + "\n" + Integer.toString(temp));
			writer.close();
		}
		//Initializing Object attributes and calling newBook()
		this.title = title;
		this.author = author;
		this.record = new BookRecord(this.bookID, quantity);
		newBook();
		//Logging the addition of a new book.
		writer = new FileWriter(Menu.$logs, true);
		writer.write(this.bookID + "\t" + "Added " + quantity + " copy/copies of '" + title + "' on " + java.time.LocalDate.now() + ".\n");
		writer.close();
	}
	
	//constructor for existing books being loaded from files
	BookData(String bookID ,String title, String author, BookRecord record) throws IOException{
		this.bookID = bookID;
		this.title = title;
		this.author = author;
		this.record = record;
	}
	
	
	//getters
	public String getID() {
		return this.bookID;
	}
	public String getTitle() {
		return this.title;
	}
	public String getAuthor() {
		return this.author;
	}
	public BookRecord getRecord() {
		return this.record;
	}
	//setters are unnecessary because we use a method for modification.
	
	//Method Called when Modifying a Book (NON STATIC)
	public void modify(String title, String author, int i, int j, int k) throws IOException {
		//Modifying the Object Attributes
		this.title = title;
		this.author = author;
		this.record.setStored(i);
		this.record.setBorrowed(j);
		this.record.setTotal(k);
		//Modifying the record from file
		s = new Scanner(Menu.$booklist);
		//--Loading the new content to a string, replacing line to modify
		String newData = "";
		while(s.hasNextLine()) {
			String current = s.next();
			if(!current.equals(this.bookID))
				newData = newData.concat(current + s.nextLine() + "\n");
			else {
				newData = newData.concat(current + "\t" + this.title.replace(" ", "_") + "\t" + this.author.replace(" ", "_") + "\n");
				s.nextLine();
			}
		}
		s.close();
		//--Replacing the content of file with the loaded String
		writer = new FileWriter(Menu.$booklist);
		writer.write(newData);
		writer.close();
		//--Calling modify() method of the record attribute
		this.record.modify();
		//Writing Logs for modifying the Book
		writer = new FileWriter(Menu.$logs, true);
		writer.write(this.bookID + "\t" + "Modified '" + title + "' on " + java.time.LocalDate.now() + ".\n");
		writer.close();
	}
	
	//Mehod Called when Deleting A Book (NON STATIC)
	public void deleteBook() throws IOException {
		//Deleting the Record from Files
		s = new Scanner(Menu.$booklist);
		//--Loading each line of file to a string, skipping the record to delete
		String newData = "";
		while(s.hasNextLine()) {
			String current = s.next();
			if(!current.equals(this.bookID))
				newData = newData.concat(current + s.nextLine() + "\n");
			else
				s.nextLine();
		}
		s.close();
		//--Overwriting the file with the loaded String
		writer = new FileWriter(Menu.$booklist);
		writer.write(newData);
		writer.close();
		//--Calling delete() method from the BookRecord object
		this.record.delete();
		
		//--Calling deleteBook() method from Patron Objects who has copies of the deleted book
		for(String currentID: Menu.patrons.keySet()) {
			PatronData patron = Menu.patrons.get(currentID);
			if(patron.getBorrowedBook().contains(this.bookID)) {
				patron.deleteBook(this.bookID);
			}
		}

		//Writing Logs for Deleting the Book
		writer = new FileWriter(Menu.$logs, true);
		writer.write(this.bookID + "\t" + "Deleted '" + title + "' on " + java.time.LocalDate.now() + ".\n");
		writer.close();
		//Removing the book from the HashMap in memory
		Menu.books.remove(this.bookID);
		/*
		 * Setting stuffs to null, to make them eligible for
		 * garbage collection. Not sure if that's how it 
		 * works. Delete this if I'm wrong.
		 */
		this.title = null;
		this.author = null;
		this.bookID = null;
		this.record = null;
	}
	
	//Method called to write in file of record if a new book is instantiated
	private void newBook() throws IOException {
		writer = new FileWriter(Menu.$booklist, true);
		writer.write(this.bookID + "\t" + this.title.replace(" ", "_") + "\t" + this.author.replace(" ", "_") + "\n");
		writer.close();
	}
	
}
