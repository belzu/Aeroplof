package presentation;

import service.*;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
//import business_logic.FlightManager;
//import business_logic.IFlightManager;
//import data_access.DbAccessManager;
//import domain.ConcreteFlight;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;

public class FlightBookingGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel panel;

	private JLabel departureLabel = new JLabel("Departure city:");
	private JTextField departureInput = new JTextField();
	private JLabel arrivalLabel = new JLabel("Arrival city:");
	private JTextField arrivalInput = new JTextField();

	private JLabel yearLabel = new JLabel("Year:");
	private JTextField yearInput = new JTextField();
	private JLabel monthLabel = new JLabel("Month:");
	private JComboBox<String> monthCombo;
	private JLabel dayLabel = new JLabel("Day:");
	private JTextField dayInput = new JTextField();

	private JLabel fareLabel = new JLabel("Fare:");
	private JRadioButton firstRB = new JRadioButton("First class", true); 
	private JRadioButton businessRB = new JRadioButton("Business class", false);
	private JRadioButton economyRB = new JRadioButton("Economy class", false);
//	private JRadioButton selectedRB;
	private ButtonGroup fareButtonGroup = new ButtonGroup();

	private JButton searchConFlightsButton = new JButton("Search matching flights");
	private JLabel searchResultAnswer = new JLabel("", 0);

	private DefaultListModel<ConcreteFlight> conFlightInfo = new DefaultListModel<ConcreteFlight>();	
	private JList<ConcreteFlight> conFlightList = new JList<ConcreteFlight>(conFlightInfo);
	private JScrollPane conFlightListScrollPane = new JScrollPane();

	private JButton bookSelectedConFlightButton = new JButton("");
//	private IFlightManager businessLogic;
	private WebServiceLogicInterface businessLogic;
	private ConcreteFlight selectedConFlight;
	private final JTextField txtNtickets = new JTextField();
	private final JLabel lblNTickets = new JLabel("N\u00BA tickets:");
	private final JLabel logo = new JLabel();
	JLabel lblNtickets = new JLabel(" ");
	private final JLabel lblNtickets2 = new JLabel(" ");
	private final JLabel lblNtickets3 = new JLabel(" ");
	private URL url;
	private QName qname;
	private Service service;
	private WebServiceLogicInterface wsl;

	/**
	 * setupInputComponents method (1st block of standard constructor)
	 * 
	 * It configures and adds to the GUI's panel all elements needed to
	 * capture the input options of the user (flight route, date and fare)
	 */
	private void setupInputComponents() {
		
		String [] monthNames = {"January", "February", "March","April", "May",
				"June", "July", "August", "September", "October", "November",
				"December"};
		DefaultComboBoxModel<String> cbContent = new DefaultComboBoxModel<String>(monthNames);
		this.monthCombo = new JComboBox<String>(cbContent);

		this.fareButtonGroup.add(firstRB);
		this.fareButtonGroup.add(businessRB);
		this.fareButtonGroup.add(economyRB);
	}

	
	private void setup() {
	    try {
//	    	http://localhost:8080/ws?wsdl
//	    	http://169.254.254.62:8080/ws?wsdl
	        this.url = new URL("http://localhost:8080/ws?wsdl");
	        this.qname =  new QName("http://service/","WebServiceLogicService");
	        this.service = Service.create(url, qname);
	        this.wsl  = service.getPort(WebServiceLogicInterface.class);
	    } catch (MalformedURLException ex) {
	    	JOptionPane.showMessageDialog(new JFrame(),"ERROR CONNECTING TO THE SERVER\nPlease get sure that the server is running","ERROR",JOptionPane.ERROR_MESSAGE);
	    	System.out.println("Error");
	        throw new RuntimeException(ex);	        
	    }
	    businessLogic = wsl;
	}
	private void setup(String s) {
	    try {
//	    	http://localhost:8080/ws?wsdl
//	    	http://169.254.254.62:8080/ws?wsdl
	        this.url = new URL(s);
	        this.qname =  new QName("http://service/","WebServiceLogicService");
	        this.service = Service.create(url, qname);
	        this.wsl  = service.getPort(WebServiceLogicInterface.class);
	    } catch (MalformedURLException ex) {
	    	JOptionPane.showMessageDialog(new JFrame(),"ERROR CONNECTING TO THE SERVER\nPlease get sure that the server is running","ERROR",JOptionPane.ERROR_MESSAGE);
	    	System.out.println("Error");
	        throw new RuntimeException(ex);	        
	    }
	    businessLogic = wsl;
	}
	/**
	 * setupSearchFlight method (2nd block of standard constructor)
	 * 
	 * It configures, provides behavior and adds to the  GUI's panel a) the
	 * button that starts the search of the concrete flights that match the 
	 * user's criteria, and b) the label "searchResultAnswer" that publishes 
	 * the answers of the system to the user within each search.
	 * 
	 * The button sends a request to the business logic to fetch a list of 
	 * concrete flights, which then is loaded into "conFlightInfo", the content
	 * of the JList "conFlightList" used to display the flights and allow 
	 * their selection by the user.
	 */
	private void setupSearchFlightsButton() {

		this.searchConFlightsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conFlightInfo.clear();

				String chosenDateString = monthCombo.getSelectedItem() + " " + 
						dayInput.getText() + " " + yearInput.getText();
				SimpleDateFormat format = new SimpleDateFormat("MMMM' 'd' 'yyyy", Locale.ENGLISH);
				format.setLenient(false);

				try {
					Date chosenDate = format.parse(chosenDateString);
					List<ConcreteFlight> foundConFlights = businessLogic.
							getMatchingConFlights(departureInput.getText(), 
									arrivalInput.getText(), chosenDate);	
					for (ConcreteFlight v : foundConFlights)
						conFlightInfo.addElement(v);

					if (foundConFlights.isEmpty())
						searchResultAnswer.setText("No matching flights found. " +
								"Please change your options");
					else
						searchResultAnswer.setText("Choose an available flight" +
								" in the following list:");
				}
				catch(ParseException pe) {
					searchResultAnswer.setText("The chosen date " + chosenDateString + 
							" is not valid. Please correct it");
				}
			}
		});
