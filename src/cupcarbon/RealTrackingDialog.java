package cupcarbon;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class RealTrackingDialog extends JDialog implements ActionListener,
		PropertyChangeListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String targetId = null;
	private String trackerId = null;
	private JTextField targetTextField;
	private JTextField trackerTextField;

	private JOptionPane optionPane;

	private String demarrerBtnStr = "Enter";
	private String annulerBtnStr = "Cancel";

	public String getTargetId() {
		return targetId;
	}

	public String getTrackerId() {
		return trackerId;
	}

	public RealTrackingDialog(Frame aFrame) {
		super(aFrame, true);
		setTitle("Real tracking options");
		initializeTextFields();
		initializeOptionsPane();
	}

	private void initializeTextFields() {
		trackerTextField = new JTextField(10);
		targetTextField = new JTextField(10);
		trackerTextField.addActionListener(this);
		targetTextField.addActionListener(this);
	}

	private void initializeOptionsPane() {
		Object[] array = { "tracker Id :", trackerTextField, "Target Id:",
				targetTextField };
		Object[] options = { demarrerBtnStr, annulerBtnStr };

		optionPane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_OPTION, null, options, options[0]);
		setContentPane(optionPane);
		optionPane.addPropertyChangeListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		optionPane.setValue(demarrerBtnStr);
	}

	public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();

		if (isOptionPaneInterestingPropetry(e, propertyName)) {
			Object value = optionPane.getValue();

			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				return;
			}
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

			if (demarrerBtnStr.equals(value)) {
				updateTrackingIds();
				if (isValidForm()) {
					clearAndHide();
				} else {
					trackerTextField.selectAll();
					JOptionPane.showMessageDialog(RealTrackingDialog.this,
							"Veuillez saisir tous les champs", "",
							JOptionPane.ERROR_MESSAGE);
					initializeIds();
					trackerTextField.requestFocusInWindow();
				}
			} else {
				clearAndHide();
			}
		}
	}
	
	private void initializeIds() {
		targetId = null;
		trackerId = null;
	}

	private boolean isValidForm() {
		return trackerId != null && !trackerId.isEmpty() && targetId != null
				&& !targetId.isEmpty();
	}

	private void updateTrackingIds() {
		trackerId = trackerTextField.getText();
		targetId = targetTextField.getText();
	}

	private boolean isOptionPaneInterestingPropetry(PropertyChangeEvent e,
			String propertyName) {
		return isVisible()
				&& (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(propertyName) || JOptionPane.INPUT_VALUE_PROPERTY
						.equals(propertyName));
	}

	public void clearAndHide() {
		setVisible(false);
	}

}