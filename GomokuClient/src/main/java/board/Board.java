package board;

import com.jcraft.jsch.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Board extends JPanel {
    private final int MARGIN = 5;
    private final double PIECE_FRAC = 0.9;

    private int size = 19;
    private GameState state;

    public Board() {
        this(15);
    }

    public Board(int size) {
        super();
        this.size = size;
        state = new GameState(size);
        addMouseListener(new MouseListener());
    }

    class MouseListener extends MouseAdapter {
        public void mouseReleased(MouseEvent e) {
            double panelWidth = getWidth();
            double panelHeight = getHeight();
            double boardWidth = Math.min(panelWidth, panelHeight) - 2 * MARGIN;
            double squareWidth = boardWidth / size;
            double xLeft = (panelWidth - boardWidth) / 2 + MARGIN;
            double yTop = (panelHeight - boardWidth) / 2 + MARGIN;
            int col = (int) Math.round((e.getX() - xLeft) / squareWidth - 0.5);
            int row = (int) Math.round((e.getY() - yTop) / squareWidth - 0.5);
            if (row >= 0 && row < size && col >= 0 && col < size
                    && state.getPiece(row, col) == GameState.NONE
                    && state.getWinner() == GameState.NONE) {
                state.playPiece(row, col);
                repaint();
                int winner = state.getWinner();
                if (winner != GameState.NONE) {
                    JOptionPane.showMessageDialog(null,
                            (winner == GameState.BLACK) ? "Black wins!"
                                    : "White wins!");
                    uploadFinalBoardState();
                }
            }
        }
    }

    private void uploadFinalBoardState() {
        try {
            JSch ssh = new JSch();
            Session session = ssh.getSession("username", "myip90000.ordomain.com", 22);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword("Passw0rd");

            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftp = (ChannelSftp) channel;

//            cream imaginea boardului care va fi exportata
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            this.printAll(g);
            g.dispose();
            try {
                ImageIO.write(image, "png", new File("Board.png"));
            } catch (IOException exp) {
                exp.printStackTrace();
            }

            sftp.cd("archive");
            sftp.put("Board.png", "/var/www/remote/Board.png");

            Boolean success = true;

            if(success){
                System.out.println("Board uploaded successfully!");
            }

            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        } catch (SftpException e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        }
    }


    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        double panelWidth = getWidth();
        double panelHeight = getHeight();

        g2.setColor(new Color(1f, 0.6f, 0.23f));
        g2.fill(new Rectangle2D.Double(0, 0, panelWidth, panelHeight));

        double boardWidth = Math.min(panelWidth, panelHeight) - 2 * MARGIN;
        double squareWidth = boardWidth / size;
        double gridWidth = (size - 1) * squareWidth;
        double pieceDiameter = PIECE_FRAC * squareWidth;
        boardWidth -= pieceDiameter;
        double xLeft = (panelWidth - boardWidth) / 2 + MARGIN;
        double yTop = (panelHeight - boardWidth) / 2 + MARGIN;

        g2.setColor(Color.BLACK);
        for (int i = 0; i < size; i++) {
            double offset = i * squareWidth;
            g2.draw(new Line2D.Double(xLeft, yTop + offset,
                    xLeft + gridWidth, yTop + offset));
            g2.draw(new Line2D.Double(xLeft + offset, yTop,
                    xLeft + offset, yTop + gridWidth));
        }

        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++) {
                int piece = state.getPiece(row, col);
                if (piece != GameState.NONE) {
                    Color c = (piece == GameState.BLACK) ? Color.BLACK : Color.WHITE;
                    g2.setColor(c);
                    double xCenter = xLeft + col * squareWidth;
                    double yCenter = yTop + row * squareWidth;
                    Ellipse2D.Double circle
                            = new Ellipse2D.Double(xCenter - pieceDiameter / 2,
                            yCenter - pieceDiameter / 2,
                            pieceDiameter,
                            pieceDiameter);
                    g2.fill(circle);
                    g2.setColor(Color.black);
                    g2.draw(circle);
                }
            }
    }
}