//		refresh();
	}

	/**
	 * setupConflightList method (3rd block of standard constructor)
	 * 
	 * It configures, provides behavior (including scrollability) and adds to 
	 * the GUI's panel the "conFlightList" Jlist that displays the found 
	 * matching concrete flights and allow their selection by the user.
	 * 
	 * When the user selects a flight the "bookSelectedConFlightButton" is
	 * enabled and displays an invitation to book it
	 */
	private void setupConFlightList() {	

		this.conFlightList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				if (!conFlightList.isSelectionEmpty()) { 						
					selectedConFlight = (ConcreteFlight) conFlightList.getSelectedValue();
					refresh();
					if(firstRB.isSelected()){
						if(selectedConFlight.getFreeFirstSeatNo() == 0) {
							searchResultAnswer.setText("No available tickets in this fare");
							bookSelectedConFlightButton.setVisible(false);
							bookSelectedConFlightButton.setEnabled(false);
						}
						else {
							bookSelectedConFlightButton.setEnabled(true);
							bookSelectedConFlightButton.setVisible(true);
							bookSelectedConFlightButton.setText("Book a ticket in selected flight");
						}				
					}
					else if(businessRB.isSelected()){
						if(selectedConFlight.getFreeBusinessSeatNo() == 0) {
							searchResultAnswer.setText("No available tickets in this fare");
							bookSelectedConFlightButton.setVisible(false);
							bookSelectedConFlightButton.setEnabled(false);
						}
						else {
							bookSelectedConFlightButton.setEnabled(true);
							bookSelectedConFlightButton.setVisible(true);
							bookSelectedConFlightButton.setText("Book a ticket in selected flight");
						}				
					}
					else {
						if(selectedConFlight.getFreeEconomySeatNo() == 0) {
							searchResultAnswer.setText("No available tickets in this fare");
							bookSelectedConFlightButton.setVisible(false);
							bookSelectedConFlightButton.setEnabled(false);
						}
						else {
							bookSelectedConFlightButton.setEnabled(true);
							bookSelectedConFlightButton.setVisible(true);
							bookSelectedConFlightButton.setText("Book a ticket in selected flight");
						}					
					}
				}
//				refresh();
			}
		});
		this.conFlightListScrollPane.setViewportView(conFlightList);
	}

	/**
	 * setupBookSelectedConFlightButton method (4th block of standard constructor)
	 * 
	 * It configures, provides behavior and adds to the  GUI's panel the
	 * button that books the concrete flight selected by the user. Normally
	 * disabled, excepting when the user's choice takes place.
	 */
	private void setupBookSelectedConFlightButton() {
		this.bookSelectedConFlightButton.setVisible(true);
		this.bookSelectedConFlightButton.setEnabled(false);
		this.bookSelectedConFlightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ntickets = 0;
				try {
					ntickets = Integer.parseInt(txtNtickets.getText());
				}
				catch(Exception pe){
					ntickets = 0;
				}			
				int remaining = 0;
				if (firstRB.isSelected()) {
					remaining = businessLogic.bookSeat(selectedConFlight, "First",ntickets);
//					lblNtickets.setText(Integer.toString(selectedConFlight.getFreeFirstSeatNo()));
				}
				else if (businessRB.isSelected()) {
					remaining = businessLogic.bookSeat(selectedConFlight, "Business",ntickets);
//					lblNtickets.setText(Integer.toString(selectedConFlight.getFreeBusinessSeatNo()));
				}
				else if (economyRB.isSelected()) {
					remaining = businessLogic.bookSeat(selectedConFlight, "Economy",ntickets);
//					lblNtickets.setText(Integer.toString(selectedConFlight.getFreeEconomySeatNo()));
				}
				if (remaining < 0) 
					bookSelectedConFlightButton.
					setText("Error: This flight had no ticket for the requested fare!");
				else
					bookSelectedConFlightButton.
					setText(ntickets +" tickets have been booked. Remaining tickets = " + remaining);
				bookSelectedConFlightButton.setEnabled(false);
				refresh();
			}
		});
		
