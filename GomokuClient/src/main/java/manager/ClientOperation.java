package manager;

import rmi.RMIInterface;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientOperation {

    private static RMIInterface look_up;

    public static void main(String[] args)
            throws MalformedURLException, RemoteException, NotBoundException {

        look_up = (RMIInterface) Naming.lookup("//localhost/MyServer");
        String playerName = "p1";
        String gameName = "g1";

        String response = look_up.createGame(playerName, gameName);
        JOptionPane.showMessageDialog(null, response);

    }

}