package com.mycompany.typingtutor;


import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Name: Joan Antonio Rodriguez Munoz
 * Student ID: 2533309
 * Date: 25 Sep 2026
 * Nagat Drawel
 * Program Development in a Graphical Environment
 * 
 * Github Repo: https://github.com/JoanAntonioRM/TypingTutor
 * 
 * Assignment 1: Typing Tutor
 */
public class App extends Application {

    // The six sample sentences from the assignment.
    private final String[] sentences = {
        "Try typing this text. Do it as quickly and as accurately as you can.",
        "Next type another line of input data.",
        "The quick brown fox jumps over the lazy dog.",
        "Five big quacking zephyrs jolt my wax bed.",
        "Sympathizing would fix Quaker objectives.",
        "A large fawn jumped quickly over the white zinc boxes."
    };

    private int currentIndex = 0;          // which sentence is showing (0 = first)
    private TextField promptTextField;     // text the user must type
    private TextField inputTextField;      // text the user types
    private Label progressLabel;           // "1 of 6", "2 of 6", ...
    private TextField keyTextField;        // last key pressed, or "Not handled"

    // Maps a physical KeyCode to the matching on-screen Button.
    private final Map<KeyCode, Button> keys = new HashMap<>();
    private Button leftShift;
    private Button rightShift;

    private Label correctLabel;
    private Label incorrectLabel;

    @Override
    public void start(Stage stage) {
        // Create the prompt TextField. The user can see it but cannot edit it.
        Label promptLabel = new Label("Text to type");
        promptTextField = new TextField();
        promptTextField.setEditable(false);
        promptTextField.setFocusTraversable(false);
        promptTextField.setText(sentences[currentIndex]);

        // Create the input TextField. Characters appear here as the user types.
        Label inputLabel = new Label("Your typed response");
        inputTextField = new TextField();
        inputTextField.setPromptText("Your typing appears here");

        progressLabel = new Label();

        // Displays the value of the key that was pressed.
        keyTextField = new TextField();
        keyTextField.setEditable(false);
        keyTextField.setFocusTraversable(false);
        keyTextField.setPrefWidth(160);
        Label keyLabel = new Label("Key pressed:");

        // Score labels. Colors come from styles.css (correct-label / incorrect-label).
        correctLabel = new Label();
        correctLabel.getStyleClass().add("correct-label");
        incorrectLabel = new Label();
        incorrectLabel.getStyleClass().add("incorrect-label");

        // Register ActionEvent handlers.
        Button nextButton = new Button("Next");
        nextButton.setFocusTraversable(false);
        nextButton.setOnAction(e -> {
            nextPrompt();
        });

        Button resetButton = new Button("Reset");
        resetButton.setFocusTraversable(false);
        resetButton.setOnAction(e -> {
            resetSession();
        });

        // Update the score whenever the typed text changes, including Backspace.
        inputTextField.textProperty().addListener((obs, oldText, newText) -> {
            updateScore();
        });

        // Layout containers.
        HBox hbox = new HBox(12);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(progressLabel, nextButton, resetButton,
                keyLabel, keyTextField, correctLabel, incorrectLabel);

        VBox topBox = new VBox(8);
        topBox.setPadding(new Insets(16));
        topBox.getChildren().addAll(promptLabel, promptTextField,
                inputLabel, inputTextField, hbox);

        VBox keyboard = buildKeyboard();

        BorderPane rootNode = new BorderPane();
        rootNode.setTop(topBox);
        rootNode.setCenter(keyboard);

        loadCurrentPrompt();

        Scene scene = new Scene(rootNode, 1280, 720);
        // Load the stylesheet from the resources folder.
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Register KEY_PRESSED and KEY_RELEASED on the Scene.
        scene.setOnKeyPressed(event -> {
            onKeyPressed(event);
        });
        
        scene.setOnKeyReleased(event -> {
            onKeyReleased(event);
        });

        // Also register on the TextField that has focus.
        inputTextField.setOnKeyPressed(event -> {
            onKeyPressed(event);
        });
        
        inputTextField.setOnKeyReleased(event -> {
            onKeyReleased(event);
        });

        stage.setTitle("Typing Tutor");
        stage.setScene(scene);
        stage.show();
        inputTextField.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }

    // Show the next sentence and wrap back to the first after the last one.
    private void nextPrompt() {
        currentIndex = (currentIndex + 1) % sentences.length;
        loadCurrentPrompt();
    }

    // Return to sentence 1 and clear typing, score, and key highlights.
    private void resetSession() {
        currentIndex = 0;
        setPressed(leftShift, false);
        setPressed(rightShift, false);

        for (Button key : keys.values()) {
            setPressed(key, false);
        }

        loadCurrentPrompt();
    }

    // Load the current sentence, clear the input, and update the counter.
    private void loadCurrentPrompt() {
        promptTextField.setText(sentences[currentIndex]);
        inputTextField.clear();
        progressLabel.setText((currentIndex + 1) + " of " + sentences.length);
        showKeyValue("", false);
        updateScore();
        inputTextField.requestFocus();
    }

