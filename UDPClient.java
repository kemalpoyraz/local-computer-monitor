import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.*;

public class UDPClient extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JLabel label;
	private JTextArea textArea;
	private JButton control;
	private JButton stream;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenuItem about;
	private JMenuItem exit;
	private JMenuItem capture;
	
	private InetAddress hostAddress;
	private int hostPort;
	
	private static Thread session;
	private static UDPClient client;
	DatagramSocket clientSocket;
	
	private static String message = "";
	
	public static void main (String[] args)
	{
		client = new UDPClient();
		client.setPreferredSize(new Dimension(1280, 720));
		
		try {
			client.clientSocket = new DatagramSocket(7676);
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		client.menuBar = new JMenuBar();
		
		client.fileMenu = new JMenu("File");
		client.capture = new JMenuItem("Capture a frame");
		client.fileMenu.add(client.capture);
		client.exit = new JMenuItem("Exit");
		client.fileMenu.add(client.exit);
		
		client.helpMenu = new JMenu("Help");
		client.about = new JMenuItem("About");
		client.helpMenu.add(client.about);
		
		client.menuBar.add(client.fileMenu);
		client.menuBar.add(client.helpMenu);
		client.setJMenuBar(client.menuBar);
		
		client.capture.addActionListener(new ActionListener()
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
		
		client.exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		client.about.addActionListener(new ActionListener() 
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	JOptionPane.showMessageDialog(client,"Kemal Poyraz");
	        }
	    });
		
		client.panel = new JPanel(new FlowLayout());
		client.control = new JButton("Control");
		client.stream = new JButton("Stream");
		client.label = new JLabel("No Peers");
		client.textArea = new JTextArea();
		client.control.addActionListener(new ActionListener()
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
		client.stream.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
			}
		});
		
		client.textArea.setPreferredSize(new Dimension(1200, 450));
		client.label.setPreferredSize(new Dimension(1200, 100));
		client.control.setPreferredSize(new Dimension(600, 100));
		client.stream.setPreferredSize(new Dimension(600, 100));
		client.textArea.setEditable(false);
		client.label.setHorizontalAlignment(SwingConstants.CENTER);
		
		client.panel.add(client.textArea);
		client.panel.add(client.label);
		client.panel.add(client.control);
		client.panel.add(client.stream);
		client.add(client.panel);
		
		client.pack();
		client.setResizable(false);
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.setVisible(true);
		
		client.tutor();
	}
	
	private void tutor()
	{
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			clientSocket.setBroadcast(true);
			InetAddress IP = InetAddress.getByName(getBroadcastAddress());
			byte[] send = new byte[1024];
			byte[] receive = new byte[1024];
			
			send = "Peer".getBytes();
			DatagramPacket sendPacket = new DatagramPacket(send, send.length, IP, 8888);
			clientSocket.send(sendPacket);
			
			DatagramPacket receivePacket = new DatagramPacket(receive, receive.length);
			clientSocket.receive(receivePacket);
			hostAddress = receivePacket.getAddress();
			hostPort = receivePacket.getPort();
			
			String str = new String(receivePacket.getData()).trim();
			if (str.equals("Allow"));
			{
				label.setText("Connected to Tutor");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private String getBroadcastAddress()
	{
		String IP01 = "";
		String IP02 = "";
		
		try {
			InetAddress address = InetAddress.getLocalHost();
			IP01 = address.getHostAddress();
			IP02 = address.getHostAddress();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		int position01 = IP01.indexOf('.', 0);
		int i = 2;
	    while (i-- > 0 && position01 != -1)
	    {
	    	position01 = IP01.indexOf('.', position01 + 1);
	    }
	    
		IP01 = IP01.substring(0, position01 + 1);
		IP01 = IP01.concat("255");
		
		int position02 = IP01.indexOf('.', 0);
		i = 1;
	    while (i-- > 0 && position02 != -1)
	    {
	    	position02 = IP01.indexOf('.', position02 + 1);
	    }
	    
	    IP02 = IP02.substring(0, position02 + 1);
	    IP02 = IP02.concat("255.255");
		
		return IP01;
	}
}
