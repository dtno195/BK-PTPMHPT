package hust.service;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface FileStreamingService extends Remote {
    ArrayList<String[]> list() throws RemoteException;

    boolean createFolder(String folderName) throws RemoteException;

    boolean delete(String filename) throws RemoteException;

    boolean back() throws RemoteException;

    void forward(String to) throws RemoteException;

    File[] findRoots() throws RemoteException;

    void goToRoot() throws RemoteException;

    byte[] download(String filename) throws RemoteException;

    boolean upload(byte[] data, String filename) throws RemoteException;

}