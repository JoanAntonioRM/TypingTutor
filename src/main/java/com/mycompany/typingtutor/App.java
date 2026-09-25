package com.mycompany.typingtutor;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
        
        VBox root = new VBox(8);
        root.setPadding(new Insets(16));
        root.getChildren().addAll(promptLabel, promptTextField, inputLabel, inputTextField);
        
        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Typing Tutor");
        stage.setScene(scene);
        stage.show();
        inputTextField.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }

}