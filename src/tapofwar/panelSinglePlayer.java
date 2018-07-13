/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tapofwar;

import com.sun.glass.events.KeyEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author LoganLee
 */
public class panelSinglePlayer extends javax.swing.JPanel {
    private int player1ctr;
    private int compCtr;
    private int ctr;
    private final int timeLimit;
    private final int timeReady;
    private int preTimer;
    private Random random;
    private boolean isTimeUp;
    private final Object[] options;
    private panelMain panelMain;
    private boolean isStart;
    private int answer;
    /**
     * Creates new form panelSinglePlayer
     */
    public panelSinglePlayer(panelMain panelMain) {
        this.options = new Object[]{"Yes", "No"};
        initComponents();
        
        this.panelMain = panelMain;
        random = new Random();
        
        timeReady = 5;
        timeLimit = 10;
       
    }
    
    public void startGame(){
        resetGame();
        //Timer Thread
        new Thread() {
                public void run() {

                    answer = 0;

                    while(answer == 0){
                        for (ctr = timeLimit + timeReady; ctr >= 0 && isFinished()!=true; --ctr) {
                            preTimer = timeLimit - ctr;
                            try {

                                if(preTimer<=-1){
                                    timerLabel.setText(Integer.toString(ctr-timeLimit));
                                } else if(preTimer==0){
                                    timerLabel.setText("Start!");
                                    isStart = true;
                                } else {
                                    timerLabel.setText(Integer.toString(ctr));
                               
                                }

                                Thread.sleep(1000);

                            } catch (InterruptedException ex) {
                                Logger.getLogger(panelSinglePlayer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if(ctr==0){
                                isTimeUp = true;
                            }
                        }
                        isStart = false;
                        displayWinner();
                        answer = JOptionPane.showOptionDialog(null , "Want to play again?", "Game End", JOptionPane.YES_NO_CANCEL_OPTION, 
                                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                        if(answer == 0) resetGame();
                    }

                    panelMain.showMain();
                }
        }.start();
        
        //Computer Thread
        new Thread(){

            public void run(){
                while(answer == 0){
                    try {
                        if(isStart){
                            compCtr = random.nextInt(2);
                            barMain.setValue(barMain.getValue()+compCtr);
                        }
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(panelSinglePlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }
    
    public void displayWinner(){
        if(barMain.getValue()>=100 || player1ctr<compCtr){
            timerLabel.setText("You Lose");
        } else if (barMain.getValue()<=0 || player1ctr>compCtr){
            timerLabel.setText("You Win");
        }
        
    }
    
    public void resetGame(){
        barMain.setValue(50);
        compCtr = 0;
        player1ctr = 0;
        timerLabel.setText("");
        isTimeUp = false;
    }
    
    public boolean isFinished(){
        return (barMain.getValue()>=100 || barMain.getValue()<=0);
    }
    
    public void moveBar(java.awt.event.KeyEvent evt){
        if(isTimeUp == false && preTimer >= 0){
            if(evt.getKeyCode()== KeyEvent.VK_M){
                player1ctr++;
                barMain.setValue(barMain.getValue() - 1);
            }
        }
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
        ctrlabel = new javax.swing.JLabel();

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

        ctrlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(barMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(ctrlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(timerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(timerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(barMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(ctrlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(370, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        moveBar(evt);
    }//GEN-LAST:event_formKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barMain;
    private javax.swing.JLabel ctrlabel;
    private javax.swing.JLabel timerLabel;
    // End of variables declaration//GEN-END:variables
}
