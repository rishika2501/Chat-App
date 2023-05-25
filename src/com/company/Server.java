package com.company;

import java.io.IOException;
import java.net.*;

public class Server {
    private ServerSocket serverSocket;

    //Ctor--use to initialise the state of object , has no return type
    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;//refers to the current object in the Ctor(eliminates the confusion b/w object and att.of the method)
        // System.out.println("Hi from ctor Server");
    }

    public void startServer(){

        try{
            System.out.println("Server Started . . .");
            while(!serverSocket.isClosed()){

                Socket socket = serverSocket.accept();

                System.out.println("A new Client entered the chat!");
                ClientHandler clientHandler = new ClientHandler(socket);// class implementing the Runnable interface
                Thread thread = new Thread(clientHandler);
                thread.start();
                // System.out.println("Hi from method startServer");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //to prevent nested try-catch
    public void closeServerSocket(){
        try{
            if(serverSocket!=null){
                serverSocket.close();
                //System.out.println("Hi from method closeServerSocket");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
        // System.out.println("Hi from main methods of Server class");

    }




}

