package com.company.client;

import com.company.properties.Properties;

import javax.jnlp.FileContents;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) throws Exception {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                takeInputFromUser();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }, 0, Properties.clientLoopDelayMillis, TimeUnit.MILLISECONDS);
    }

    private static void takeInputFromUser() throws IOException {

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("localhost", Properties.serverPort);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());


        //BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String command = inFromUser.readLine();
        outToServer.write(command.getBytes(Properties.CHARSET), 0, command.length());

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        command = inFromServer.readLine();
        System.out.println("Response from Server : " + command);
        clientSocket.close();

    }
}