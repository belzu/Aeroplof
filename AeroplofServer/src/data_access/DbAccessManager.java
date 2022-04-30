package data_access;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import service.WebServiceLogicInterface;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import domain.*;
public class DbAccessManager {
	private EntityManager dbConnector;
	private EntityManagerFactory emf;
	String fileName = "db/Flights2.odb";
	
	public DbAccessManager() {
		emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
		dbConnector = emf.createEntityManager();
		System.out.println("DataBase opened");
	}
	// later we will add further operations here
	public void closeDb() {
		dbConnector.close();
		System.out.println("DataBase closed");
	}
	public void storeFlight(Flight flight) {
		dbConnector.getTransaction().begin();
		dbConnector.persist(flight);
		dbConnector.getTransaction().commit();
		System.out.println("The flight with the code: "+ flight.getFlightCode() + " has been saved");
	}

	public List<Flight> getAllFlights() {
		TypedQuery<Flight> q1 = dbConnector.createQuery("SELECT p FROM Flight p",
				Flight.class);
		List<Flight> flights = q1.getResultList();
		return flights;
	}
	public List<ConcreteFlight> getAllConcreteFlights() {
		TypedQuery<ConcreteFlight> q1 = dbConnector.createQuery("SELECT p FROM ConcreteFlight p",
		ConcreteFlight.class);
		List<ConcreteFlight> flights = q1.getResultList();
		return flights;
	}
	public List<ConcreteFlight> getConcreteFlights(String cfCode) {
		TypedQuery<ConcreteFlight> q1 = dbConnector.createQuery("SELECT p FROM ConcreteFlight " +
		"p WHERE p.cfCode = \"" + cfCode + "\"", ConcreteFlight.class);
		List<ConcreteFlight> flights = q1.getResultList();
		return flights;
	}

	public List<Flight> getFlightByCities(String arrCity,String depCity) {		
		TypedQuery<Flight> q2 = dbConnector.createQuery("SELECT p FROM Flight " +
		"p WHERE p.arrivalCity = \"" + arrCity + "\"" + " AND p.departureCity = \"" + depCity + "\"",
			Flight.class);
		List<Flight> flights = q2.getResultList();
		return flights;
	}
	public List<ConcreteFlight> getConcreteFlightsByCities(String arrCity,String depCity) {
		TypedQuery<ConcreteFlight> q2 = dbConnector.createQuery("SELECT p FROM ConcreteFlight "+ 
		"p WHERE p.flight.arrivalCity = \"" + arrCity + "\"" + " AND p.flight.departureCity = \"" + depCity + "\"",
			ConcreteFlight.class);	
		List<ConcreteFlight> flights = q2.getResultList();
		return flights;
	}
	
	
	
	
	
	
	
	public List<ConcreteFlight> getFlightByCitiesDate(String arrCity,String depCity, Date date) {
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int year  = localDate.getYear();
		int month = localDate.getMonthValue();
		int day   = localDate.getDayOfMonth();
	
		TypedQuery<ConcreteFlight> q2 = dbConnector.createQuery("SELECT p FROM ConcreteFlight "+ 
		"p WHERE p.flight.arrivalCity = \"" + arrCity + "\"" + " AND p.flight.departureCity = \"" + depCity + "\"" +
		" AND YEAR(p.date) =" + year + " AND MONTH(p.date) = " + month + " AND DAY(p.date) = " + day,
			ConcreteFlight.class);
		
		List<ConcreteFlight> flights = q2.getResultList();
//		System.out.println("Function getFlightByCitiesDate at DbAccessManager");
//		System.out.println(flights.get(0));
		return flights;
	}
	
	public void bookSeat(ConcreteFlight concreteFlight,String fare, int numSeats) {
		System.out.println(">> DataAccess: bookSeat");	
		ConcreteFlight cf = dbConnector.find(ConcreteFlight.class, concreteFlight.getCfCode());
		dbConnector.getTransaction().begin();
		cf.allocateSeat(fare, numSeats);
		dbConnector.persist(cf);
		dbConnector.getTransaction().commit();
		System.out.println(">> DataAccess: "+numSeats+ " seats allocated for "+cf+ " in "+ fare + " class");
	}
	
	public void updateDB() {
		dbConnector.getTransaction().begin();
		dbConnector.getTransaction().commit();
		System.out.println("The database was updated");
		
	}
	
}
