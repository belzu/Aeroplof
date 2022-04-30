package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import data_access.DbAccessManager;
import domain.ConcreteFlight;
import domain.Flight;
import domain.ConcreteFlightContainer;
@WebService(endpointInterface = "service.WebServiceLogicInterface")
public class WebServiceLogic{
	List<Flight> availableFlights;
	DbAccessManager dataManager;
	public WebServiceLogic () {
		super();

		/* This section is for testing purposes and it's not truly related to
		 * the construction process. A couple of flights are created and
		 * included in the list 'availableFlights'. Then associated concrete
		 * flights are also created and uploaded in main memory.
		 * Please note that they will be accessed from the flights to whom they
		 * belong (which are safe in 'availableFlights').
		 * 
		 * These fictitious flights and concrete flights serve to test if the 
		 * use case is correctly implemented prior to establishing persistence.
		 */
		this.dataManager = new DbAccessManager();		
	}

	/**
	 * 'getConFlights' method provides a List of concrete flights matching
	 * some user's requirements
	 * 
	 * @param intendedDepartureCity    introduced by user
	 * @param intendedArrivalCity      introduced by user
	 * @param intendedDate             introduced/selected by user
	 * @return                         List of concrete flights
	 */
	@WebMethod
	public List<ConcreteFlight> getMatchingConFlights(String intendedDepartureCity, 
			String intendedArrivalCity, Date intendedDate){
		List<ConcreteFlight> matchingConFlights = new ArrayList<ConcreteFlight>();
		matchingConFlights = dataManager.getFlightByCitiesDate(intendedArrivalCity, intendedDepartureCity, intendedDate);
		return matchingConFlights;		
	}
	
	@WebMethod
	public List<ConcreteFlightContainer> getMatchingCConFlights(String intendedDepartureCity, 
			String intendedArrivalCity, Date intendedDate){	
		List<ConcreteFlightContainer> matchingCConFlights = new ArrayList<ConcreteFlightContainer>();
		for(ConcreteFlight cf: dataManager.getFlightByCitiesDate(intendedArrivalCity, intendedDepartureCity, intendedDate)) {
			matchingCConFlights.add(new ConcreteFlightContainer(cf));
		}		
		return matchingCConFlights;		
	}
	
	/**
	 * @param conFli		The concrete flight in which a free seat is to be booked
	 * @param fare			The fare of the ticket
	 * @return				The number of remaining free seats for this fare after 
	 * 						the booking, or -1 if no available seat to book
	 */
	@WebMethod
	public int bookSeat(ConcreteFlight conFli, String fare,int ntickets) {
		dataManager.bookSeat(conFli, fare, ntickets);
		return conFli.allocateSeat(fare, ntickets);
	}
	
	@WebMethod
	public List<Flight> getAllFlights(){
		return dataManager.getAllFlights();
	}
}
