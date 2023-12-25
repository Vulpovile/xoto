package com.flaremicro.homeautomation.xoto;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.fazecast.jSerialComm.SerialPort;
import com.flaremicro.homeautomation.xoto.controller.CM10AController;
import com.flaremicro.homeautomation.xoto.controller.SerialController;
import com.flaremicro.homeautomation.xoto.enums.DeviceNumber;
import com.flaremicro.homeautomation.xoto.enums.HomeLetter;
import com.flaremicro.homeautomation.xoto.module.X10Module;
import com.flaremicro.homeautomation.xoto.module.impl.LampModule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.JButton;
/**
 * 
 * @author Vulpovile
 *
 */
public class MainInterface extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	SerialController serialController;
	
	ArrayList<X10Module> modules = new ArrayList<X10Module>();
	
	private final JPanel pnlToolbars = new JPanel();
	private final JToolBar toolBar = new JToolBar();
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private final JPanel panel = new JPanel();
	private final JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.BOTTOM);
	private final JPanel panel_1 = new JPanel();
	private final JButton btnNewModule = new JButton("New Module");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try
				{
					//Hideous
					try
					{
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					}
					catch (Exception e)
					{
						try
						{
							UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.GTKLookAndFeel");
						}
						catch (Exception e2)
						{
							try
							{
								UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
							}
							catch (Exception e3)
							{
								
							}
						}
					}
					MainInterface frame = new MainInterface();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainInterface() {
		setTitle("Xoto X10 Home Automation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		pnlToolbars.setBorder(new EmptyBorder(0, 0, 2, 0));

		contentPane.add(pnlToolbars, BorderLayout.NORTH);
		pnlToolbars.setLayout(new BoxLayout(pnlToolbars, BoxLayout.X_AXIS));

		pnlToolbars.add(toolBar);
		
		toolBar.add(btnNewModule);

		contentPane.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addTab("List View", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));

		panel.add(tabbedPane_1);

		tabbedPane_1.addTab("All", null, panel_1, null);

		serialController = new CM10AController(SerialPort.getCommPort("/dev/ttyUSB0"));
		serialController.start();
		
		LampModule lampModule = new LampModule(HomeLetter.A, DeviceNumber._2);
		modules.add(lampModule);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
}
