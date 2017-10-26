package easy.commons.io.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import easy.commons.io.EasyBarcode;

public class BarcodeTest {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BarcodeTest window = new BarcodeTest();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BarcodeTest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Panel panel = new Panel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				EasyBarcode.getInstance().buildFromXML(
						"E:\\Projects\\OpenSource\\easyjutils\\easycommonsio\\resources\\barcode\\Codabar.xml",
						"12312312312414", EasyBarcode.OutputType.AWT, "F:\\barcode.svg",
						(Graphics2D) panel.getGraphics());
			}
		});
	}

}
