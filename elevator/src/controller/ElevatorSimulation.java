package controller;
// ElevatorSimulation.java

// Application with Elevator Model, View, and Controller (MVC)

// Java core packages
import java.awt.BorderLayout;

// Java extension packages
import javax.swing.JFrame;

import model.ElevatorModel;
import view.ElevatorView;

public class ElevatorSimulation extends JFrame {
	private static final long serialVersionUID = 1L;

	// model, view and controller
	private ElevatorModel model;
	private ElevatorView view;
	private ElevatorController controller;

	// constructor instantiates model, view, and controller
	public ElevatorSimulation() {
		super("Deitel Elevator Simulation");

		// instantiate model, view and ,controller
		model = new ElevatorModel();
		view = new ElevatorView();
		controller = new ElevatorController(model);

		// register View for Model events
		model.setElevatorModelListener(view);

		// add view and controller to ElevatorSimulation
		getContentPane().add(view, BorderLayout.CENTER);
		getContentPane().add(controller, BorderLayout.SOUTH);

	} // end ElevatorSimulation constructor

	// main method starts program
	public static void main(String args[]) {
		// instantiate ElevatorSimulation
		ElevatorSimulation simulation = new ElevatorSimulation();
		simulation.setDefaultCloseOperation(EXIT_ON_CLOSE);
		simulation.pack();
		simulation.setVisible(true);
	}
}

/**************************************************************************
 * (C) Copyright 2002 by Deitel & Associates, Inc. and Prentice Hall. * All
 * Rights Reserved. * * DISCLAIMER: The authors and publisher of this book have
 * used their * best efforts in preparing the book. These efforts include the *
 * development, research, and testing of the theories and programs * to
 * determine their effectiveness. The authors and publisher make * no warranty
 * of any kind, expressed or implied, with regard to these * programs or to the
 * documentation contained in these books. The authors * and publisher shall not
 * be liable in any event for incidental or * consequential damages in
 * connection with, or arising out of, the * furnishing, performance, or use of
 * these programs. *
 *************************************************************************/
