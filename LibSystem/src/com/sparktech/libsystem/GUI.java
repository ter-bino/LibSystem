/*
 * This is the class that contains the main function
 * and the over all Graphic User Interface. Might be
 * confusing due to countless GUI components added
 * in the program. Improve code if possible.
 */

package com.sparktech.libsystem;

import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


public class GUI{

	//Components of GUI
	private static JFrame mainFrame;
	private static JLayeredPane layeredPane;
	private static JPanel _header, _booksPanel, _patronsPanel, _logsPanel, _aboutPanel, _bookButton, _patronButton, _logsButton, _aboutButton, bookTablePanel, patronTablePanel, logContentPanel, logHeaderPanel, bgPanel, appPanel, devPanel;
	private static JLabel xButton, _Button, bookButtonLabel, patronButtonLabel, logsButtonLabel, aboutButtonLabel, bookResultOf, patronResultOf, logHeaderIDLabel, logResultOf, appLogo, textArt, appLabel, appText, devLabel, devText;
	private static JButton addButton, borrowButton, returnButton, refreshButton, patronSearchButton, addPatronButton, patronLogButton, bookLogButton, deleteLogsButton, allLogsButton, logRefreshButton, delButton, modifyButton, delPatronButton, modPatronButton, changePassButton;
	private static JTextField bookSearchField, patronSearchField, logSearchField;
	private static JTable booksTable, patronsTable;
	private static JTextArea logsArea;
	private static JScrollPane logsPane, patronListPane, bookListPane;
	//The values where the size of the components are relative to
	private static final int APP_WIDTH = (int) (Menu.UNIVERSAL_WIDTH * 0.80), APP_HEIGHT = (int) (Menu.UNIVERSAL_HEIGHT * 0.80), TABLE_CELL_ROW_HEIGHT = 25;
	//For draggable header of the Undecorated Window
	private static int mouseX, mouseY;

