package com.company;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client implements Runnable{

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    public Client client;

    public Client(String username, Socket socket){//Ctor
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
            // System.out.println("Hi from ctor Client");

        }catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);

        }
    }



    public void sendMsg(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner sc = new Scanner(System.in);
            // System.out.println("Hi from method sendMsg");

            while(socket.isConnected()){
                String msgToSend = sc.nextLine();
                bufferedWriter.write(username + ":" + msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

            }
        }catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){

        try{
            // System.out.println("Hi from method closeEverything");
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {//listens from the client

        while (socket.isConnected()) {
            try {
                String msgFromGroup = bufferedReader.readLine();

                System.out.println(msgFromGroup);
                // System.out.println("Hi from the listening thread");


            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    public static void main(String args[]) throws IOException {
        // System.out.println("Hi from main method of Client class");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username ");
        String username = sc.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(username, socket);
        Thread thr = new Thread(client);
        thr.start();
        client.sendMsg();






    }
}

