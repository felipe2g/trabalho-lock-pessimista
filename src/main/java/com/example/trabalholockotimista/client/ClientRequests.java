package com.example.trabalholockotimista.client;

import com.example.trabalholockotimista.TrabalhoLockOtimistaApplication;
import org.springframework.boot.SpringApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClientRequests {
    public static void main(String[] args) {
        for (int i = 1; i <= 100; i++) {
            Thread thread = new Thread(new ApiRequestTask(i));
            thread.start();
        }
    }

    static class ApiRequestTask implements Runnable {
        private int threadNumber;

        public ApiRequestTask(int threadNumber) {
            this.threadNumber = threadNumber;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 100; i++) {
                try {
                    URL url = new URL("http://localhost:8080/conta/depositar?numeroConta=" + i + "&valor=" + i*0.8); // Substitua com a URL da API real
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");

                    int responseCode = connection.getResponseCode();

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    System.out.println("Thread " + threadNumber + " - Request " + i + " - Response Code: " + responseCode + " - Response JSON: " + response.toString());

                    connection.disconnect();
                } catch (Exception e) {
                    System.err.println("Thread " + threadNumber + " - Request " + i + " - Error: " + e.getMessage());
                }
            }
        }
    }
}