    // Build the virtual keyboard from Button controls.
    private VBox buildKeyboard() {
        VBox keyboard = new VBox(10);
        keyboard.setAlignment(Pos.CENTER);
        keyboard.setPadding(new Insets(12));

        leftShift = makeButton("Shift", 120);
        rightShift = makeButton("Shift", 120);

        HBox row1 = new HBox(10);
        row1.setAlignment(Pos.CENTER);
        row1.getChildren().addAll(
                key("1", KeyCode.DIGIT1), key("2", KeyCode.DIGIT2),
                key("3", KeyCode.DIGIT3), key("4", KeyCode.DIGIT4),
                key("5", KeyCode.DIGIT5), key("6", KeyCode.DIGIT6),
                key("7", KeyCode.DIGIT7), key("8", KeyCode.DIGIT8),
                key("9", KeyCode.DIGIT9), key("0", KeyCode.DIGIT0),
                keyWide("Backspace", KeyCode.BACK_SPACE, 140));

        HBox row2 = new HBox(10);
        row2.setAlignment(Pos.CENTER);
        row2.getChildren().addAll(
                key("Q", KeyCode.Q), key("W", KeyCode.W), key("E", KeyCode.E),
                key("R", KeyCode.R), key("T", KeyCode.T), key("Y", KeyCode.Y),
                key("U", KeyCode.U), key("I", KeyCode.I), key("O", KeyCode.O),
                key("P", KeyCode.P));

        HBox row3 = new HBox(10);
        row3.setAlignment(Pos.CENTER);
        row3.getChildren().addAll(
                key("A", KeyCode.A), key("S", KeyCode.S), key("D", KeyCode.D),
                key("F", KeyCode.F), key("G", KeyCode.G), key("H", KeyCode.H),
                key("J", KeyCode.J), key("K", KeyCode.K), key("L", KeyCode.L));

        HBox row4 = new HBox(10);
        row4.setAlignment(Pos.CENTER);
        row4.getChildren().addAll(
                leftShift,
                key("Z", KeyCode.Z), key("X", KeyCode.X), key("C", KeyCode.C),
                key("V", KeyCode.V), key("B", KeyCode.B), key("N", KeyCode.N),
                key("M", KeyCode.M), key(",", KeyCode.COMMA),
                key(".", KeyCode.PERIOD),
                rightShift);

        HBox row5 = new HBox(10);
        row5.setAlignment(Pos.CENTER);
        row5.getChildren().addAll(keyWide("Space", KeyCode.SPACE, 520));

        keyboard.getChildren().addAll(row1, row2, row3, row4, row5);
        return keyboard;
    }

    // Create a standard-size key and store it in the map.
    private Button key(String text, KeyCode code) {
        return keyWide(text, code, 66);
    }

    // Create a wider key (Backspace, Space) and store it in the map.
    private Button keyWide(String text, KeyCode code, double width) {
        Button button = makeButton(text, width);
        keys.put(code, button);
        return button;
    }

    // Create one keyboard Button. The "key" style class is defined in styles.css.
    private Button makeButton(String text, double width) {
        Button button = new Button(text);
        button.getStyleClass().add("key");
        button.setFocusTraversable(false);
        button.setPrefSize(width, 66);
        button.setMinSize(width, 66);
        return button;
    }

    // KEY_PRESSED: identify the key, highlight the virtual key, show its value.
    private void onKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();   // physical key
        String keyText = event.getText();    // character typed, affected by Shift

        if (keyCode == KeyCode.SHIFT) {
            setPressed(leftShift, true);
            setPressed(rightShift, true);
            showKeyValue("Shift", false);
            return;
        }

        Button key = keys.get(keyCode);
        if (key == null) {
            // This key is not on the virtual keyboard (F-keys, arrows, Caps Lock, ...).
            showKeyValue("Not handled", true);
            return;
        }

        setPressed(key, true);
        showKeyValue(displayValue(keyCode, keyText), false);
    }

    // KEY_RELEASED: return the virtual key to its normal appearance.
    private void onKeyReleased(KeyEvent event) {
        KeyCode keyCode = event.getCode();

        if (keyCode == KeyCode.SHIFT) {
            setPressed(leftShift, false);
            setPressed(rightShift, false);
            return;
        }

        Button key = keys.get(keyCode);
        if (key != null) {
            setPressed(key, false);
        }
    }

    // Text shown in the "Key pressed" field.
    private String displayValue(KeyCode keyCode, String keyText) {
        
        if (keyCode == KeyCode.BACK_SPACE) {
            return "Backspace";
        }
        
        if (keyCode == KeyCode.SPACE) {
            return "Space";
        }
        
        if (keyText != null && !keyText.isEmpty() && Character.isLetter(keyText.charAt(0))) {
            return keyText.toUpperCase();
        }
        
        if (keyText != null && !keyText.isEmpty()) {
            return keyText;
        }
        
        return keyCode.getName();
    }

    // Add or remove the "pressed" CSS class. Colors are in styles.css, not setStyle.
    private void setPressed(Button button, boolean pressed) {
        
        if (pressed) {
            if (!button.getStyleClass().contains("pressed")) {
                button.getStyleClass().add("pressed");
            }
        } else {
            button.getStyleClass().remove("pressed");
        }
    }

    // Show the key value. "Not handled" uses the not-handled class (red text).
    private void showKeyValue(String value, boolean notHandled) {
        keyTextField.setText(value);
        keyTextField.getStyleClass().remove("not-handled");

        if (notHandled) {
            keyTextField.getStyleClass().add("not-handled");
        }
    }

    // Compare each typed character with the same position in the prompt.
    // Backspace updates this automatically because the text listener runs again.
    private void updateScore() {
        String target = sentences[currentIndex];
        String typed = inputTextField.getText();
        int good = 0;
        int bad = 0;

        for (int i = 0; i < typed.length(); i++) {
            if (i < target.length() && typed.charAt(i) == target.charAt(i)) {
                good++;
            } else {
                bad++;
            }
        }

        correctLabel.setText("Correct: " + good);
        incorrectLabel.setText("Incorrect: " + bad);
    }
}