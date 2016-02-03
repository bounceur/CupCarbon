/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013 Ahcene Bounceur
 * ----------------------------------------------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------*/

package cupcarbon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 */

public class AboutCupCarbon extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AboutCupCarbon dialog = new AboutCupCarbon();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutCupCarbon() {
		setTitle("About CupCarbon");
		setIconImage(Toolkit.getDefaultToolkit().getImage(CupCarbonParameters.IMGPATH+"cupcarbon_logo2.png"));
		setBackground(Color.WHITE);
		setBounds(100, 100, 617, 341);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(2, 0, 0, 0));
		{
			JLabel lblNewLabel = new JLabel("");
			lblNewLabel.setBackground(Color.WHITE);
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel);
			lblNewLabel.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH+"cupcarbon_logo_small.png"));
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new GridLayout(5, 0, 0, 0));
			{
				JLabel lblCupcarbonV = new JLabel("CupCarbon v. "+Version.VERSION);
				lblCupcarbonV.setHorizontalAlignment(SwingConstants.CENTER);
				lblCupcarbonV.setBackground(Color.WHITE);
				panel.add(lblCupcarbonV);
			}
			{
				JLabel lblLabsticc = new JLabel("Lab-STICC - ANR PERSEPTEUR");
				lblLabsticc.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblLabsticc);
			}
			{
				JLabel lblUniversitDeBretagne = new JLabel("UBO - IEMN - Xlim - Virtualys");
				lblUniversitDeBretagne.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblUniversitDeBretagne);
			}
			{
				JLabel lblThis = new JLabel("This project is supported by the French Agence Nationale de la Recherche ANR");
				lblThis.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblThis);
			}
			{
				JLabel lblWwwbounceurcomcupcarbon = new JLabel(Version.YEAR+" - "+"www.cupcarbon.com");
				lblWwwbounceurcomcupcarbon.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblWwwbounceurcomcupcarbon);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose() ;
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
