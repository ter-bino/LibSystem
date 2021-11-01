/*
 * An Object of this class is instantiated when
 * deleting a Book or Patron from Record. Done
 * from deleteBook() and deletePatron() methods
 * in Menu class.
 */

package com.sparktech.libsystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

@SuppressWarnings("serial")
public class DelUI extends JDialog {
	//Components of the UI/JDialog
	private JPanel  detailsPanel, contentPanel, headerPanel;
	private JTextField textField_2, textField_1, idField;
	private JLabel headerLabel, totalVal, storedVal, borrowedVal, idLabel, attributeLabel_1, attributeLabel_2,
					storedLabel, borrowedLabel, totalLabel, unreturnedLabel, unreturnedVal;
	private JButton cancelButton, deleteButton;
	//Mouse position for dragging of the UI's header
	private static int mouseX, mouseY;
	//Sizes within the UI
	private static final int APP_WIDTH = (int) (Menu.UNIVERSAL_WIDTH * 0.2299), APP_HEIGHT = (int) (Menu.UNIVERSAL_HEIGHT * 0.1499), COMPONENT_HEIGHT = 16;
	//Contents of the UI (Attributes of the passed record)
	private static String label_1, label_2, label_3, data_1, data_2, data_3, st, bo, to; // st, bo, to are amount labels lmao
	//Flag for wether we are opening UI for a Book or a Patron
	private static boolean isBook;

