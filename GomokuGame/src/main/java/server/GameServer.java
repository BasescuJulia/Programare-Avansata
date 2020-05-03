package server;

import org.apache.commons.lang.StringUtils;
import server.models.Game;
import server.models.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class GameServer {
    private static int PORT = 6767;
    private List<Player> playerList;
    private List<Game> gameList;

    public static GameServer serverInstance = null;

    private GameServer(){
        if (serverInstance == null) {
            serverInstance = new GameServer();
        }
    }


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();

               ClientThread newClient = new ClientThread(socket);
               newClient.run();

               socket.close();
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public List<Game> getGameList() {
        return gameList;
    }
}
