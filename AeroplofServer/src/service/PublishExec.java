package service;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Toolkit;

public class PublishExec extends JFrame {

	private JPanel contentPane;
	private JTextField txtHost;
	boolean shutdown = false;	
	JButton btnExecuteServer = new JButton("Execute server");
	JButton btnShutDownServer = new JButton("Shut down server");
	JLabel lblSetUpHost = new JLabel("Set up host:");
	/**
	 * Launch the application.
	 */
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
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PublishExec frame = new PublishExec();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

	/**
	 * Create the frame.
	 */
	public PublishExec() {
		try 
        {
            setIconImage(Toolkit.getDefaultToolkit().getImage("logo/plane.png"));
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
		setTitle("Aeroplof Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		btnShutDownServer.setEnabled(false);
		btnShutDownServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shutdown = true;
				JOptionPane.showMessageDialog(new JFrame(),"Server closed","Server closed",JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		});
		
		txtHost = new JTextField("http://localhost:8080/ws");
		txtHost.setColumns(10);
	
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(42)
							.addComponent(btnExecuteServer)
							.addGap(89)
							.addComponent(btnShutDownServer))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(78)
							.addComponent(txtHost, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(155)
							.addComponent(lblSetUpHost, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(71, Short.MAX_VALUE))
		);
		lblSetUpHost.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(51)
					.addComponent(lblSetUpHost, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtHost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnExecuteServer)
						.addComponent(btnShutDownServer))
					.addGap(60))
		);
		btnExecuteServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Publisher p = new Publisher(txtHost.getText());
				btnExecuteServer.setEnabled(false);
				btnShutDownServer.setEnabled(true);
			}
		});
		contentPane.setLayout(gl_contentPane);
	}
}
