package com.example.mellotron2;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private GridPane gridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PianoRollGrid pianoRollGrid = new PianoRollGrid();
        gridPane.getChildren().add(pianoRollGrid);
    }
    @FXML
    private AnchorPane musicScorePane;
    @FXML
    public Text notesLabel;
    public Button noteButton1;
    public Button noteButton2;
    public Button noteButton3;
    public Button noteButton4;
    public Button noteButton5;
    public Button noteButton6;
    public Button noteButton7;
    public Button noteButton8;
    public Button noteButton9;
    public Button noteButton10;
    public Button noteButton11;
    public Button noteButton12;
    private ArrayList<String> notesSelected = new ArrayList<>();
    private static final int NUM_NOTES = 4;
    private static final int NUM_BEATS = 16;
    private Random random = new Random();

    private MidiChannel channel;

    public HelloController() {
        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            channel = synthesizer.getChannels()[0];

            // Set the instrument to a grand piano and the volume to 80
            channel.programChange(0);  // Set the instrument to a grand piano
            channel.controlChange(7, 100);   // Set the volume to 90 (out of 127)

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {

        try {
            // Set a background color for the musicScorePane
            musicScorePane.setStyle("-fx-background-color: white");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private double getNoteDuration(int beat, int note) {
        int beatsInBar = 16;
        double beatDuration = 1.0;

        // Calculate the start time and end time of the note based on the current beat and note duration
        long startTime = Math.round(beat * beatDuration * 1000);
        long endTime = Math.round((beat + 1) * beatDuration * 1000);

        // Find the note within the selected notes that falls within the start and end time
        for (int i = 0; i < notesSelected.size(); i++) {
            String selectedNote = notesSelected.get(i);
            int noteNumber = convertNoteToMidi(selectedNote);

            if (noteNumber != -1) {
                long noteStartTime = Math.round(i * beatsInBar * beatDuration * 1000);
                long noteEndTime = Math.round((i + 1) * beatsInBar * beatDuration * 1000);

                if (startTime >= noteStartTime && endTime <= noteEndTime) {
                    // The current beat and note fall within the selected note's duration
                    return Math.pow(0.5, random.nextInt(4)); // Return the duration
                }
            }
        }

        return 0.0; // Return 0.0 if the beat and note are not within any selected note's duration
    }

    @FXML
    private void handleNoteButton(ActionEvent event) {
        Button button = (Button) event.getSource();
        String note = button.getText();


        // Handle the selected note by playing it
        if (notesSelected.size() < 4) {
            // Generate a random pastel color
            Color pastelColor = generatePastelColor();


            // Set the background color of the button to the generated pastel color
            button.setStyle("-fx-background-color: " + toRGBCode(pastelColor) + ";");


            notesLabel.setText(notesLabel.getText() + note + " ");
            notesSelected.add(note);
            playSelectedNoteMidi(note);
        }
    }

    private Color generatePastelColor() {
        Random random = new Random();
        // Generate random values between 150 and 255 to create a pastel color
        int red = 150 + random.nextInt(106);
        int green = 150 + random.nextInt(106);
        int blue = 150 + random.nextInt(106);
        return Color.rgb(red, green, blue);
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void playSelectedNoteMidi(String note) {
        int noteNumber = convertNoteToMidi(note);
        if (noteNumber != -1) {
            channel.noteOn(noteNumber, 127);
            // Delay for a short duration to play the note
            try {
                Thread.sleep(100); // Adjust the duration as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            channel.noteOff(noteNumber, 127);
        }
    }
    public void regenerateButtonAction(ActionEvent actionEvent) {
        generateMelody();
        playMelody();
    }

    public void redoButtonAction(ActionEvent actionEvent) {
        noteButton1.setStyle("");
        noteButton2.setStyle("");
        noteButton3.setStyle("");
        noteButton4.setStyle("");
        noteButton5.setStyle("");
        noteButton6.setStyle("");
        noteButton7.setStyle("");
        noteButton8.setStyle("");
        noteButton9.setStyle("");
        noteButton10.setStyle("");
        noteButton11.setStyle("");
        noteButton12.setStyle("");

        //for all buttons. Erases the user selected notes.
        notesLabel.setText("");
        notesSelected.clear();
    }

    public void shuffleButtonAction(ActionEvent actionEvent) {
        ArrayList<Integer> shuffledIndices = shuffleSelectedNotes();
        generateMelody(shuffledIndices);
        playMelody();
        shuffleSelectedNotes();
    }

    void generateMelody() {
        Random random = new Random();
        notesSelected.clear();
        for (int i = 0; i < 4; i++) {
            int note = random.nextInt(NUM_NOTES);
            String noteName = convertMidiToNoteName(note);
            notesSelected.add(noteName);
        }
    }

    private void generateMelody(ArrayList<Integer> indices) {
        ArrayList<String> shuffledNotes = new ArrayList<>();
        for (int index : indices) {
            shuffledNotes.add(notesSelected.get(index));
        }
        notesSelected = shuffledNotes;
    }

    private ArrayList<Integer> shuffleSelectedNotes() {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < notesSelected.size(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
        return indices;
    }

    private String convertMidiToNoteName(int noteNumber) {
        int note = noteNumber % NUM_NOTES;
        switch (note) {
            case 0:
                return "C";
            case 1:
                return "C#";
            case 2:
                return "D";
            case 3:
                return "D#";
            case 4:
                return "E";
            case 5:
                return "F";
            case 6:
                return "F#";
            case 7:
                return "G";
            case 8:
                return "G#";
            case 9:
                return "A";
            case 10:
                return "A#";
            case 11:
                return "B";
            default:
                return "";
        }
    }

    private void playMelody() {
        int beatsInBar = 16; // Number of beats in a bar (4/4 time signature for a 4-bar melody)
        double beatDuration = 1.0; // Duration of one beat in seconds
        int totalBars = 4; // Total number of bars in the melody

        Random random = new Random();

        for (int bar = 0; bar < totalBars; bar++) {
            int currentBeat = bar * beatsInBar;

            for (String note : notesSelected) {
                int noteNumber = convertNoteToMidi(note);
                if (noteNumber != -1) {
                    // Generate a random duration for the note
                    double duration = getNoteDuration(currentBeat, noteNumber);

                    // Calculate the start time and end time of the note based on the current beat and note duration
                    long startTime = Math.round(currentBeat * beatDuration * 1000); // Convert to milliseconds
                    long endTime = Math.round((currentBeat + duration) * beatDuration * 1000); // Convert to milliseconds

                    // Play the note on the MIDI channel with the calculated start time
                    channel.noteOn(noteNumber, 127);

                    try {
                        Thread.sleep((long) (duration * beatDuration * 1000)); // Sleep for the duration of the note
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Stop playing the note after the duration
                    channel.noteOff(noteNumber, 127);

                    currentBeat += (int) duration; // Move to the next beat

                    // Loop through the randomization process.
                    // Allow notes to be played more than once to create a longer melody.
                    if (currentBeat >= (bar + 1) * beatsInBar) {
                        currentBeat = bar * beatsInBar;
                    }
                }
            }
        }
    }


    private int convertNoteToMidi(String note) {
        switch (note) {
            case "C":
                return 60; // MIDI note number for C4
            case "C#":
                return 61;
            case "D":
                return 62;
            case "D#":
                return 63;
            case "E":
                return 64;
            case "F":
                return 65;
            case "F#":
                return 66;
            case "G":
                return 67;
            case "G#":
                return 68;
            case "A":
                return 69;
            case "A#":
                return 70;
            case "B":
                return 71;
            default:
                return -1; // Invalid note
        }
    }
}

