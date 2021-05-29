import javax.swing.*;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class UDPServer extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel panel ;
	private JButton control;
	private JButton stream;
	private JLabel label;
	private JTextArea textArea;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenuItem exit;
	private JMenuItem about;
	private JMenuItem capture;
	private JMenuItem listConnections;
	
	private static UDPServer server;
	private ArrayList<InetAddress> clientList = new ArrayList<>();
	
	private static String message = "";
	
	public static void main(String[] args)
	{
		server = new UDPServer();
		server.setPreferredSize(new Dimension(1280, 720));
		server.panel = new JPanel(new FlowLayout());
		server.control = new JButton("Control");
		server.stream = new JButton("Stream");
		server.label = new JLabel("Peers: ");
		server.textArea = new JTextArea();
		
		server.menuBar = new JMenuBar();
		
		server.fileMenu = new JMenu("File");
		server.capture = new JMenuItem("Capture a frame");
		server.listConnections = new JMenuItem("Show active Connections");
		server.exit = new JMenuItem("Exit");
		server.fileMenu.add(server.capture);
		server.fileMenu.add(server.listConnections);
		server.fileMenu.add(server.exit);
		
		server.helpMenu = new JMenu("Help");
		server.about = new JMenuItem("About");
		server.helpMenu.add(server.about);
		
		
		server.control.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try {
					new RobotMouse();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		
		server.stream.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							new ScreenStream();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
		});
		
		server.capture.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						
						new ScreenCapture();
					}
				});
			}
		});
		
		server.listConnections.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String str = "Active";
				
				for (InetAddress clients: server.clientList)
				{
					str = str + "\n" + clients.toString();
				}
				JOptionPane.showMessageDialog(server,  str);
			}
		});
		
		server.exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		server.about.addActionListener(new ActionListener() 
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	JOptionPane.showMessageDialog(server,"Kemal Poyraz");
	        }
	    });
		
		server.textArea.setPreferredSize(new Dimension(1200, 450));
		server.label.setPreferredSize(new Dimension(1200, 100));
		server.control.setPreferredSize(new Dimension(600, 100));
		server.stream.setPreferredSize(new Dimension(600, 100));	
		server.textArea.setEditable(false);
		server.label.setHorizontalAlignment(SwingConstants.CENTER);
		
		server.panel.add(server.textArea);
		server.panel.add(server.label);
		server.panel.add(server.control);
		server.panel.add(server.stream);
		
		server.menuBar.add(server.fileMenu);
	    server.menuBar.add(server.helpMenu);
	    server.setJMenuBar(server.menuBar);
		server.add(server.panel);
		
		server.pack();
		server.setResizable(false);
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		server.setVisible(true);
		
		server.students();
	}
	
	public void students()
	{
		Thread getStudents = new getStudents();
		getStudents.start();
	}
	
	public class getStudents extends Thread
	{
		public void run()
		{
			try {
				DatagramSocket serverSocket = new DatagramSocket(8888);
				byte[] sendByte = new byte[1024];
				byte[] receiveByte = new byte[1024];
				
				while (true)
				{
					DatagramPacket receivePacket = new DatagramPacket(receiveByte, receiveByte.length);
					serverSocket.receive(receivePacket);
					
					String str = new String(receivePacket.getData()).trim();
					if(str.equals("Peer"))
					{
						sendByte = "Allow".getBytes();
						clientList.add(receivePacket.getAddress());
						textArea.setText(String.valueOf(clientList.size()));
						label.setText("Peers: " + String.valueOf(clientList.size()));
						message = message + "\nStudent " + clientList.size() + " connected, " + receivePacket.getAddress();
						textArea.setText(message);
						DatagramPacket sendPacket = new DatagramPacket(sendByte, sendByte.length, receivePacket.getAddress(), receivePacket.getPort());
						serverSocket.send(sendPacket);
					}
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
}
