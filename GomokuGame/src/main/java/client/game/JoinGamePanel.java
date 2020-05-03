package client.game;

import client.board.Board;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoinGamePanel extends JPanel {

    public JoinGamePanel(final JFrame mainFrame) {

        JLabel userNameLabel = new JLabel("Enter your name:");
        final JTextField userName = new JTextField(20);

        this.add(userNameLabel);
        userName.setBounds(70, 30, 150, 20);
        this.add(userName);

        JLabel gameNameLabel = new JLabel("Enter game name:");
        final JTextField gameName = new JTextField(20);

        this.add(gameNameLabel);
        gameName.setBounds(70, 50, 150, 20);
        this.add(gameName);

        JButton createGameButton = new JButton("Join game");

        createGameButton.setBounds(110, 100, 80, 20);

        this.add(createGameButton);

        setVisible(true);
        createGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String userNameText = userName.getText();
                String gameNameText = gameName.getText();
                if (StringUtils.isEmpty(userNameText)) {
                    JOptionPane.showMessageDialog(null, "Player name cannot be empty!");
                } else if (StringUtils.isEmpty(gameNameText)) {
                    JOptionPane.showMessageDialog(null, "Game name cannot be empty!");
                } else {
                    joinGamePanel(userNameText, gameNameText, mainFrame);
                }

            }
        });
    }

    private void joinGamePanel(String userNameText, String gameNameText, JFrame mainFrame) {
        //        afisam fereastra de joc
        int size = 15;

        JFrame gameFrame = new JFrame();

        final int FRAME_WIDTH = 600;
        final int FRAME_HEIGHT = 650;
        gameFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        gameFrame.setTitle("Gomoku | Game name: " + gameNameText + " | Player: " + userNameText);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Board panel = new Board(size);
        gameFrame.add(panel);

        gameFrame.setVisible(true);
        mainFrame.setVisible(false);
    }
}
