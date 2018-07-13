/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tapofwar;

import com.sun.glass.events.KeyEvent;
import java.awt.event.InputEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author LoganLee
 */
public class panelLocalMulti extends javax.swing.JPanel{
    private int timerState;
    private int ctr;
    private int player1ctr=0;
    private int player2ctr=0;
    private int timeLimit = 20;
    private int timeReady = 5;
    private boolean isTimeUp = false;
    private int preTimer;
    private boolean isP1ComboDone = false;
    private boolean isP2ComboDone = false;
    private boolean isFrozen1;
    private boolean isFrozen2;
    private int answer;
    private int comboTimer;
    private final Object[] options;
    private panelMain panelMain;
    /**
     * Creates new form panel2Player
     */
    public panelLocalMulti(panelMain panelMain) {
        this.options = new Object[]{"Yes", "No"};
        this.panelMain = panelMain;
        initComponents();
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
                                } else {
                                    timerLabel.setText(Integer.toString(ctr));
                                }

                                Thread.sleep(1000);

                            } catch (InterruptedException ex) {
                                Logger.getLogger(panelLocalMulti.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if(ctr==0){
                                isTimeUp = true;
                            }
                        }
                        displayWinner();
                        answer = JOptionPane.showOptionDialog(null , "Want to play again?", "Game End", JOptionPane.YES_NO_CANCEL_OPTION, 
                                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                        if(answer == 0) resetGame();
                    }

                    panelMain.showMain();
                }
        }.start();
    }
    
    public void displayWinner(){
        if(player1ctr<player2ctr){
            timerLabel.setText("Player 2 Won");
        } else if (player1ctr>player2ctr){
            timerLabel.setText("Player 1 Won");
        }
        
    }
    
    public void resetGame(){
        barMain.setValue(50);
        player2ctr = 0;
        player1ctr = 0;
        timerLabel.setText("");
        p1Label.setText("Player 1");
        p2Label.setText("Player 2");
        p2Power2.setText("No power");
        p2power.setText("No power");
        isTimeUp = false;
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
        p1Label = new javax.swing.JLabel();
        p2Label = new javax.swing.JLabel();
        p2Power2 = new javax.swing.JLabel();
        timerLabel = new javax.swing.JLabel();
        p2power = new javax.swing.JLabel();
        p1power = new javax.swing.JLabel();
        p1power2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(63, 32, 69));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        barMain.setValue(50);

        p1Label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        p1Label.setForeground(new java.awt.Color(254, 220, 159));
        p1Label.setText("Player 1");

        p2Label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        p2Label.setForeground(new java.awt.Color(254, 220, 159));
        p2Label.setText("Player 2");

        p2Power2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        p2Power2.setForeground(new java.awt.Color(255, 255, 40));
        p2Power2.setText("No power");

        timerLabel.setFont(new java.awt.Font("Tahoma", 0, 25)); // NOI18N
        timerLabel.setForeground(new java.awt.Color(255, 255, 40));
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        p2power.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        p2power.setForeground(new java.awt.Color(255, 255, 40));
        p2power.setText("No power");

        p1power.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        p1power.setForeground(new java.awt.Color(255, 255, 40));
        p1power.setText("No power");

        p1power2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        p1power2.setForeground(new java.awt.Color(255, 255, 40));
        p1power2.setText("No power");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(p1Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(p2Label)
                        .addGap(72, 72, 72))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(barMain, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(timerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(p1power2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(p2power, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(p2Power2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(37, 37, 37)
                    .addComponent(p1power, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(174, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(timerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barMain, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 185, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(p2Label))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p2Power2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(p1power2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(p2power, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(512, Short.MAX_VALUE)
                    .addComponent(p1power, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(2, 2, 2)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        moveBar(evt);
    }//GEN-LAST:event_formKeyReleased
    
    public void moveBar(java.awt.event.KeyEvent evt){
        KeyStroke combo1 = KeyStroke.getKeyStroke(evt.getKeyCode(), evt.getModifiers());
        if(isTimeUp == false && preTimer >= 0){
            if(evt.getKeyCode()== KeyEvent.VK_Z && isFrozen1==false){
                player1ctr++;
                p1Label.setText(""+player1ctr);
                barMain.setValue(barMain.getValue() + 1);
                //tap steal at player 1
                if(player1ctr==20){
                    p1power.setText("Shift+A to steal!");
                    p1power2.setText("Shift+S to freeze!");
                }    
            }else if(combo1.equals(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.SHIFT_DOWN_MASK)) && player1ctr>=20 && isP1ComboDone == false){
                    p2Power2.setText("P2's TAPS STOLEN");
                    player2ctr-=10;
                    p2Label.setText(""+player2ctr);
                    System.out.println("Works");
                    isP1ComboDone = true;
            }else if(evt.getKeyCode()== KeyEvent.VK_M && isFrozen2==false){
                player2ctr++;
                p2Label.setText(""+player2ctr);
                barMain.setValue(barMain.getValue() - 1);
                //tap steal at payer 2
                if(player2ctr==20){
                    p2power.setText("Alt+K to steal!");
                    p2Power2.setText("Alt+L to freeze!");
                }    
            }else if(combo1.equals(KeyStroke.getKeyStroke(KeyEvent.VK_K,InputEvent.ALT_DOWN_MASK)) && player2ctr>=20 && isP2ComboDone == false){
                    p2power.setText("P1's TAPS STOLEN");
                    player1ctr-=10;
                    p1Label.setText(""+player1ctr);
                    System.out.println("Works");
                    isP2ComboDone = true;
            }else if((combo1.equals(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.SHIFT_DOWN_MASK))&& player1ctr>=20 && isP1ComboDone == false)){
                    freeze(1);
                    isP1ComboDone = true;
            }else if(combo1.equals(KeyStroke.getKeyStroke(KeyEvent.VK_L,InputEvent.ALT_DOWN_MASK))&& player2ctr>=20 && isP2ComboDone == false){
                    freeze(2);
                    isP2ComboDone = true;
            }
        }
    }

    public void freeze(int side){
        
        new Thread(){
            @Override
            public void run(){
//                
                    if(side==2){
                        isFrozen1 = true;
                    } else if (side==1){
                        isFrozen2 = true;
                    }
                    
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(panelLocalMulti.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    isFrozen1 = false;
                    isFrozen2 = false;
                }
        }.start();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barMain;
    private javax.swing.JLabel p1Label;
    private javax.swing.JLabel p1power;
    private javax.swing.JLabel p1power2;
    private javax.swing.JLabel p2Label;
    private javax.swing.JLabel p2Power2;
    private javax.swing.JLabel p2power;
    private javax.swing.JLabel timerLabel;
    // End of variables declaration//GEN-END:variables
}
