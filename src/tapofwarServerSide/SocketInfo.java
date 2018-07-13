/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tapofwarServerSide;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author LoganLee
 */
public class SocketInfo {
    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private int socketIndex;
    private int opponentIndex;
    private boolean playAgain;

    public boolean isPlayAgain() {
        return playAgain;
    }

    public void setPlayAgain(boolean playAgain) {
        this.playAgain = playAgain;
    }

    public Socket getConnection() {
        return connection;
    }

    public void setConnection(Socket connection) {
        this.connection = connection;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public void setInput(ObjectInputStream input) {
        this.input = input;
    }

    public int getSocketIndex() {
        return socketIndex;
    }

    public void setSocketIndex(int socketIndex) {
        this.socketIndex = socketIndex;
    }

    public int getOpponentIndex() {
        return opponentIndex;
    }

    public void setOpponentIndex(int opponentIndex) {
        this.opponentIndex = opponentIndex;
    }
    
}
