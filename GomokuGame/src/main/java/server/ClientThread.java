package server;

import org.apache.commons.lang.StringUtils;
import server.models.Game;
import server.models.Player;

import java.io.*;
import java.net.Socket;
import java.text.ParseException;

public class ClientThread implements Runnable {
    private final Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            String text;
            do {
                text = reader.readLine();
                if (text != null) {
                    String command = new StringBuilder(text).toString();
                    if (StringUtils.isNotEmpty(command)) {
                        writer.println("Server received the request");
                        System.out.println("Command received: " + command);
                        String result = executeCommand(command);
                        writer.println(result);
                    }
                }

            } while (text == null || !text.equals("stop"));
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String executeCommand(String command) {
        String result = "Command not supported!";
        if (command.contains("create a game")) {
            try {
                String[] params = command.split(" ");
                String playerName = params[3];
                String gameName = params[4];
                boolean playerExists = false;
                boolean gameExists = false;
                for (Player p : GameServer.serverInstance.getPlayerList()) {
                    if (p.getName().equals(playerName)) {
                        playerExists = true;
                        break;
                    }
                }
                if (playerExists) {
                    return "Player name already exists!";
                }
                for (Game g : GameServer.serverInstance.getGameList()) {
                    if (g.getName().equals(gameName)) {
                        gameExists = true;
                        break;
                    }
                }
                if (gameExists) {
                    return "Game name already exists!";
                }

                Player player = new Player();
                player.setName(playerName);
                GameServer.serverInstance.getPlayerList().add(player);
                Game game = new Game();
                game.setName(gameName);
                GameServer.serverInstance.getGameList().add(game);
            } catch (IndexOutOfBoundsException e) {
                return "Invalid number of parameters!";
            }

        } else if (command.contains("join a game")) {
            try {
                String[] params = command.split(" ");
                String playerName = params[3];
                String gameName = params[4];
                boolean playerExists = false;
                boolean gameExists = false;
                for (Player p : GameServer.serverInstance.getPlayerList()) {
                    if (p.getName().equals(playerName)) {
                        playerExists = true;
                        break;
                    }
                }
                if (playerExists) {
                    return "Player name already exists!";
                }
                for (Game g : GameServer.serverInstance.getGameList()) {
                    if (g.getName().equals(gameName)) {
                        gameExists = true;
                        break;
                    }
                }
                if (!gameExists) {
                    return "Game doesn't exist!";
                }
                Player player = new Player();
                player.setName(playerName);
                GameServer.serverInstance.getPlayerList().add(player);
            } catch (IndexOutOfBoundsException e) {
                return "Invalid number of parameters!";
            }

        } else if (command.contains("submit a move")) {
            try {
                String[] params = command.split(" ");
                Integer row = Integer.parseInt(params[3]);
                Integer column = Integer.parseInt(params[4]);

                if (row > 15 || column > 15) {
                    return "Invalid position! (larger than 15)";
                }
                GameState.instance.playPiece(row, column);
            } catch (IndexOutOfBoundsException e) {
                return "Invalid number of parameters!";
            } catch (NumberFormatException e) {
                return "Invalid position";
            }

        } else if (command.contains("stop")) {
            return "Server stopped!";
        }
        return result;
    }
}