//		refresh();
	}
	
	
	private void refresh() {
		if(firstRB.isSelected()) {
			if(selectedConFlight.getFreeFirstSeatNo()>0) searchResultAnswer.setText("This flight is available!");
			else searchResultAnswer.setText("No available tickets in this fare");
		}
		else if(businessRB.isSelected()) {
			if(selectedConFlight.getFreeBusinessSeatNo()>0) searchResultAnswer.setText("This flight is available!");
			else searchResultAnswer.setText("No available tickets in this fare");
		}
		else if(economyRB.isSelected()) {
			if(selectedConFlight.getFreeEconomySeatNo()>0) searchResultAnswer.setText("This flight is available!");
			else searchResultAnswer.setText("No available tickets in this fare");
		}
//		else lblNtickets.setText("Select a concrete flight with a fare");
		lblNtickets.setText(Integer.toString(selectedConFlight.getFreeFirstSeatNo()) + " > First class");
		lblNtickets2.setText(Integer.toString(selectedConFlight.getFreeBusinessSeatNo())  + " > Business class");
		lblNtickets3.setText(Integer.toString(selectedConFlight.getFreeEconomySeatNo()) + " > Economic class");
		
	}
	
	
	private void layoutComponents() {
		logo.setIcon(new ImageIcon("C:\\Users\\ignacio\\eclipse-workspace\\Bum2\\images\\AeroplofLogo.jpg"));
		
		JLabel lblNAvailableTickets = new JLabel("N\u00BA available tickets");
		

		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(departureLabel, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(departureInput, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(arrivalLabel, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(arrivalInput, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(yearLabel)
					.addGap(14)
					.addComponent(yearInput, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(monthLabel)
					.addGap(5)
					.addComponent(monthCombo, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(dayLabel)
					.addGap(5)
					.addComponent(dayInput, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(fareLabel)
					.addGap(47)
					.addComponent(firstRB)
					.addGap(25)
					.addComponent(businessRB)
					.addGap(43)
					.addComponent(economyRB))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(40)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(searchConFlightsButton, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
						.addComponent(searchResultAnswer, GroupLayout.PREFERRED_SIZE, 325, GroupLayout.PREFERRED_SIZE))
					.addGap(31)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNTickets)
						.addComponent(txtNtickets, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)))
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(conFlightListScrollPane, GroupLayout.PREFERRED_SIZE, 405, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblNAvailableTickets))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNtickets2, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
								.addComponent(lblNtickets, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
								.addComponent(lblNtickets3, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))))
					.addGap(46))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(40)
					.addComponent(bookSelectedConFlightButton, GroupLayout.PREFERRED_SIZE, 325, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(32)
					.addComponent(logo, GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
					.addGap(10))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(2)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addComponent(departureLabel))
						.addComponent(departureInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(10)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addComponent(arrivalLabel))
						.addComponent(arrivalInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(10)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addComponent(yearLabel))
						.addComponent(yearInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addComponent(monthLabel))
						.addComponent(monthCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addComponent(dayLabel))
						.addComponent(dayInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(4)
							.addComponent(fareLabel))
						.addComponent(firstRB)
						.addComponent(businessRB)
						.addComponent(economyRB))
					.addGap(7)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(searchConFlightsButton)
							.addGap(6)
							.addComponent(searchResultAnswer, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(1)
							.addComponent(lblNTickets)
							.addGap(6)
							.addComponent(txtNtickets, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(5)
							.addComponent(conFlightListScrollPane, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(15)
							.addComponent(lblNAvailableTickets)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblNtickets)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNtickets2)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lblNtickets3)))
					.addGap(15)
					.addComponent(bookSelectedConFlightButton)
					.addGap(6)
					.addComponent(logo, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE))
		);
		panel.setLayout(gl_panel);
	}

	/**
	 * FlightBookingGUI
	 * 
	 * Default constructor of the GUI designed to implement the use case 
	 * "Select Flight"
	 */
	public FlightBookingGUI() {
		super("Book flights");
		try 
        {
            setIconImage(Toolkit.getDefaultToolkit().getImage("logo/plane.png"));
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
		txtNtickets.setColumns(10);
		this.setup();
		setTitle("AeroPlof | Connected to: "+ this.url);
		this.panel = new JPanel();
		setContentPane(panel);
		this.setupInputComponents();
		this.setupSearchFlightsButton();
		this.setupConFlightList();
		this.setupBookSelectedConFlightButton();
		this.layoutComponents();
		this.setSize(568, 568);
	}
	public FlightBookingGUI(String s) {
		super("Book flights");
		try 
        {
            setIconImage(Toolkit.getDefaultToolkit().getImage("logo/plane.png"));
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
		txtNtickets.setColumns(10);
		this.setup(s);
		setTitle("AeroPlof | Connected to: "+ this.url);
		this.panel = new JPanel();
		setContentPane(panel);
		this.setupInputComponents();
		this.setupSearchFlightsButton();
		this.setupConFlightList();
		this.setupBookSelectedConFlightButton();
		this.layoutComponents();
		this.setSize(568, 568);
	}

	/**
	 * Method setBusinessLogic
	 * 
	 * @param g      the business logic controller 
	 *               (it must implement the interface IFlightManager)
	 */
	public void setBusinessLogic(WebServiceLogicInterface g) {
		businessLogic = g;
	}

	public static void main(String[] args) {	
		try {
            Process p = Runtime.getRuntime().exec("netsh advfirewall set global StatefulFTP disable");
            p.waitFor();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        }
		catch (IOException e1) {
        }
		catch (InterruptedException e2){
        }
		
		ClientSetup pe = new ClientSetup();
		String s;
		pe.setVisible(true);
		while(pe.btnExecuteServer.isEnabled())
		{
			s = pe.txtHost.getText();			
		}
		try {
		pe.setVisible(false);
		FlightBookingGUI frame = new FlightBookingGUI();
		frame.setBusinessLogic(frame.wsl);
		frame.setVisible(true);
		frame.pack();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),"ERROR CONNECTING TO THE SERVER\nPlease get sure that the server is running","ERROR",JOptionPane.ERROR_MESSAGE);
		}
	}
}