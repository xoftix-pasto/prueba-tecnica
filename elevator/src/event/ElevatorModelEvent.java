// ElevatorModelEvent.java
// Basic event packet in Elevator simulation
package event;

// Deitel packages
import model.*;

public class ElevatorModelEvent {

	// Location where ElevatorModelEvent was generated
	private Location location;

	// source Object that generated ElevatorModelEvent
	private Object source;

	// ElevatorModelEvent constructor sets Location
	public ElevatorModelEvent(Object source, Location location) {
		setSource(source);
		setLocation(location);
	}

	// set ElevatorModelEvent Location
	public void setLocation(Location eventLocation) {
		location = eventLocation;
	}

	// get ElevatorModelEvent Location
	public Location getLocation() {
		return location;
	}

	// set ElevatorModelEvent source
	private void setSource(Object eventSource) {
		source = eventSource;
	}

	// get ElevatorModelEvent source
	public Object getSource() {
		return source;
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
