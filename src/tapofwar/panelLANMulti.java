/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tapofwar;

import com.sun.glass.events.KeyEvent;
import java.awt.event.InputEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author LoganLee
 */
public class panelLANMulti extends javax.swing.JPanel {
    private int player1ctr;
    private int player2ctr;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String chatServer;
    private Socket client;
    private String playerName, opponentName;
    private final Object[] options;
    
    private int ctr;
    private final int timeLimit;
    private final int timeReady;
    private int preTimer;
    private boolean isTimeUp;
    private panelMain panelMain;
    private int answer;
    
    private boolean isP1ComboDone = false;
    private boolean isP2ComboDone = false;
    private boolean isClosed;
    
    /**
     * Creates new form panelLANMulti
     */
    public panelLANMulti(String host, panelMain panelMain) {
        initComponents();
        
        this.options = new Object[]{"Yes", "No"};
        
        chatServer = host;
        
        this.panelMain = panelMain;
        isClosed = false;
        timeReady = 5;
        timeLimit = 60;
    }
    
    public void runServer() {
        try {
            connectToServer();
            getStream();
            processConnection();
        } catch (EOFException ex) {
            displayMessage("\nClient terminated connection");
        } catch (IOException ex) {
            Logger.getLogger(panelLANMulti.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
            panelMain.showMain();
        }
        
    }
    
    private void connectToServer() throws IOException {
        displayMessage("Attempting connection\n");

        client = new Socket(InetAddress.getByName(chatServer), 12345);

        displayMessage("Connected to " + client.getInetAddress().getHostName());
    }

    private void getStream() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();

        input = new ObjectInputStream(client.getInputStream());

        displayMessage("\nGot I/O streams\n");
    }

