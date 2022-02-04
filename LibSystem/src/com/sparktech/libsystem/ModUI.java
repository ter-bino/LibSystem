/*
 * An Object of this class is instantiated when
 * modifying a Book or Patron from Record. Done
 * from modifyBook() and modifyPatron() methods
 * in Menu class.
 */

package com.sparktech.libsystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

@SuppressWarnings("serial")
public class ModUI extends JDialog {
	//Components of the UI/JDialog
	private JPanel contentPanel, detailsPanel, headerPanel;
	private JTextField textField_2, textField_1, idField, totalVal;
	private JSpinner storedSpinner, borrowedSpinner;
	private JLabel headerLabel, idLabel, attributeLabel_1, attributeLabel_2, storedLabel, borrowedLabel,
					totalLabel, unreturnedLabel, unreturnedVal;
	private JButton cancelButton, modifyButton;
	//Mouse position for dragging of the UI's header
	private static int mouseX, mouseY;
	//Sizes within the UI
	private final int APP_WIDTH = (int) (Menu.UNIVERSAL_WIDTH * 0.2299), APP_HEIGHT = (int) (Menu.UNIVERSAL_HEIGHT * 0.1499), COMPONENT_HEIGHT = 16;
	//Contents of the UI (Attributes of the passed record)
	private static String label_1, label_2, label_3, data_1, data_2, data_3;
	private static int st, bo, to; // st, bo, to are amount labels lmao
	//Flag for wether we are opening UI for a Book or a Patron
	static boolean isBook = false;

