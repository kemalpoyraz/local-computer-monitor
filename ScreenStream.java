import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ScreenStream extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	public ScreenStream() throws IOException
	{
		super("Frame");
		Thread stream = new Thread();
		stream.run();
	}

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				new ScreenCapture();
			}
		});
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			DatagramSocket serverSocket2 = new DatagramSocket(8888);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] sendByte = new byte[1024];
		byte[] receiveByte = new byte[1024];
		
		while(true)
		{	
			Robot r;
			
			try {
				r = new Robot();
				Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit()
						.getScreenSize());
				BufferedImage image = r.createScreenCapture(screen);
				
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				ImageIO.write(image, "jpeg", buffer);
				sendByte = buffer.toByteArray();
				
				JLabel label = new JLabel(new ImageIcon(image));
				add(label);
				pack();
				setVisible(true);
				setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
			} catch (AWTException e) {
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
