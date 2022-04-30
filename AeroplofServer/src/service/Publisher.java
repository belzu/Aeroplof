package service;
import service.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.ws.Endpoint;
public class Publisher {
	public Publisher() {
		// TODO Auto-generated method stub
//		String url = "http://0.0.0.0:8080/ws";
//		String url = "http://169.254.254.62:8080/ws";
		String url = "http://0.0.0.0:8080/ws";
		Endpoint.publish(url, new WebServiceLogic());
		System.out.println("The web service has been launched");
		JOptionPane.showMessageDialog(new JFrame(),"Connected to:\n"+url,"Connected to the server",JOptionPane.INFORMATION_MESSAGE);
	}
	public Publisher(String s) {
		// TODO Auto-generated method stub
//		String url = "http://0.0.0.0:8080/ws";
//		String url = "http://169.254.254.62:8080/ws";
		String url = s;
		Endpoint.publish(url, new WebServiceLogic());
		System.out.println("The web service has been launched");
		JOptionPane.showMessageDialog(new JFrame(),"Connected to:\n"+url,"Connected to the server",JOptionPane.INFORMATION_MESSAGE);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String url = "http://0.0.0.0:8080/ws";
//		String url = "http://169.254.254.62:8080/ws";
		String url = "http://0.0.0.0:8080/ws";
		Endpoint.publish(url, new WebServiceLogic());
		System.out.println("The web service has been launched");
		JOptionPane.showMessageDialog(new JFrame(),"Connected to:\n"+url,"Connected to the server",JOptionPane.INFORMATION_MESSAGE);
	}
}
