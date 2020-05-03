
import game.MainGamePanel;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameClient {
    private static String HOST = "localhost";
    private static Integer PORT = 6767;


    public static void main(String[] args) {
//      1. GUI  afisam fereastra de alegere: joc nou sau alatura-te la joc existent
        JFrame mainGameFrame = new JFrame();

        mainGameFrame.setSize(350, 150);
        mainGameFrame.setTitle("Gomoku game");
        mainGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainGamePanel mainGamePanel = new MainGamePanel(mainGameFrame);
        mainGameFrame.add(mainGamePanel);

        mainGameFrame.setVisible(true);

//      2. citire argumente de la tastatura
        try (Socket socket = new Socket(HOST, PORT)) {

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            Console console = System.console();
            String text;
            System.out.println("** GOMOKU Game ** \n" +
                    "Available operations: \n" +
                    "create a game (eg. create a game player1 game1, where player1 is your player name, and game name will be used to join)\n" +
                    "join a game (eg. join a game player2 game1, where player2 is your player name, and game name will be join)\n" +
                    "submit a move (eg. submit a move 13 12 where 13 and 12 are the row and the column on the board, respectively\n" +
                    "exit (exits the game)\n" +
                    "stop (closes the server)\n");

            do {
                text = readLine("Enter operation: ");

                writer.println(text);

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String serverText = reader.readLine();

                if (text.equals("exit")) {
                    System.out.println("Client exiting...");
                    socket.close();
                } else {
                    System.out.println(serverText);
                }

            } while (!text.equals("exit"));

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }


    }

    private static String readLine(String format, Object... args) throws IOException {
        if (System.console() != null) {
            return System.console().readLine(format, args);
        }
        System.out.print(String.format(format, args));
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));
        return reader.readLine();
    }
}

