package com.mycompany.typingtutor;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {
    private final String[] sentences = {
        "Try typing this text. Do it as quickly and as accurately as you can.",
        "Next type another line of input data.",
        "The quick brown fox jumps over the lazy dog.",
        "Five big quacking zephyrs jolt my wax bed.",
        "Sympathizing would fix Quaker objectives.",
        "A large fawn jumped quickly over the white zinc boxes."
    };
    
    private int currentIndex = 0;
    private TextField promptTextField;
    private TextField inputTextField;
    private Label progressLabel;
    
    @Override
    public void start(Stage stage) {
        Label promptLabel = new Label("Text to type");
        promptTextField = new TextField();
        promptTextField.setEditable(false);
        promptTextField.setFocusTraversable(false);
        promptTextField.setText(sentences[currentIndex]);
        
        Label inputLabel = new Label("Your typed response");
        inputTextField = new TextField();
        inputTextField.setPromptText("Your typing appears here");
        
        progressLabel = new Label();
        
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
        
        HBox hbox = new HBox(12);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(progressLabel, nextButton, resetButton);
        
        VBox root = new VBox(8);
        root.setPadding(new Insets(16));
        root.getChildren().addAll(promptLabel, promptTextField, inputLabel, hbox);
        
        loadCurrentPrompt();
        
        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Typing Tutor");
        stage.setScene(scene);
        stage.show();
        inputTextField.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }

    private void nextPrompt() {
        currentIndex = (currentIndex + 1) % sentences.length;
        loadCurrentPrompt();
    }
    private void resetSession() {
        currentIndex = 0;
        loadCurrentPrompt();
    }
    private void loadCurrentPrompt() {
        promptTextField.setText(sentences[currentIndex]);
        inputTextField.clear();
        progressLabel.setText((currentIndex + 1) + " of " + sentences.length);
        inputTextField.requestFocus();
    }

}