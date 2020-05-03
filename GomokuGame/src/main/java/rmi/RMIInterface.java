package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {

    public String createGame(String playerName, String gameName) throws RemoteException;
    public String joinGame(String playerName, String gameName) throws RemoteException;
    public String submitMove(String gameName, String playerName, int row, int column) throws RemoteException;
}