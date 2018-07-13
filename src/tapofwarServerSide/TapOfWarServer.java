/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tapofwarServerSide;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author LoganLee
 */
public class TapOfWarServer {

    private ServerSocket server;
    private Socket temp;
    private SocketInfo socket;
    private volatile int counter = 0;
    private int temp_int = 0, size = 100;
    private SocketInfo[] socketArray = new SocketInfo[size];
    
    public TapOfWarServer() {
        for(int i=0; i < 100; i ++){
            socketArray[i] = new SocketInfo();
        }
    }

    public void insertSocket(){
        int x, len = socketArray.length;
        for(x=0; x < len; x ++){
            if(socketArray[x] == null)break;
        }
        
        if(x == len)
            x = counter;
        
        socketArray[x] = socket;
        socketArray[x].setSocketIndex(x);
        temp_int = x;
        
        if(x % 2 == 0){
            socketArray[x].setOpponentIndex(x + 1);
            displayMessage("Index : " + x + " / Opponent : " + (x+1));
        }else{
            socketArray[x].setOpponentIndex(x - 1);
            displayMessage("Index : " + x + " / Opponent : " + (x-1));
        }
        

    }
    
    public void runServer() {
        try {
            server = new ServerSocket(12345, 100);
            while (true) {
                displayMessage("Waiting connection");
                temp = server.accept();
                socket = new SocketInfo();
                socket.setConnection(temp);
                if(counter < 100){
                    insertSocket();
                    counter ++;
                    new Thread(){
                        public void run(){
                            int index = temp_int;
                            try {
                                getStream(index);
                                processConnection(index);
                            }catch (EOFException ex) {
                                displayMessage("\nClient terminated connection");
                            } catch (IOException ex) {
                                Logger.getLogger(TapOfWarServer.class.getName()).log(Level.SEVERE, null, ex);
                            }finally{
                                closeConnection(index);
                            }
                        }
                    }.start();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TapOfWarServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getStream(int i) throws IOException {
        socketArray[i].setOutput(new ObjectOutputStream(socketArray[i].getConnection().getOutputStream()));
        socketArray[i].getOutput().flush();
        
        socketArray[i].setInput(new ObjectInputStream(socketArray[i].getConnection().getInputStream()));
        
        displayMessage("\nGot I/O streams\n");
    }

    private void processConnection(int i) throws IOException {
        String message = "Conenction successful";
        Object object = message;
        while(counter % 2 != 0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TapOfWarServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        sendData(true, i);
        do {
            try {
                object = socketArray[i].getInput().readObject();
                if(object.equals("Click")){
                    socketArray[i].setPlayAgain(true);
                    if(socketArray[socketArray[i].getOpponentIndex()].isPlayAgain()){
                        sendData("Okay", i);
                        sendData("Okay", socketArray[i].getOpponentIndex());
                    }
                }else if(object.equals("Finish")){
                    socketArray[i].setPlayAgain(false);
                }
                
                sendData(object, i);
            } catch (ClassNotFoundException ex) {
                displayMessage("\nUnkown object type received");
            }
        } while (!object.equals("TERMINATE"));
    }

    private void closeConnection(int i) {
        displayMessage("\nTerminating connection " + i + "\n");
        try {
            sendData("TERMINATE", i);
            socketArray[i].getOutput().close();
            socketArray[i].getInput().close();
            socketArray[i].getConnection().close();
            socketArray[i] = null;
            counter --;
        } catch (IOException ex) {
            Logger.getLogger(TapOfWarServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendData(Object object, int i) {
        displayMessage("Data sending from index " + i + " : " + object);
        try {
            if(socketArray[i] != null && socketArray[socketArray[i].getOpponentIndex()] != null){
                socketArray[socketArray[i].getOpponentIndex()].getOutput().writeObject(object);
                socketArray[socketArray[i].getOpponentIndex()].getOutput().flush();
            }
        } catch (IOException ex) {
            displayMessage("\nError writing object");
        }
    }

    private void displayMessage(final String message) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        System.out.println(message);
                    }
                }
        );
    }
    
    public static void main(String[] args) {
        TapOfWarServer server = new TapOfWarServer();
        server.runServer();
    }
}
