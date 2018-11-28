// ElevatorMusic.java
// Allows for MIDI playing capabilities
package view;

import java.net.*;

// Java extension packages
import javax.sound.midi.*;

public class ElevatorMusic implements MetaEventListener {

	// MIDI sequencer
	private Sequencer sequencer;

	// should music stop playing?
	//private boolean endOfMusic;

	// sound file name
	private String fileName;

	// sequence associated with sound file
	private Sequence soundSequence;

	// constructor opens a MIDI file to play
	public ElevatorMusic(String file) {
		// set sequencer
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.addMetaEventListener(this);
			fileName = file;
		}

		// handle exception if MIDI is unavailable
		catch (MidiUnavailableException midiException) {
			midiException.printStackTrace();
		}
	} // end ElevatorMusic constructor

	// open music file
	public boolean open() {
		try {

			// get URL for media file
			URL url = getClass().getResource(fileName);

			// get valid MIDI file
			soundSequence = MidiSystem.getSequence(url);

			// open sequencer for specified file
			sequencer.open();
			sequencer.setSequence(soundSequence);
		}

		// handle exception if URL does not exist
		catch (NullPointerException nullPointerException) {
			nullPointerException.printStackTrace();
			return false;
		}

		// handle exception if MIDI data is invalid
		catch (InvalidMidiDataException midiException) {
			midiException.printStackTrace();
			soundSequence = null;
			return false;
		}

		// handle IO exception
		catch (java.io.IOException ioException) {
			ioException.printStackTrace();
			soundSequence = null;
			return false;
		}

		// handle exception if MIDI is unavailable
		catch (MidiUnavailableException midiException) {
			midiException.printStackTrace();
			return false;
		}

		return true;
	}

	// play MIDI track
	public void play() {
		sequencer.start();
		//endOfMusic = false;
	}

	// get sequencer
	public Sequencer getSequencer() {
		return sequencer;
	}

	// handle end of track
	public void meta(MetaMessage message) {
		if (message.getType() == 47) {
			//endOfMusic = true;
			sequencer.stop();
		}
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
