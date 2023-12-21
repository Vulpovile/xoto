package com.flaremicro.homeautomation.xoto.controls;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class X10ModuleControl extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public X10ModuleControl() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblTheNameHere = new JLabel("Lamp Module");
		lblTheNameHere.setIcon(new ImageIcon(X10ModuleControl.class.getResource("/javax/swing/plaf/basic/icons/image-delayed.png")));
		panel.add(lblTheNameHere, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setLayout(new GridLayout(2, 2, 2, 2));
		
		JLabel lblHome = new JLabel("Home: ");
		panel_2.add(lblHome);
		
		JComboBox comboBox_1 = new JComboBox();
		panel_2.add(comboBox_1);
		
		JLabel lblDevice = new JLabel("Device");
		panel_2.add(lblDevice);
		
		JComboBox comboBox = new JComboBox();
		panel_2.add(comboBox);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(panel_3, BorderLayout.EAST);
		panel_3.setLayout(new BorderLayout(5, 0));
		
		JButton btnOn = new JButton("On");
		panel_3.add(btnOn);
		
		JPanel panel_4 = new JPanel();
		add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JSlider slider = new JSlider();
		panel_4.add(slider, BorderLayout.CENTER);
		
		JLabel lblDimness = new JLabel("Dimming");
		lblDimness.setHorizontalAlignment(SwingConstants.CENTER);
		panel_4.add(lblDimness, BorderLayout.NORTH);

	}
}