	//for Delete UI of a BookData
	public static void make(BookData book) {
		//Changing UI contents based on passed argument
		isBook = true;
		label_1 = "Book ID:";
		label_2 = "Book Title:";
		label_3 = "Book Author:";
		data_1 = book.getID();
		data_2 = book.getTitle();
		data_3 = book.getAuthor();
		st = Integer.toString(book.getRecord().getStored());
		bo = Integer.toString(book.getRecord().getBorrowed());
		to = Integer.toString(book.getRecord().getTotal());
		//Instantiating the DelUI
		try {
			DelUI dialog = new DelUI();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//for Delete UI of a PatronData
	public static void make(PatronData patron) {
		//Changing UI contents based on passed argument
		isBook = false;
		label_1 = "Patron ID:";
		label_2 = "Full Name:";
		label_3 = "Contact No.:";
		data_1 = patron.getID();
		data_2 = patron.getName();
		data_3 = patron.getContact();
		to = Integer.toString(patron.getUnreturned());
		//Instantiating the DelUI
		try {
			DelUI dialog = new DelUI();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 
	 * Constructor of the DelUI:
	 * This is private because we don't want
	 * DelUI to be constructed without passing
	 * an argument through the make() method.
	 */
	private DelUI() {
		//JDialog attributes
		setUndecorated(true);
		setSize(APP_WIDTH, APP_HEIGHT);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		
		//Main Content Panel
		contentPanel = new JPanel();
		contentPanel.setBackground(Color.LIGHT_GRAY);
		contentPanel.setBounds(0, 28, APP_WIDTH, APP_HEIGHT-28);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel);
		
		//draggable header pannel
		headerPanel = new JPanel();
		headerPanel.setBackground(SystemColor.controlDkShadow);
		headerPanel.setBounds(0, 0, APP_WIDTH, 28);
		getContentPane().add(headerPanel);
		//event listeners that allows dragging of the dialog
		headerPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				setLocation(getX() +  e.getX() - mouseX, getY() + e.getY() - mouseY);
			}
		});
		headerPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		
		//label of draggable header
		headerLabel = new JLabel("DELETE THIS RECORD?");
		headerLabel.setForeground(UIManager.getColor("Button.light"));
		headerLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerPanel.add(headerLabel);
		
		//panel that contains the details of the record
		detailsPanel = new JPanel();
		detailsPanel.setBackground(Color.DARK_GRAY);
		detailsPanel.setBounds((int)(APP_WIDTH*0.01999), 0, (int)(APP_WIDTH*0.95999), (int)(APP_HEIGHT-56));
		detailsPanel.setLayout(null);
		contentPanel.add(detailsPanel);
		
		//Row 1: ID of the Record
		idLabel = new JLabel(label_1);
		idLabel.setForeground(Color.WHITE);
		idLabel.setFont(new Font("Century Gothic", Font.BOLD, 13));
		idLabel.setBounds((int)(detailsPanel.getWidth()*0.0499), (int)(detailsPanel.getHeight()*0.0999), (int)(detailsPanel.getWidth()*0.2499), COMPONENT_HEIGHT);
		detailsPanel.add(idLabel);
		
		idField = new JTextField();
		idField.setForeground(Color.WHITE);
		idField.setBackground(Color.GRAY);
		idField.setFont(new Font("Arial Narrow", Font.PLAIN, 14));
		idField.setBounds((int)(detailsPanel.getWidth()*0.3499), (int)(detailsPanel.getHeight()*0.0999), (int)(detailsPanel.getWidth()*0.5999), COMPONENT_HEIGHT);
		idField.setColumns(10);
		idField.setEditable(false);
		idField.setText(data_1);
		detailsPanel.add(idField);

		//Row 2: An attribute of the record (Book Title or Patron name)
		attributeLabel_1 = new JLabel(label_2);
		attributeLabel_1.setForeground(Color.WHITE);
		attributeLabel_1.setFont(new Font("Century Gothic", Font.BOLD, 13));
		attributeLabel_1.setBounds((int)(detailsPanel.getWidth()*0.0499), (int)(detailsPanel.getHeight()*0.0999)*2 + COMPONENT_HEIGHT/2, (int)(detailsPanel.getWidth()*0.2499), COMPONENT_HEIGHT);
		detailsPanel.add(attributeLabel_1);

		textField_1 = new JTextField();
		textField_1.setForeground(Color.WHITE);
		textField_1.setBackground(Color.GRAY);
		textField_1.setFont(new Font("Arial Narrow", Font.PLAIN, 14));
		textField_1.setBounds((int)(detailsPanel.getWidth()*0.3499), (int)(detailsPanel.getHeight()*0.0999)*2 + COMPONENT_HEIGHT/2, (int)(detailsPanel.getWidth()*0.5999), COMPONENT_HEIGHT);
		textField_1.setColumns(10);
		textField_1.setEditable(false);
		textField_1.setText(data_2);
		detailsPanel.add(textField_1);
		
		//Row 3: An attribute of the record (Book Author or Patron Contact)
		attributeLabel_2 = new JLabel(label_3);
		attributeLabel_2.setForeground(Color.WHITE);
		attributeLabel_2.setFont(new Font("Century Gothic", Font.BOLD, 13));
		attributeLabel_2.setBounds((int)(detailsPanel.getWidth()*0.0499), (int)(detailsPanel.getHeight()*0.0999)*3 + COMPONENT_HEIGHT, (int)(detailsPanel.getWidth()*0.2499), COMPONENT_HEIGHT);
		detailsPanel.add(attributeLabel_2);
		
		textField_2 = new JTextField();
		textField_2.setForeground(Color.WHITE);
		textField_2.setBackground(Color.GRAY);
		textField_2.setFont(new Font("Arial Narrow", Font.PLAIN, 14));
		textField_2.setBounds((int)(detailsPanel.getWidth()*0.3499), (int)(detailsPanel.getHeight()*0.0999)*3 + COMPONENT_HEIGHT, (int)(detailsPanel.getWidth()*0.5999), COMPONENT_HEIGHT);
		textField_2.setColumns(10);
		textField_2.setEditable(false);
		textField_2.setText(data_3);
		detailsPanel.add(textField_2);
		
		if(isBook) {
		//ROW 4: (If we are displaying a Book)
			//Stored Books
			storedLabel = new JLabel("Stored");
			storedLabel.setForeground(Color.WHITE);
			storedLabel.setFont(new Font("Century Gothic", Font.BOLD, 13));
			storedLabel.setBounds((int)(detailsPanel.getWidth()*0.1499), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT/2*3, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			storedLabel.setHorizontalAlignment(SwingConstants.CENTER);
			detailsPanel.add(storedLabel);
			
			storedVal = new JLabel();
			storedVal.setForeground(Color.WHITE);
			storedVal.setFont(new Font("Tahoma", Font.BOLD, 11));
			storedVal.setBounds((int)(detailsPanel.getWidth()*0.1499), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT*2+5, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			storedVal.setHorizontalAlignment(SwingConstants.CENTER);
			storedVal.setText(st);
			detailsPanel.add(storedVal);
	
			//Borrowed Books
			borrowedLabel = new JLabel("Borrowed");
			borrowedLabel.setForeground(Color.WHITE);
			borrowedLabel.setFont(new Font("Century Gothic", Font.BOLD, 13));
			borrowedLabel.setBounds((int)(detailsPanel.getWidth()*0.3999), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT/2*3, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			borrowedLabel.setHorizontalAlignment(SwingConstants.CENTER);
			detailsPanel.add(borrowedLabel);
	
			borrowedVal = new JLabel();
			borrowedVal.setForeground(Color.WHITE);
			borrowedVal.setFont(new Font("Tahoma", Font.BOLD, 11));
			borrowedVal.setBounds((int)(detailsPanel.getWidth()*0.3999), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT*2+5, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			borrowedVal.setHorizontalAlignment(SwingConstants.CENTER);
			borrowedVal.setText(bo);
			detailsPanel.add(borrowedVal);
	
			//Total Books (Library Owned Copies)
			totalLabel = new JLabel("Total");
			totalLabel.setForeground(Color.WHITE);
			totalLabel.setFont(new Font("Century Gothic", Font.BOLD, 13));
			totalLabel.setBounds((int)(detailsPanel.getWidth()*0.6499), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT/2*3, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
			detailsPanel.add(totalLabel);
			
			totalVal = new JLabel();
			totalVal.setForeground(Color.WHITE);
			totalVal.setFont(new Font("Tahoma", Font.BOLD, 11));
			totalVal.setBounds((int)(detailsPanel.getWidth()*0.6499), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT*2+5, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			totalVal.setHorizontalAlignment(SwingConstants.CENTER);
			totalVal.setText(to);
			detailsPanel.add(totalVal);
			
		}else {
		//ROW 4: (If we are displaying a Patron)
			//Amount of books unreturned by Patron
			unreturnedLabel = new JLabel("Unreturned Books");
			unreturnedLabel.setForeground(Color.WHITE);
			unreturnedLabel.setFont(new Font("Century Gothic", Font.BOLD, 12));
			unreturnedLabel.setBounds((int)(detailsPanel.getWidth()*0.3999), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT/2*3, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			unreturnedLabel.setHorizontalAlignment(SwingConstants.CENTER);
			detailsPanel.add(unreturnedLabel);
	
			unreturnedVal = new JLabel();
			unreturnedVal.setForeground(Color.WHITE);
			unreturnedVal.setFont(new Font("Tahoma", Font.BOLD, 11));
			unreturnedVal.setBounds((int)(detailsPanel.getWidth()*0.3999), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT*2+5, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			unreturnedVal.setHorizontalAlignment(SwingConstants.CENTER);
			unreturnedVal.setText(to);
			detailsPanel.add(unreturnedVal);
		}
		
		//Cancel Button
		cancelButton = new JButton("Cancel");
		cancelButton.setForeground(SystemColor.textHighlightText);
		cancelButton.setBackground(SystemColor.textInactiveText);
		cancelButton.setFont(new Font("Century Gothic", Font.BOLD, 14));
		cancelButton.setBounds((int)(APP_WIDTH/2+10), contentPanel.getHeight()-26, 90, 24);
		cancelButton.setFocusable(false);cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUI.getDelButton().setEnabled(true);
				GUI.getDelPatronButton().setEnabled(true);
				dispose();
			}
		});
		contentPanel.add(cancelButton);
		
		//Button for Confirmation
		deleteButton = new JButton("Delete");
		deleteButton.setForeground(Color.WHITE);
		deleteButton.setBackground(SystemColor.textInactiveText);
		deleteButton.setFont(new Font("Century Gothic", Font.BOLD, 14));
		deleteButton.setBounds((int)(APP_WIDTH/2-100), contentPanel.getHeight()-26, 90, 24);
		deleteButton.setFocusable(false);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showMessageDialog(null, "Deleted \"" + data_2 + "\" from records.", "Record Deleted", JOptionPane.INFORMATION_MESSAGE);
					if(isBook) {
						//Calls deleteBook() method of the BookData
						Menu.books.get(data_1).deleteBook(); 
						GUI.getRefreshButton().doClick();
					}else {
						//Calls deletePatron() method of the PatronData
						Menu.patrons.get(data_1).deletePatron();
						GUI.getPatronSearchButton().doClick();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				//Re-enables delete button of the main GUI
				GUI.getDelButton().setEnabled(true);
				GUI.getDelPatronButton().setEnabled(true);
				dispose();
			}
		});
		contentPanel.add(deleteButton);
		
		//Setting the Default Button of the Dialog
		getRootPane().setDefaultButton(deleteButton);
	}
}
