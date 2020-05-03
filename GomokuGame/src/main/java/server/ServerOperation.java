package server;
import rmi.RMIInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ServerOperation extends UnicastRemoteObject implements RMIInterface {

    private static final long serialVersionUID = 1L;

    protected ServerOperation() throws RemoteException {

        super();

    }

    @Override
    public String createGame(String gameName, String playerName) throws RemoteException{

        System.err.println(playerName + " is trying to create game: " + gameName);
        return "Server says hello to " + playerName;

    }

    @Override
    public String joinGame(String playerName, String gameName) throws RemoteException {
        return null;
    }

    @Override
    public String submitMove(String gameName, String playerName, int row, int column) throws RemoteException {
        return null;
    }

    public static void main(String[] args){

        try {

            Naming.rebind("//localhost/MyServer", new ServerOperation());
            System.err.println("Server ready");

        } catch (Exception e) {

            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();

        }

    }

}