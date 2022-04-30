package service;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import domain.*;

@WebService
public interface WebServiceLogicInterface {
	/**
	 * Provides an ArrayList of concrete flights that match the data introduced 
	 * by a requesting actor
	 * 
	 * @param departureCity		The city from which the concrete flight must take off
	 * @param arrivalCity		The city in which the concrete flight must land
	 * @param date				The date of the concrete flight
	 * @return					A List of concrete flights that meet the restrictions
	 */
	@WebMethod
	public List<ConcreteFlight> getMatchingConFlights(String departureCity, 
			String arrivalCity, Date date);

	/**
	 * Ensures that any reservation made by an user is recorded in the conFlight
	 * 
	 * @param conFli		The concrete flight in which a free seat is to be booked
	 * @param fare			The fare of the ticket
	 * @return				The number of remaining free seats for this fare after 
	 * 						the booking, or -1 if no available seat to book
	 */
	@WebMethod
	public int bookSeat(ConcreteFlight conFli, String fare,int ntickets);
	
	@WebMethod
	public List<Flight> getAllFlights();
	
	@WebMethod
	public List<ConcreteFlightContainer> getMatchingCConFlights(String departureCity, 
			String arrivalCity, Date date);
}