	//main function of the whole program
	public static void main(String[] args) throws HeadlessException, IOException {
		//create the necessary files if they don't exist yet(first time running prgram)
		if (!Menu.$dir.exists()) {
			Menu.firstLunch();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new GUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		new Menu();//Make sure data is loaded to memory
	}
	
	//Calls Initialize Method
	private GUI() throws IOException {
		initialize();
	}

	//Method that Constructs the Main Graphical User Interface
	@SuppressWarnings("serial")
	private void initialize() throws IOException {
		mainFrame = new JFrame();
		mainFrame.setTitle("Library System");
		mainFrame.setUndecorated(true);
		mainFrame.getContentPane().setBackground(Color.DARK_GRAY);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setSize(APP_WIDTH, APP_HEIGHT);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		mainFrame.setIconImage(Menu.icon.getImage());
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		bgPanel = new FramePanel();
		bgPanel.setBackground(Color.DARK_GRAY);
		bgPanel.setVisible(true);
		bgPanel.setBounds(0,0,APP_WIDTH, APP_HEIGHT);
		bgPanel.setLayout(null);
		mainFrame.add(bgPanel);
		
		//header of the GUI
		_header = new JPanel();
		_header.setBackground(Color.BLACK);
		_header.setBounds(0, 0, APP_WIDTH, 34);
		_header.setLayout(null);
		_header.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				mainFrame.setLocation(mainFrame.getX() +  e.getX() - mouseX, mainFrame.getY() + e.getY() - mouseY);
			}
		});
		_header.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		bgPanel.add(_header);
		
		//Exit Button
		xButton = new JLabel("x");
		xButton.setForeground(Color.WHITE);
		xButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
		xButton.setBounds(APP_WIDTH - 20, 0, 20, 34);
		xButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		xButton.setVerticalAlignment(SwingConstants.TOP);
		_header.add(xButton);
		xButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		
		//Minimize Button
		_Button = new JLabel("-");
		_Button.setForeground(Color.WHITE);
		_Button.setFont(new Font("Tahoma", Font.PLAIN, 25));
		_Button.setBounds(APP_WIDTH - xButton.getWidth() - 23, 0, 23, 34);
		_Button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		_Button.setHorizontalAlignment(SwingConstants.CENTER);
		_header.add(_Button);
		_Button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainFrame.setState(Frame.ICONIFIED);
			}
		});
		
		appLogo = new JLabel();
		appLogo.setHorizontalAlignment(JLabel.CENTER);
		appLogo.setVerticalAlignment(JLabel.TOP);
		appLogo.setIcon(new ImageIcon(Menu.icon.getImage().getScaledInstance((int)(APP_HEIGHT*0.2399), (int)(APP_HEIGHT*0.2399), Image.SCALE_SMOOTH)));
		appLogo.setBounds((int)(APP_WIDTH*0.02999),34,(int)(APP_HEIGHT*0.2399),(int)(APP_HEIGHT*0.2399));
		appLogo.setVisible(true);
		bgPanel.add(appLogo);
		
		layeredPane = new JLayeredPane();
		layeredPane.setBounds((int)(APP_WIDTH*0.185), (int)((APP_HEIGHT - _header.getHeight())*0.013) + _header.getHeight(), (int)(APP_WIDTH*0.809), (int)(APP_HEIGHT*0.92));
		layeredPane.setLayout(new CardLayout(0, 0));
		bgPanel.add(layeredPane);
		
		
		//for Buttons within the Panels
		final int BUTTON_HEIGHT = 34;
		final int BUTTON_WIDTH = (int) (layeredPane.getWidth()*0.1639);
		
		//for panwel switcher button sizes and positioning
		final int PANEL_SWITCHER_WIDTH = (int)(APP_WIDTH*0.175);
		final int PANEL_SWITCHER_HEIGHT = 34;
		final int PANEL_SWITCHER_X = (int)(APP_WIDTH*0.185);
		final int PANEL_SWITCHER_Y = (int)(APP_HEIGHT*0.2399)+34;
		
		//button for switching to Book List Screen
		_bookButton = new JPanel();
		_bookButton.setBackground(Color.LIGHT_GRAY);
		_bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		_bookButton.setBounds(PANEL_SWITCHER_X-PANEL_SWITCHER_WIDTH, PANEL_SWITCHER_Y, PANEL_SWITCHER_WIDTH, PANEL_SWITCHER_HEIGHT);
		_bookButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				switchPanel(_booksPanel);
				_bookButton.setBackground(new Color(0xb0aea9));
			}
		});
		bgPanel.add(_bookButton);
		
		bookButtonLabel = new JLabel("LIST OF BOOKS");
		bookButtonLabel.setForeground(Color.WHITE);
		bookButtonLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
		bookButtonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		_bookButton.add(bookButtonLabel);
		
		
		//button for switching to Patron Screen
		_patronButton = new JPanel();
		_patronButton.setBackground(Color.GRAY);
		_patronButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		_patronButton.setBounds(PANEL_SWITCHER_X-PANEL_SWITCHER_WIDTH, PANEL_SWITCHER_Y+(PANEL_SWITCHER_HEIGHT+10), PANEL_SWITCHER_WIDTH, PANEL_SWITCHER_HEIGHT);
		_patronButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				switchPanel(_patronsPanel);
				_patronButton.setBackground(new Color(0xb0aea9));
			}
		});
		bgPanel.add(_patronButton);
		
		patronButtonLabel = new JLabel("PATRONS");
		patronButtonLabel.setForeground(Color.WHITE);
		patronButtonLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
		patronButtonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		_patronButton.add(patronButtonLabel);
		
		
		//button for switching to Logs Screen
		_logsButton = new JPanel();
		_logsButton.setBackground(Color.GRAY);
		_logsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		_logsButton.setBounds(PANEL_SWITCHER_X-PANEL_SWITCHER_WIDTH, PANEL_SWITCHER_Y+(PANEL_SWITCHER_HEIGHT+10)*2, PANEL_SWITCHER_WIDTH, PANEL_SWITCHER_HEIGHT);
		_logsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				switchPanel(_logsPanel);
				_logsButton.setBackground(new Color(0xb0aea9));
			}
		});
		bgPanel.add(_logsButton);
		
		logsButtonLabel = new JLabel("LOGS");
		logsButtonLabel.setForeground(Color.WHITE);
		logsButtonLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
		logsButtonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		_logsButton.add(logsButtonLabel);
		
		
		//button for switching to About Screen
		_aboutButton = new JPanel();
		_aboutButton.setBackground(Color.GRAY);
		_aboutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		_aboutButton.setBounds(PANEL_SWITCHER_X-PANEL_SWITCHER_WIDTH, PANEL_SWITCHER_Y+(PANEL_SWITCHER_HEIGHT+10)*3, PANEL_SWITCHER_WIDTH, PANEL_SWITCHER_HEIGHT);
		_aboutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				switchPanel(_aboutPanel);
				_aboutButton.setBackground(new Color(0xb0aea9));
			}
		});
		bgPanel.add(_aboutButton);
		
		aboutButtonLabel = new JLabel("ABOUT");
		aboutButtonLabel.setForeground(Color.WHITE);
		aboutButtonLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
		aboutButtonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		_aboutButton.add(aboutButtonLabel);
		
		textArt = new JLabel();
		textArt.setHorizontalAlignment(JLabel.CENTER);
		textArt.setVerticalAlignment(JLabel.TOP);
		textArt.setIcon(new ImageIcon(Menu.art.getImage().getScaledInstance((int)(APP_WIDTH*0.0999), (int)(APP_HEIGHT*(APP_HEIGHT*0.0999/APP_WIDTH)*8), Image.SCALE_SMOOTH)));
		textArt.setBounds(0,PANEL_SWITCHER_Y+(PANEL_SWITCHER_HEIGHT+10)*4,(int)(APP_WIDTH*0.1999), (int)(APP_HEIGHT*(APP_HEIGHT*0.0999/APP_WIDTH)*8));
		textArt.setVisible(true);
		bgPanel.add(textArt);
		
		
		///////////////////////////////////////////////////////
		////BLOCK OF CODE DEALING WITH THE BOOK LIST SCREEN////
		///////////////////////////////////////////////////////
		_booksPanel = new BackgroundPanel();
		_booksPanel.setSize(layeredPane.getWidth(), layeredPane.getHeight());
		_booksPanel.setLayout(null);
		layeredPane.add(_booksPanel);
		//panel that will contain the scrollpane/table
		bookTablePanel = new JPanel();
		bookTablePanel.setBackground(Color.GRAY);
		bookTablePanel.setBounds((int)(_booksPanel.getWidth()*0.045), (int)(_booksPanel.getHeight()*0.095), (int)(_booksPanel.getWidth()*0.895), (int)(_booksPanel.getHeight()*0.795));
		_booksPanel.add(bookTablePanel);
		bookTablePanel.setLayout(null);
		//scrollPane for the table
		bookListPane = new JScrollPane();
		bookListPane.setSize(bookTablePanel.getWidth(), bookTablePanel.getHeight());
		
		String[] bookHeaders = {"Book ID", "Book Title", "Book Author", "Available"};
		String[][] bookData = constructBookTable("");
		booksTable = new JTable();
		booksTable.setEnabled(false);
		booksTable.setForeground(Color.WHITE);
		booksTable.setBackground(Color.GRAY);
		booksTable.setRowHeight(TABLE_CELL_ROW_HEIGHT);
		booksTable.getTableHeader().setReorderingAllowed(false);
		booksTable.setFont(new Font("Century Gothic", Font.BOLD, 18));
		booksTable.setModel(new DefaultTableModel(bookData, bookHeaders));
		resizeTable(booksTable, bookListPane);
		bookListPane.setViewportView(booksTable);;
		bookTablePanel.add(bookListPane);
		
		//Add Book Button
		addButton = new JButton("ADD BOOK");
		addButton.setForeground(Color.WHITE);
		addButton.setBackground(SystemColor.controlDkShadow);
		addButton.setFont(new Font("Arial Narrow", Font.BOLD, 12));
		addButton.setBounds((int)(_booksPanel.getWidth()*0.03), _booksPanel.getHeight() - BUTTON_HEIGHT - 15, BUTTON_WIDTH, BUTTON_HEIGHT);
		addButton.setFocusable(false);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Menu.addBook();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		_booksPanel.add(addButton);
		
		//Borrow Book Button
		borrowButton = new JButton("BORROW BOOK");
		borrowButton.setForeground(Color.WHITE);
		borrowButton.setBackground(SystemColor.controlDkShadow);
		borrowButton.setFont(new Font("Arial Narrow", Font.BOLD, 12));
		borrowButton.setBounds((int)(_booksPanel.getWidth()*0.03)*2+BUTTON_WIDTH, _booksPanel.getHeight() - BUTTON_HEIGHT - 15, BUTTON_WIDTH, BUTTON_HEIGHT);
		borrowButton.setFocusable(false);
		borrowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Menu.borrowBook();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		_booksPanel.add(borrowButton);
		
		//Return Book Button
		returnButton = new JButton("RETURN BOOK");
		returnButton.setForeground(Color.WHITE);
		returnButton.setBackground(SystemColor.controlDkShadow);
		returnButton.setFont(new Font("Arial Narrow", Font.BOLD, 12));
		returnButton.setBounds((int)(_booksPanel.getWidth()*0.03)*3+BUTTON_WIDTH*2, _booksPanel.getHeight() - BUTTON_HEIGHT - 15, BUTTON_WIDTH, BUTTON_HEIGHT);
		returnButton.setFocusable(false);
		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Menu.returnBook();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		_booksPanel.add(returnButton);
		
		//Modify Book Button
		modifyButton = new JButton("MODIFY BOOK");
		getModifyButton().setForeground(Color.WHITE);
		getModifyButton().setBackground(SystemColor.controlDkShadow);
		getModifyButton().setFont(new Font("Arial Narrow", Font.BOLD, 12));
		getModifyButton().setBounds((int)(_booksPanel.getWidth()*0.03)*4+BUTTON_WIDTH*3, _booksPanel.getHeight() - BUTTON_HEIGHT - 15, BUTTON_WIDTH, BUTTON_HEIGHT);
		getModifyButton().setFocusable(false);
		getModifyButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getModifyButton().setEnabled(false);
				Menu.modifyBook();
			}
		});
		_booksPanel.add(getModifyButton());
		
		//Delete Book Button
		delButton = new JButton("DELETE BOOK");
		getDelButton().setForeground(Color.WHITE);
		getDelButton().setBackground(SystemColor.controlDkShadow);
		getDelButton().setFont(new Font("Arial Narrow", Font.BOLD, 12));
		getDelButton().setBounds((int)(_booksPanel.getWidth()*0.03)*5+BUTTON_WIDTH*4, _booksPanel.getHeight() - BUTTON_HEIGHT - 15, BUTTON_WIDTH, BUTTON_HEIGHT);
		getDelButton().setFocusable(false);
		getDelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getDelButton().setEnabled(false);
				Menu.deleteBook();
			}
		});
		_booksPanel.add(getDelButton());
		
		//Action when Hitting "Refresh/Search" or Enter in Search Field in BOOK LIST Screen
		Action action = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e){
				String key;
					if(bookSearchField.getText().equals("Enter search keywords for list filtering") || bookSearchField.getText().equals("")) {
						key = "";
						bookResultOf.setText("Showing all books in record");
					}
					else {
						key = bookSearchField.getText();
						try {
							if(mainFrame.getFocusOwner()!=bookSearchField)
							bookSearchField.setText("Enter search keywords for list filtering");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						bookResultOf.setText("Showing results for \"" + key + "\"");
					}
						
					booksTable.setModel(new DefaultTableModel(constructBookTable(key.toUpperCase()), bookHeaders));
					resizeTable(booksTable, bookListPane);
			}
		};
		
		//the Refresh/Search Button in the BOOK LIST Screen
		refreshButton = new JButton("SEARCH / REFRESH");
		refreshButton.setForeground(Color.WHITE);
		refreshButton.setBackground(Color.DARK_GRAY);
		refreshButton.setFont(new Font("Arial Narrow", Font.BOLD, 14));
		refreshButton.setBounds((int)(_booksPanel.getWidth()*0.07999), (int)(_booksPanel.getHeight()*0.01999), (int)(_booksPanel.getWidth()*0.20999), 25);
		refreshButton.setFocusPainted(false);
		refreshButton.addActionListener(action);
		_booksPanel.add(getRefreshButton());
		
		//the Search Field Area in BOOK LIST Screen
		bookSearchField = new JTextField("Enter search keywords for list filtering");
		bookSearchField.setForeground(Color.WHITE);
		bookSearchField.setBackground(Color.GRAY);
		bookSearchField.setFont(new Font("Arial Narrow", Font.ITALIC, 16));
		bookSearchField.setBounds((int)(_booksPanel.getWidth()*0.28999), (int)(_booksPanel.getHeight()*0.01999), (int)(_booksPanel.getWidth()*0.62999), 25);
		bookSearchField.setColumns(10);
		bookSearchField.addActionListener(action);
		bookSearchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				bookSearchField.setText("");
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(bookSearchField.getText().isBlank())
					bookSearchField.setText("Enter search keywords for list filtering");
				}
			});
		_booksPanel.add(bookSearchField);
		
		//Label that shows which results are currently shown in the book table
		bookResultOf = new JLabel("Showing all books in record");
		bookResultOf.setForeground(Color.WHITE);
		bookResultOf.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
		bookResultOf.setBounds((int)(_booksPanel.getWidth()*0.14999), (int)(_booksPanel.getHeight()*0.01999) + 27, (int)(_booksPanel.getWidth()*0.62999), 14);
		_booksPanel.add(bookResultOf);
				
		
		//////////////////////////////////////////////////////////
		////BLOCK OF CODE DEALING WITH THE PATRONS LIST SCREEN////
		//////////////////////////////////////////////////////////
		_patronsPanel = new BackgroundPanel();
		_patronsPanel.setSize(layeredPane.getWidth(), layeredPane.getHeight());
		_patronsPanel.setLayout(null);
		layeredPane.add(_patronsPanel);
		
		//Panel that Contains the patron table/ scrollpane
		patronTablePanel = new JPanel();
		patronTablePanel.setBackground(Color.GRAY);
		patronTablePanel.setBounds((int)(_patronsPanel.getWidth()*0.045), (int)(_patronsPanel.getHeight()*0.095), (int)(_patronsPanel.getWidth()*0.895), (int)(_patronsPanel.getHeight()*0.795));
		_patronsPanel.add(patronTablePanel);
		patronTablePanel.setLayout(null);
		
		patronListPane = new JScrollPane();
		patronListPane.setSize(patronTablePanel.getWidth(), patronTablePanel.getHeight());
		patronTablePanel.add(patronListPane);
		
		String[] patronHeaders = {"Patron ID", "Full Name", "Contact No.", "Unreturned"};
		String[][] patronData = constructPatronTable("");
		patronsTable = new JTable();
		patronsTable.setEnabled(false);
		patronsTable.setForeground(Color.WHITE);
		patronsTable.setBackground(Color.GRAY);
		patronsTable.setRowHeight(TABLE_CELL_ROW_HEIGHT);
		patronsTable.getTableHeader().setReorderingAllowed(false);
		patronsTable.setFont(new Font("Century Gothic", Font.BOLD, 18));
		patronsTable.setModel(new DefaultTableModel(patronData, patronHeaders));
		resizeTable(patronsTable, patronListPane);
		patronListPane.setViewportView(patronsTable);;
		patronTablePanel.add(patronListPane);
		
		//Add Patrons Button
		addPatronButton = new JButton("ADD PATRON");
		addPatronButton.setForeground(Color.WHITE);
		addPatronButton.setBackground(SystemColor.controlDkShadow);
		addPatronButton.setFont(new Font("Arial Narrow", Font.BOLD, 12));
		addPatronButton.setBounds((int)(_booksPanel.getWidth()*0.18999), _booksPanel.getHeight() - BUTTON_HEIGHT - 15, BUTTON_WIDTH, BUTTON_HEIGHT);
		addPatronButton.setFocusable(false);
		addPatronButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Menu.addPatron();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		_patronsPanel.add(addPatronButton);
		
		//Modift Patron Button
		modPatronButton = new JButton("MODIFY PATRON");
		modPatronButton.setForeground(Color.WHITE);
		modPatronButton.setBackground(SystemColor.controlDkShadow);
		modPatronButton.setFont(new Font("Arial Narrow", Font.BOLD, 12));
		modPatronButton.setBounds((int)(_patronsPanel.getWidth()*0.18999 + BUTTON_WIDTH + _patronsPanel.getWidth()*0.04999), _patronsPanel.getHeight() - BUTTON_HEIGHT - 15, BUTTON_WIDTH, BUTTON_HEIGHT);
		modPatronButton.setFocusable(false);
		modPatronButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Menu.modifyPatron();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		_patronsPanel.add(modPatronButton);
		
		//Delete Patron Button
		delPatronButton = new JButton("DELETE PATRON");
		delPatronButton.setForeground(Color.WHITE);
		delPatronButton.setBackground(SystemColor.controlDkShadow);
		delPatronButton.setFont(new Font("Arial Narrow", Font.BOLD, 12));
		delPatronButton.setBounds((int)(_patronsPanel.getWidth()*0.18999 + BUTTON_WIDTH*2 + _patronsPanel.getWidth()*0.09999), _patronsPanel.getHeight() - BUTTON_HEIGHT - 15, BUTTON_WIDTH, BUTTON_HEIGHT);
		delPatronButton.setFocusable(false);
		delPatronButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getDelPatronButton().setEnabled(false);
				Menu.deletePatron();
			}
		});
		_patronsPanel.add(getDelPatronButton());

		//Action when Hitting "Refresh/Search" or Enter in Search Field in PATRONS Screen
		Action action2 = new AbstractAction()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		    	String key;
				if(patronSearchField.getText().equals("Enter search keywords for list filtering") || patronSearchField.getText().equals("")) {
					key = "";
					patronResultOf.setText("Showing all patrons in record");
				}
				else {
					key = patronSearchField.getText();
					try {
						if(mainFrame.getFocusOwner()!=patronSearchField)
						patronSearchField.setText("Enter search keywords for list filtering");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					patronResultOf.setText("Showing results for \"" + key + "\"");
				}
				patronsTable.setModel(new DefaultTableModel(constructPatronTable(key.toUpperCase()), patronHeaders));
				resizeTable(patronsTable, patronListPane);		
		   }
		};
		
		patronSearchButton = new JButton("SEARCH/REFRESH");
		patronSearchButton.setForeground(Color.WHITE);
		patronSearchButton.setBackground(Color.DARK_GRAY);
		patronSearchButton.setFont(new Font("Arial Narrow", Font.BOLD, 14));
		patronSearchButton.setBounds((int)(_booksPanel.getWidth()*0.07999), (int)(_booksPanel.getHeight()*0.01999), (int)(_booksPanel.getWidth()*0.20999), 25);
		patronSearchButton.setFocusPainted(false);
		patronSearchButton.addActionListener(action2);
		_patronsPanel.add(patronSearchButton);
		
		patronSearchField = new JTextField("Enter search keywords for list filtering");
		patronSearchField.setForeground(Color.WHITE);
		patronSearchField.setBackground(Color.GRAY);
		patronSearchField.setFont(new Font("Arial Narrow", Font.ITALIC, 16));
		patronSearchField.setBounds((int)(_booksPanel.getWidth()*0.28999), (int)(_booksPanel.getHeight()*0.01999), (int)(_booksPanel.getWidth()*0.62999), 25);
		patronSearchField.setColumns(10);
		patronSearchField.addActionListener(action2);
		patronSearchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				patronSearchField.setText("");
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(patronSearchField.getText().isBlank())
					patronSearchField.setText("Enter search keywords for list filtering");
				}
			});
		_patronsPanel.add(patronSearchField);
		
		patronResultOf = new JLabel("Showing all patrons in record");
		patronResultOf.setForeground(Color.WHITE);
		patronResultOf.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
		patronResultOf.setBounds((int)(_patronsPanel.getWidth()*0.14999), (int)(_patronsPanel.getHeight()*0.01999) + 27, (int)(_patronsPanel.getWidth()*0.62999), 14);
		_patronsPanel.add(patronResultOf);
		
		///////////////////////////////////////////////////
		////BLOCK OF CODE DEALING WITH THE LOGS SCREEN/////
		///////////////////////////////////////////////////
		
		_logsPanel = new BackgroundPanel();
		_logsPanel.setLayout(null);
		_logsPanel.setSize(layeredPane.getWidth(), layeredPane.getHeight());
		layeredPane.add(_logsPanel);
		
		//Panel that Contains the Logs Scroll Pane
		logContentPanel = new JPanel();
		logContentPanel.setBackground(Color.GRAY);
		logContentPanel.setBounds((int)(_patronsPanel.getWidth()*0.045), (int)(_patronsPanel.getHeight()*0.095), (int)(_patronsPanel.getWidth()*0.895), (int)(_patronsPanel.getHeight()*0.795));
		logContentPanel.setLayout(null);
		_logsPanel.add(logContentPanel);
		
		//Header for the Content of the Logs
		logHeaderPanel = new JPanel();
		logHeaderPanel.setBackground(Color.DARK_GRAY);
		logHeaderPanel.setBounds(0, 0, logContentPanel.getWidth(), 25);
		logHeaderPanel.setLayout(null);
		logContentPanel.add(logHeaderPanel);
		//Label of the previously created header
		logHeaderIDLabel = new JLabel("Book/Patron ID | Logs information");
		logHeaderIDLabel.setForeground(Color.WHITE);
		logHeaderIDLabel.setFont(new Font("Century Gothic", Font.BOLD, 16));
		logHeaderIDLabel.setSize(logContentPanel.getWidth(), 25);
		logHeaderIDLabel.setHorizontalAlignment(SwingConstants.LEFT);
		logHeaderPanel.add(logHeaderIDLabel);
		
		//ScrollPane that Contains the Log
		logsPane = new JScrollPane();
		logsPane.setBounds(0, 25, logContentPanel.getWidth(), logContentPanel.getHeight()-25);
		logContentPanel.add(logsPane);
		
		//TextArea which prints the contents of the logs
		logsArea = new JTextArea();
		logsArea.setBackground(Color.GRAY);
		logsArea.setFont(new Font("Century Gothic", Font.BOLD, 16));
		logsArea.setForeground(Color.WHITE);
		logsArea.setEditable(false);
		logsArea.setText(Menu.getLogs(""));
		logsPane.setViewportView(logsArea);
		
		//Button for Displaying Logs Sorted By Patrons
		patronLogButton = new JButton("BY PATRONS");
		patronLogButton.setForeground(Color.WHITE);
		patronLogButton.setBackground(SystemColor.controlDkShadow);
		patronLogButton.setFont(new Font("Century", Font.BOLD, 10));
		patronLogButton.setBounds((int)(_logsPanel.getWidth()*0.06799), _logsPanel.getHeight() - BUTTON_HEIGHT - 15, BUTTON_WIDTH, BUTTON_HEIGHT);
		patronLogButton.setFocusable(false);
		_logsPanel.add(patronLogButton);
		patronLogButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					logsArea.setText(Menu.getLogs("^LIB"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//Button for Displaying Logs Sorted By Books
		bookLogButton = new JButton("BY BOOKS");
		bookLogButton.setForeground(Color.WHITE);
		bookLogButton.setBackground(SystemColor.controlDkShadow);
		bookLogButton.setFont(new Font("Century", Font.BOLD, 10));
		bookLogButton.setBounds((int)(_logsPanel.getWidth()*0.06799*2 + BUTTON_WIDTH), _logsPanel.getHeight() - BUTTON_HEIGHT - 15, BUTTON_WIDTH, BUTTON_HEIGHT);
		bookLogButton.setFocusable(false);
		_logsPanel.add(bookLogButton);
		bookLogButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					logsArea.setText(Menu.getLogs("^20"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//Button for displaying all Logs
		allLogsButton = new JButton("ALL LOGS");
		allLogsButton.setForeground(Color.WHITE);
		allLogsButton.setBackground(SystemColor.controlDkShadow);
		allLogsButton.setFont(new Font("Century", Font.BOLD, 10));
		allLogsButton.setBounds((int)(_logsPanel.getWidth()*0.06799*3 + BUTTON_WIDTH*2), _logsPanel.getHeight() - BUTTON_HEIGHT - 15, BUTTON_WIDTH, BUTTON_HEIGHT);
		allLogsButton.setFocusable(false);
		_logsPanel.add(allLogsButton);
		allLogsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					logsArea.setText(Menu.getLogs(""));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//Button for Deleting All Existing Logs
		deleteLogsButton = new JButton("DELETE LOGS");
		deleteLogsButton.setForeground(Color.WHITE);
		deleteLogsButton.setBackground(SystemColor.controlDkShadow);
		deleteLogsButton.setFont(new Font("Century", Font.BOLD, 10));
		deleteLogsButton.setBounds((int)(_logsPanel.getWidth()*0.06799*4 + BUTTON_WIDTH*3), _logsPanel.getHeight() - BUTTON_HEIGHT - 15, BUTTON_WIDTH, BUTTON_HEIGHT);
		deleteLogsButton.setFocusable(false);
		_logsPanel.add(deleteLogsButton);
		deleteLogsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Menu.deleteLogs();
				} catch (IOException e1) {
					e1.printStackTrace();
				}finally {
					try {
						logsArea.setText(Menu.getLogs(""));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		//Action when searching/filtering Logs
		Action action3 = new AbstractAction()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		    	String key;
				if(logSearchField.getText().equals("Enter search keywords for logs filtering") || logSearchField.getText().equals("")) {
					key = "";
					logResultOf.setText("Showing all logs in record");
				}
				else {
					key = logSearchField.getText();
					try {
						if(mainFrame.getFocusOwner()!=logSearchField)
						logSearchField.setText("Enter search keywords for logs filtering");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					logResultOf.setText("Showing results for \"" + key + "\"");
				}
				try {
					logsArea.setText(Menu.getLogs(key));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
		    }
		};
		
		logRefreshButton = new JButton("SEARCH/REFRESH");
		logRefreshButton.setForeground(Color.WHITE);
		logRefreshButton.setBackground(Color.DARK_GRAY);
		logRefreshButton.setFont(new Font("Century Gothic", Font.BOLD, 14));
		logRefreshButton.setBounds((int)(_logsPanel.getWidth()*0.07999), (int)(_logsPanel.getHeight()*0.01999), (int)(_logsPanel.getWidth()*0.20999), 25);
		logRefreshButton.setFocusPainted(false);
		logRefreshButton.addActionListener(action3);
		_logsPanel.add(logRefreshButton);
		
		logSearchField = new JTextField("Enter search keywords for logs filtering");
		logSearchField.setForeground(Color.WHITE);
		logSearchField.setBackground(Color.GRAY);
		logSearchField.setFont(new Font("Century", Font.ITALIC, 16));
		logSearchField.setBounds((int)(_logsPanel.getWidth()*0.28999), (int)(_logsPanel.getHeight()*0.01999), (int)(_logsPanel.getWidth()*0.62999), 25);
		logSearchField.setColumns(10);
		logSearchField.addActionListener(action3);
		_logsPanel.add(logSearchField);
		logSearchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				logSearchField.setText("");
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(logSearchField.getText().isBlank())
					logSearchField.setText("Enter search keywords for logs filtering");
			}
		});
		
		logResultOf = new JLabel("Showing all borrow and return logs");
		logResultOf.setForeground(Color.WHITE);
		logResultOf.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
		logResultOf.setBounds((int)(_logsPanel.getWidth()*0.14999), (int)(_logsPanel.getHeight()*0.01999) + 27, (int)(_logsPanel.getWidth()*0.62999), 14);
		_logsPanel.add(logResultOf);
		
		///////////////////////////////////////////////////
		////BLOCK OF CODE DEALING WITH THE ABOUT SCREEN////
		///////////////////////////////////////////////////
		_aboutPanel = new BackgroundPanel();
		_aboutPanel.setSize(layeredPane.getWidth(), layeredPane.getHeight());
		_aboutPanel.setLayout(null);
		layeredPane.add(_aboutPanel);
		
		final int boxWidth = (int)(_aboutPanel.getWidth()*0.3499), boxHeight = (int)(_aboutPanel.getHeight()*0.7999);
		
		//Panel about App
		appPanel = new FramePanel();
		appPanel.setBounds((int)(_aboutPanel.getWidth()*0.0999), (int)(_aboutPanel.getHeight()*0.0999), boxWidth, boxHeight);
		appPanel.add(new JLabel("_________________________________________________________________________________"));//to get 1 spacing lmao
		_aboutPanel.add(appPanel);
		
		//Application Logo
		appLabel = new JLabel();
		appLabel.setForeground(Color.WHITE);
		appLabel.setFont(new Font("Century Gothic", Font.BOLD, (int)(boxHeight*0.0499)));
		appLabel.setIcon(new ImageIcon(Menu.icon.getImage().getScaledInstance((int)(APP_HEIGHT*0.2399), (int)(APP_HEIGHT*0.2399), Image.SCALE_SMOOTH)));
		appLabel.setText("Lib-System");
		appLabel.setVerticalTextPosition(JLabel.BOTTOM);
		appLabel.setHorizontalTextPosition(JLabel.CENTER);
		appPanel.add(appLabel);
		
		//About Application
		appText = new JLabel();
		appText.setForeground(Color.WHITE);
		appText.setFont(new Font("Century Gothic", Font.PLAIN, (int)(boxHeight*0.02499)));
		appText.setSize(boxWidth, boxHeight);
		appText.setText("<html><br><center>The Library System application is a program created</center><br>"
				+ "<center>specifically for tracking different transactions in</center><br>"
				+ "<center>a library. This tracked transactions includes but not</center><br>"
				+ "<center>limited to adding, deleting, borrowing and returning</center><br>"
				+ "<center>books. The functions are implemented through books</center><br>"
				+ "<center>and patrons system.</center></html>");
		appPanel.add(appText);
		
		//Panel about developer
		devPanel = new FramePanel();
		devPanel.setBounds(boxWidth + (int)(_aboutPanel.getWidth()*0.0999)*2, (int)(_aboutPanel.getHeight()*0.0999), boxWidth, boxHeight);
		devPanel.add(new JLabel("_________________________________________________________________________________"));//to get 1 spacing lmao
		_aboutPanel.add(devPanel);
		
		//Developer Logo
		devLabel = new JLabel();
		devLabel.setForeground(Color.WHITE);
		devLabel.setFont(new Font("Century Gothic", Font.BOLD, (int)(boxHeight*0.0499)));
		devLabel.setIcon(new ImageIcon(Menu.devLogo.getImage().getScaledInstance((int)(APP_HEIGHT*0.2399), (int)(APP_HEIGHT*0.2399), Image.SCALE_SMOOTH)));
		devLabel.setText("Spark Tech");
		devLabel.setVerticalTextPosition(JLabel.BOTTOM);
		devLabel.setHorizontalTextPosition(JLabel.CENTER);
		devPanel.add(devLabel);
		
		//About Developer
		devText = new JLabel();
		devText.setForeground(Color.WHITE);
		devText.setFont(new Font("Century Gothic", Font.PLAIN, (int)(boxHeight*0.02499)));
		devText.setSize(boxWidth, boxHeight);
		devText.setText("<html><br><center>This application is developed by Spark Tech (I made</center><br>"
				+ "<center>up that name just now)! Developed within 7 days for</center><br>"
				+ "<center>practice purposes but anyone can use it if they want.</center><br>"
				+ "<center>Though I doubt someone will use this at all D:. The</center><br>"
				+ "<center>records of this application are managed through file</center><br>"
				+ "<center>processing. I'm not yet familiar with databases during</center><br>"
				+ "<center>the making of this program.</center></html>");
		devPanel.add(devText);
		
		//Borrow Book Button
		changePassButton = new JButton("CHANGE PASS");
		changePassButton.setForeground(Color.WHITE);
		changePassButton.setBackground(SystemColor.controlDkShadow);
		changePassButton.setFont(new Font("Arial Narrow", Font.BOLD, 12));
		changePassButton.setBounds((int)(_aboutPanel.getWidth()-BUTTON_WIDTH)/2, _aboutPanel.getHeight() - BUTTON_HEIGHT - 15, BUTTON_WIDTH, BUTTON_HEIGHT);
		changePassButton.setFocusable(false);
		changePassButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Menu.changePass();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		_aboutPanel.add(changePassButton);
		
		
		//end of Initialize()
		}

	//Method to get data for Books Table
	private String[][] constructBookTable(String key) {
		ArrayList <String[]> books = new ArrayList <String[]>();
		ArrayList<String> keys = new ArrayList<String>(Menu.books.keySet());
		Collections.sort(keys);
		
		if(keys.contains(key))
			InfoUI.make(Menu.books.get(key));
		else {
			for(String id: keys) {
				BookData i = Menu.books.get(id);
				if(i.getTitle().toUpperCase().contains(key) || i.getAuthor().toUpperCase().contains(key))
					books.add(new String[]{i.getID(), i.getTitle(), i.getAuthor(), i.getRecord().getStored()+""});
			}
			String[][] data = new String[books.size()][];
			for(int i=0; i<data.length; i++) {
				data[i] = books.get(i);
			}
			return data;
		}
		return constructBookTable("");
	}
	
	//Method to get data for Patron Table
	private String[][] constructPatronTable(String key) {
		ArrayList <String[]> patrons = new ArrayList <String[]>();
		ArrayList<String> keys = new ArrayList<String>(Menu.patrons.keySet());
		Collections.sort(keys);
		
		if(keys.contains(key))
			InfoUI.make(Menu.patrons.get(key));
		else {
			for(String id: keys) {
				PatronData i = Menu.patrons.get(id);
				if(i.getName().toUpperCase().contains(key) || i.getContact().toUpperCase().contains(key))
					patrons.add(new String[]{i.getID(), i.getName(), i.getContact(), i.getUnreturned()+""});
			}	
			String[][] data = new String[patrons.size()][];
			for(int i=0; i<data.length; i++) {
				data[i] = patrons.get(i);
			}
			return data;
		}
		return constructPatronTable("");
	}

	//Method for switch panels in the LayeredPane
	public void switchPanel(JPanel panel) {
		layeredPane.removeAll();
		layeredPane.add(panel);
		layeredPane.repaint();
		layeredPane.revalidate();
		_bookButton.setBackground(Color.GRAY);
		_patronButton.setBackground(Color.GRAY);
		_logsButton.setBackground(Color.GRAY);
		_aboutButton.setBackground(Color.GRAY);
	}
	
	//Method for resizing the columns of tables in Book and Patron Screen
	public static void resizeTable(JTable table, JScrollPane pane) {
		table.getColumnModel().getColumn(0).setPreferredWidth((int)(pane.getWidth()*0.16));
		table.getColumnModel().getColumn(1).setPreferredWidth((int)(pane.getWidth()*0.43));
		table.getColumnModel().getColumn(2).setPreferredWidth((int)(pane.getWidth()*0.29));
		table.getColumnModel().getColumn(3).setPreferredWidth((int)(pane.getWidth()*0.08));
	}

	//getter for buttons that need accessing from outside of GUI class
	public static JButton getModifyButton() {
		return modifyButton;
	}
	public static JButton getDelButton() {
		return delButton;
	}
	public static JButton getDelPatronButton() {
		return delPatronButton;
	}
	public static JButton getRefreshButton() {
		return refreshButton;
	}
	public static JButton getPatronSearchButton() {
		return patronSearchButton;
	}
	public static JButton getModPatronButton() {
		return modPatronButton;
	}
	public static JButton getLogRefreshButton() {
		return logRefreshButton;
	}
	
	//For refreshing content when doing actions like adding, returning etc.
	public static void refreshAll() {
		refreshButton.doClick();
		patronSearchButton.doClick();
		logRefreshButton.doClick();
	}
}
//For Content Panels with Background Images
@SuppressWarnings("serial")
class BackgroundPanel extends JPanel{
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(Menu.bg.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
	}
}

//For Gray Panels with Black Borders(an image)
@SuppressWarnings("serial")
class FramePanel extends JPanel{
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(Menu.frame.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
	}
}