	//for ModUI of BookData
	public static void make(BookData book) {
		//Changing UI contents based on passed argument
		isBook = true;
		label_1 = "Book ID:";
		label_2 = "Book Title:";
		label_3 = "Book Author:";
		data_1 = book.getID();
		data_2 = book.getTitle();
		data_3 = book.getAuthor();
		st = book.getRecord().getStored();
		bo = book.getRecord().getBorrowed();
		to = book.getRecord().getTotal();
		try {
			//Instantiating the InfoUI
			ModUI dialog = new ModUI();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//for ModUI of PatronData
	public static void make(PatronData patron) {
		//Changing UI contents based on passed argument
		isBook = false;
		label_1 = "Patron ID:";
		label_2 = "Full Name:";
		label_3 = "Contact No.:";
		data_1 = patron.getID();
		data_2 = patron.getName();
		data_3 = patron.getContact();
		to = patron.getUnreturned();
		try {
			//Instantiating the InfoUI
			ModUI dialog = new ModUI();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* 
	 * Constructor of the ModUI:
	 * This is private because we don't want
	 * ModUI to be constructed without passing
	 * an argument through the make() method.
	 */
	private ModUI() {
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
		
		//draggable header panel
		headerPanel = new JPanel();
		headerPanel.setBackground(SystemColor.controlDkShadow);
		headerPanel.setBounds(0, 0, APP_WIDTH, 28);
		getContentPane().add(headerPanel);
		//event listener that allows dragging of the dialog
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
		
		//label of Draggable Header
		headerLabel = new JLabel("MODIFY A RECORD");
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setForeground(UIManager.getColor("Button.light"));
		headerLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
		headerPanel.add(headerLabel);
		
		//Panel that contains the details to be modified
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
		idField.setBackground(Color.GRAY);
		idField.setForeground(Color.WHITE);
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
	
			storedSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 127, 1));
			storedSpinner.setBounds((int)(detailsPanel.getWidth()*0.1499), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT*2+10, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			storedSpinner.setValue(st);
			detailsPanel.add(storedSpinner);
			storedSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					int n = (int) borrowedSpinner.getValue();
					int m = (int) storedSpinner.getValue();
					totalVal.setText(m+n+"");
				}
			});

			//Borrowed Books
			borrowedLabel = new JLabel("Borrowed");
			borrowedLabel.setForeground(Color.WHITE);
			borrowedLabel.setFont(new Font("Century Gothic", Font.BOLD, 12));
			borrowedLabel.setBounds((int)(detailsPanel.getWidth()*0.3999), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT/2*3, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			borrowedLabel.setHorizontalAlignment(SwingConstants.CENTER);
			detailsPanel.add(borrowedLabel);
			
			borrowedSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 127, 1));
			borrowedSpinner.setBounds((int)(detailsPanel.getWidth()*0.3999), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT*2+10, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			borrowedSpinner.setValue(bo);
			detailsPanel.add(borrowedSpinner);
			borrowedSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					int n = (int) borrowedSpinner.getValue();
					int m = (int) storedSpinner.getValue();
					totalVal.setText(m+n+"");
				}
			});

			//Total Books (Library Owned Copies)
			totalLabel = new JLabel("Total");
			totalLabel.setForeground(Color.WHITE);
			totalLabel.setFont(new Font("Century Gothic", Font.BOLD, 12));
			totalLabel.setBounds((int)(detailsPanel.getWidth()*0.6499), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT/2*3, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
			detailsPanel.add(totalLabel);
	
			totalVal = new JTextField();
			totalVal.setForeground(Color.WHITE);
			totalVal.setBackground(Color.GRAY);
			totalVal.setFont(new Font("Arial Narrow", Font.BOLD, 12));
			totalVal.setBounds((int)(detailsPanel.getWidth()*0.6499), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT*2+10, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			totalVal.setText(to+"");
			totalVal.setEditable(false);
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
			unreturnedVal.setBackground(Color.DARK_GRAY);
			unreturnedVal.setFont(new Font("Tahoma", Font.BOLD, 11));
			unreturnedVal.setBounds((int)(detailsPanel.getWidth()*0.3999), (int)(detailsPanel.getHeight()*0.0999)*4 + COMPONENT_HEIGHT*2+5, (int)(detailsPanel.getWidth()*0.1999), COMPONENT_HEIGHT);
			unreturnedVal.setHorizontalAlignment(SwingConstants.CENTER);
			unreturnedVal.setText(Integer.toString(to));
			detailsPanel.add(unreturnedVal);
		}
		
		//Cancel Button
		cancelButton = new JButton("Cancel");
		cancelButton.setForeground(SystemColor.textHighlightText);
		cancelButton.setBackground(SystemColor.textInactiveText);
		cancelButton.setFont(new Font("Century Gothic", Font.BOLD, 12));
		cancelButton.setBounds((int)(APP_WIDTH/2+10), contentPanel.getHeight()-26, 90, 24);
		cancelButton.setFocusable(false);
		contentPanel.add(cancelButton);cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUI.getModifyButton().setEnabled(true);
				GUI.getModPatronButton().setEnabled(true);
				dispose();
			}
		});
		
		//Button for Confirmation
		modifyButton = new JButton("Modify");
		modifyButton.setForeground(SystemColor.textHighlightText);
		modifyButton.setBackground(SystemColor.textInactiveText);
		modifyButton.setFont(new Font("Century Gothic", Font.BOLD, 12));
		modifyButton.setBounds((int)(APP_WIDTH/2-100), contentPanel.getHeight()-26, 90, 24);
		modifyButton.setFocusable(false);
		contentPanel.add(modifyButton);
		modifyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Does not proceed as long as modify() returns false
					if(isBook) {
						//for Books
						if(modify(Menu.books.get(data_1))) {
							GUI.getModifyButton().setEnabled(true);
							GUI.getRefreshButton().doClick();
							dispose();
						}
					}
					else
						//for Patrons
						if(modify(Menu.patrons.get(data_1))) {
							GUI.getModPatronButton().setEnabled(true);
							GUI.getPatronSearchButton().doClick();
							dispose();
						}
				}catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		getRootPane().setDefaultButton(modifyButton);
	}
	
	/*
	 * Below are modify() methods that couldn't be moved
	 * to Menu class because they are reliant to what's
	 * inside of the field in the UI. Do a work around
	 * if possible.
	 */
	
	//For modifying BookData
	//Returns true if modification is succesful, otherwise false
	private boolean modify(BookData book) throws IOException {
		if(textField_1.getText().isBlank() || textField_2.getText().isBlank()) {
			//fails if a field is blank
			JOptionPane.showMessageDialog(this, "Please enter requred fields!", "Required Field Empty", JOptionPane.WARNING_MESSAGE);
			return false;
		}else if((int)storedSpinner.getValue()==0 && (int)borrowedSpinner.getValue()==0) {
			//fails if user set copies to 0
			JOptionPane.showMessageDialog(this, "Might as well delete the book if you don't hava a copy!", "Zero Copies?", JOptionPane.WARNING_MESSAGE);
			return false;
		}else {
			//If inputs are valid, call modify() method of BookData
			book.modify(textField_1.getText(), textField_2.getText(),
					(int) storedSpinner.getValue(),
					(int)borrowedSpinner.getValue(),
					Integer.parseInt(totalVal.getText()));
			//Succesful modification message
			JOptionPane.showMessageDialog(this, "Book Record Modified Sucessfully", "Modification Successful", JOptionPane.INFORMATION_MESSAGE);
		}
		return true;	
	}
	//For modifying PatronData
	//Returns true if modification is succesful, otherwise false
	private boolean modify(PatronData patron) throws IOException {
		if(textField_1.getText().isBlank() || textField_2.getText().isBlank()) {
			//fails if a field is blank
			JOptionPane.showMessageDialog(this, "Please enter requred fields!", "Required Field Empty", JOptionPane.WARNING_MESSAGE);
			return false;
		}else {
			//If inputs are valid, call modify() method of PatronData
			patron.modify(textField_1.getText(), textField_2.getText());

			//Succesful modification message
			JOptionPane.showMessageDialog(this, "Patron Record Modified Sucessfully", "Modification Successful", JOptionPane.INFORMATION_MESSAGE);
		}
		return true;	
	}
}
