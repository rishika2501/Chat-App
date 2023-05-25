package com.company;

import java.io.*;
import java.util.ArrayList;
import java.net.*;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    public ClientHandler(Socket socket){
        try{
            this.socket = socket;//makes sure that the same object is passed everytime
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//char-stream(byte-stream)--send things
            this.bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));//read things
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);//"this" represents a ClientHandler object
            broadcastMsg("SERVER: " + clientUsername + " has entered the chat!!");
            //System.out.println("Hi from ctor ClientHandler");

        }catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        //listening for messages--it is a blocking operation--means that the program will be stuck untill the operation is completed
        //We will have two threads-- one waiting for msgs and the other one listening to other msgs
        String msgFromClient;
        while (socket.isConnected()) {

            try {
                msgFromClient = bufferedReader.readLine();//blocking operation--waits for the username to be entered
                broadcastMsg(msgFromClient);
                //  System.out.println("Hi from the waiting thread");



            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }


    }
    public void broadcastMsg(String msgToSend){
        for(ClientHandler clientHandler : clientHandlers){
            try{
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(msgToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                    // System.out.println("Hi from method broadcastMsg");
                }
            }catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMsg("SERVER: "+ clientUsername+ "has left the chat!!");
        //System.out.println("Hi from method removeClientHandler");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try{
            if(bufferedReader!=null){
                bufferedReader.close();
                // System.out.println("Hi from method closeEverything");
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

//    public void closeServerSocket(){
//        try{
//            if(serverSocket != null) {
//                serverSocket.close();
//            }
//
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//        }
//    }
}

