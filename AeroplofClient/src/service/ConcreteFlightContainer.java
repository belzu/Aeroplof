package service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;


@XmlAccessorType(XmlAccessType.FIELD)
public class ConcreteFlightContainer implements Serializable {
	private String ccfCode;
	private ConcreteFlight concreteFlight;
	
	
	
	
	
	private Flight cflight;
	private Date cdate;	
	private String cdepartureTime; // Departure hours are just strings: "13:28" 
	private int cfreeFirstSeats;
	private int cfreeBusinessSeats;
	private int cfreeEconomySeats;
	public ConcreteFlightContainer() {
		ccfCode = null;
		cflight = null;
		cdate = null;	
		cdepartureTime = null; // Departure hours are just strings: "13:28" 
		cfreeFirstSeats = 0;
		cfreeBusinessSeats = 0;
		cfreeEconomySeats = 0;
		concreteFlight = null;
	}
	public ConcreteFlightContainer(ConcreteFlight cf) {
		this.ccfCode = cf.getCfCode();
		this.concreteFlight = cf;
	}
	
	
	
	public String getCcfCode() {
		return ccfCode;
	}
	
	public ConcreteFlight getConcreteFlight() {
		return this.concreteFlight;
	}
	
	public void setConcreteFlight(ConcreteFlight cf) {
		this.concreteFlight = cf;
	}
	
	public void setCcfCode(String code) {
		this.ccfCode = code;
	}

	public Flight getCFlight() {
		return cflight;
	}
	
	public Date getCDate() {
		return cdate;
	}
	
	public void setCDate(Date date) {
		this.cdate = date;
	}
	
	public String getCDepartureTime() {
		return cdepartureTime;
	}
	
	public void setCDepartureTime(String time) {
		cdepartureTime = time;
	}
	
	public int getFreeFirstSeatNo() {
		return cfreeFirstSeats;
	}
	
	public void setFreeFirstSeatNo(int seatNo) {
		cfreeFirstSeats = seatNo;
	}
	
	public int getFreeBusinessSeatNo() {
		return cfreeBusinessSeats;
	}
	
	public void setFreeBusinessSeatNo(int seatNo) {
		cfreeBusinessSeats = seatNo;
	}
	
	public int getFreeEconomySeatNo() {
		return cfreeEconomySeats;
	}
	
	public void setFreeEconomySeatNo(int seatNo) {
		cfreeEconomySeats = seatNo;
	}
	
	public String toString(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE', 'd' 'MMM' 'yyyy");
		return getConcreteFlight().getFlight() + " | " + dateFormat.format(cdate) + " [" + cdepartureTime + "]";
	}
}
