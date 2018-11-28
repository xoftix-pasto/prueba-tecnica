// Door.java
// Sends DoorEvents to DoorListeners when opened or closed
package model;

// Java core packages
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import event.DoorEvent;
import event.DoorListener;
import event.ElevatorMoveEvent;
import event.ElevatorMoveListener;

public class Door implements ElevatorMoveListener {

	// represent whether Door is open or closed
	private boolean doorOpen = false;

	// time before Door closes automatically
	public static final int AUTOMATIC_CLOSE_DELAY = 3000;

	// Set of DoorListeners
	private Set<DoorListener> doorListeners;

	// location where Door opened or closed
	private Location doorLocation;

	// Door constructor instantiates Set for DoorListeners
	public Door() {
		doorListeners = new HashSet<DoorListener>(1);
	}

	// add Door listener
	public void addDoorListener(DoorListener listener) {
		// prevent other objects from modifying doorListeners
		synchronized (doorListeners) {
			doorListeners.add(listener);
		}
	}

	// remove Door listener
	public void removeDoorListener(DoorListener listener) {
		// prevent other objects from modifying doorListeners
		synchronized (doorListeners) {
			doorListeners.remove(listener);
		}
	}

	// open Door and send all listeners DoorEvent objects
	public void openDoor(Location location) {
		if (!doorOpen) {

			doorOpen = true;

			// obtain iterator from Set
			Iterator<DoorListener> iterator;
			synchronized (doorListeners) {
				iterator = new HashSet<DoorListener>(doorListeners).iterator();
			}

			// get next DoorListener
			while (iterator.hasNext()) {
				DoorListener doorListener = iterator.next();

				// send doorOpened event to this DoorListener
				doorListener.doorOpened(new DoorEvent(this, location));
			}

			doorLocation = location;

			// declare Thread that ensures automatic Door closing
			Thread closeThread = new Thread(new Runnable() {

				public void run() {
					// close Door if open for more than 3 seconds
					try {
						Thread.sleep(AUTOMATIC_CLOSE_DELAY);
						closeDoor(doorLocation);
					}

					// handle exception if interrupted
					catch (InterruptedException exception) {
						exception.printStackTrace();
					}
				}
			} // end anonymous inner class
			);

			closeThread.start();
		}
	} // end method openDoor

	// close Door and send all listeners DoorEvent objects
	public void closeDoor(Location location) {
		if (doorOpen) {

			doorOpen = false;

			// obtain iterator from Set
			Iterator<DoorListener> iterator;
			synchronized (doorListeners) {
				iterator = new HashSet<DoorListener>(doorListeners).iterator();
			}

			// get next DoorListener
			while (iterator.hasNext()) {
				DoorListener doorListener = iterator.next();

				// send doorClosed event to this DoorListener
				doorListener.doorClosed(new DoorEvent(this, location));
			}
		}
	} // end method closeDoor

	// return whether Door is open or closed
	public boolean isDoorOpen() {
		return doorOpen;
	}

	// invoked after Elevator has departed
	public void elevatorDeparted(ElevatorMoveEvent moveEvent) {
	}

	// invoked when Elevator has arrived
	public void elevatorArrived(ElevatorMoveEvent moveEvent) {
		openDoor(moveEvent.getLocation());
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
