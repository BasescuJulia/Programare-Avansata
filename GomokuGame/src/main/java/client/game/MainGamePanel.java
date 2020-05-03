package client.game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGamePanel extends JPanel {

    public MainGamePanel(final JFrame mainFrame) {

        JButton createGameButton = new JButton("Create game");

        createGameButton.setBounds(50, 100, 80, 20);

        this.add(createGameButton);

        JButton joinGameButton = new JButton("Join game");

        joinGameButton.setBounds(50, 150, 80, 20);

        this.add(joinGameButton);

        setVisible(true);
        createGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame createGameFrame = new JFrame();
                createGameFrame.setSize(350, 150);
                createGameFrame.setTitle("Create new game");
                createGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                CreateGamePanel createGamePanel = new CreateGamePanel(createGameFrame);
                createGameFrame.add(createGamePanel);

                createGameFrame.setVisible(true);
                mainFrame.setVisible(false);
            }
        });

        joinGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame joinGameFrame = new JFrame();
                joinGameFrame.setSize(350, 150);
                joinGameFrame.setTitle("Join new game");
                joinGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JoinGamePanel joinGamePanel = new JoinGamePanel(joinGameFrame);
                joinGameFrame.add(joinGamePanel);

                joinGameFrame.setVisible(true);
                mainFrame.setVisible(false);
            }
        });
    }
}