    private void processConnection() throws IOException {
        Object object = message;
        timerLabel.setText("Waiting...");
        do {
            try {
                object = input.readObject();
                System.out.println(object);
                if (object.getClass() == Boolean.class) {
                    resetGame();
                    //Timer Thread
                    new Thread() {
                        public void run() {
                            answer = 0;
                            while(answer == 0){
                                for (ctr = timeLimit + timeReady; ctr >= 0 && isFinished()!=true && isClosed == false; --ctr) {
                                    preTimer = timeLimit - ctr;
                                    try {

                                        if(preTimer<=-1){
                                            timerLabel.setText(Integer.toString(ctr-timeLimit));
                                        } else if(preTimer==0){
                                            timerLabel.setText("Start!");
                                        } else {
                                            timerLabel.setText(Integer.toString(ctr));
                                        }

                                        Thread.sleep(1000);

                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(panelLANMulti.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    if(ctr==0){
                                        isTimeUp = true;
                                        //pointCounter();
                                    }
                                } 
                                
                                if(isClosed)break;
                                
                                displayWinner();
                                answer = JOptionPane.showOptionDialog(null , "Want to play again?", "Game End", JOptionPane.YES_NO_CANCEL_OPTION, 
                                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                                if(answer == 0) resetGame();
                            }
                            
                            if(isClosed == false){
                                sendData("TERMINATE");
                            }
                        }
                    }.start();
                    
                    continue;
                }

                if (object.getClass() == String.class) {
                    opponentName = (String) object;
                    continue;
                }
                
                if(object.getClass() == KeyStroke.class){
                    opponentTap((KeyStroke)object);
                    continue;
                }
                
                opponentTap();
            } catch (ClassNotFoundException ex) {
                displayMessage("\nUnkown object type received");
            }
        } while (!object.equals("TERMINATE"));
    }

    private void closeConnection() {
        displayMessage("\nTerminating connection\n");

        try {
            output.close();
            input.close();
            client.close();
            isClosed = true;
        } catch (IOException ex) {
            Logger.getLogger(panelLANMulti.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendData(Object object) {
        try {
            output.writeObject(object);
            output.flush();

        } catch (IOException ex) {
            displayMessage("\nError writing object");
        }
    }

    private void displayMessage(final String message) {
        System.out.println(message);
    }

    public void getPlayerName() {
        playerName = JOptionPane.showInputDialog("Input Your Name");
        if (playerName == null || playerName.equals("")) {
            playerName = "Client";
        }
    }
    
    public void opponentTap(){
        player2ctr ++;
        barMain.setValue(barMain.getValue() - 1);
        p2Label.setText(""+player2ctr);
        //tap steal at payer 2
        if(player2ctr==20){
            p2power.setText("Opponent Can Steal Your taps!");
        }
    }
    
    public void opponentTap(KeyStroke combo){
        p2power.setText("YOUR TAPS STOLEN");
        player1ctr-=10;
        p1Label.setText(""+player1ctr);
        isP2ComboDone = true;
    }
    
    public void resetGame(){
        barMain.setValue(50);
        player2ctr = 0;
        player1ctr = 0;
        timerLabel.setText("");
        p1Label.setText("You");
        p2Label.setText("Opponent");
        p1power.setText("No power");
        p2power.setText("No power");        
        isTimeUp = false;
        isClosed = false;
    }
    
    public void displayWinner(){
        if(player1ctr<player2ctr){
            timerLabel.setText("You Lose");
        } else if (player1ctr>player2ctr){
            timerLabel.setText("You Win");
        }else{
            timerLabel.setText("Draw");
        }
        
    }
    
    public boolean isFinished(){
        return (barMain.getValue()>=100 || barMain.getValue()<=0);
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barMain = new javax.swing.JProgressBar();
        timerLabel = new javax.swing.JLabel();
        p1Label = new javax.swing.JLabel();
        p2power = new javax.swing.JLabel();
        p2Label = new javax.swing.JLabel();
        p1power = new javax.swing.JLabel();

        setBackground(new java.awt.Color(63, 32, 69));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        barMain.setBackground(new java.awt.Color(250, 250, 250));
        barMain.setValue(50);

        timerLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        timerLabel.setForeground(new java.awt.Color(255, 255, 40));
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        p1Label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        p1Label.setForeground(new java.awt.Color(254, 220, 159));
        p1Label.setText("You");

        p2power.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        p2power.setForeground(new java.awt.Color(255, 255, 40));
        p2power.setText("No power");

        p2Label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        p2Label.setForeground(new java.awt.Color(254, 220, 159));
        p2Label.setText("Opponent");

        p1power.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        p1power.setForeground(new java.awt.Color(255, 255, 40));
        p1power.setText("No power");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(95, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(timerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(89, 89, 89))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(p1power)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(p2power))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(p1Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(p2Label)))
                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(timerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(barMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 256, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(p2Label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p1power, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(p2power, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void moveBar(java.awt.event.KeyEvent evt){
        KeyStroke combo1 = KeyStroke.getKeyStroke(evt.getKeyCode(), evt.getModifiers());
        if(isTimeUp == false && preTimer >= 0){
            if(evt.getKeyCode()== KeyEvent.VK_Z){
                sendData(1);
                player1ctr++;
                p1Label.setText(""+player1ctr);
                barMain.setValue(barMain.getValue() + 1);
                //tap steal at player 1
                if(player1ctr==20){
                    p1power.setText("Press Shift+A to steal taps!");
                }    
            }else if(combo1.equals(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.SHIFT_DOWN_MASK)) && player1ctr>=20 && isP1ComboDone == false){
                sendData(combo1);
                p1power.setText("P2's TAPS STOLEN");
                player2ctr-=10;
                p2Label.setText(""+player2ctr);
                System.out.println("Works");
                isP1ComboDone = true;
            }
        }
    }
    
    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        moveBar(evt);
    }//GEN-LAST:event_formKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barMain;
    private javax.swing.JLabel p1Label;
    private javax.swing.JLabel p1power;
    private javax.swing.JLabel p2Label;
    private javax.swing.JLabel p2power;
    private javax.swing.JLabel timerLabel;
    // End of variables declaration//GEN-END:variables
}
