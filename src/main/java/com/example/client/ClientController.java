package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController {
    @FXML
    private TextField inputMessage;

    @FXML
    private TextArea sentMessageArea;

    private Socket socket;

    private DataInputStream in;
    private DataOutputStream out;

    @FXML
    private TextField login;
    @FXML
    private VBox loginWindow;
    @FXML
    private VBox chat;



    public void login() {
        try {
            socket = new Socket("localhost", 777);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out.writeUTF("#login "+login.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loginWindow.setVisible(false);
        loginWindow.setManaged(false);
        chat.setVisible(true);
        chat.setManaged(true);
        connect();
    }



    public void sendMessage() {
        String message = inputMessage.getText();
        if(message!=""){
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        inputMessage.clear();}
    }

    public void connect(){

        Thread thread = new Thread(() -> {
            String message;
            while (true) {
                try {
                    message=in.readUTF();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                sentMessageArea.appendText(message+"\n");
            }


        });
        thread.start();
    }
}