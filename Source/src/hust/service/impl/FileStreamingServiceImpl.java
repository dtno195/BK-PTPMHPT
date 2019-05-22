package hust.service.impl;

import hust.service.FileStreamingService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;

public class FileStreamingServiceImpl
        extends UnicastRemoteObject
        implements FileStreamingService {

    private static String current = "";

    public FileStreamingServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public ArrayList<String[]> list() throws RemoteException {
        System.out.println(current);

        ArrayList<String[]> list = new ArrayList<>();
        if (!current.isEmpty()) {
            File file = new File(current);

            list.add(new String[]{file.getAbsolutePath()});

            for (String x : Objects.requireNonNull(file.list())) {
                list.add(new String[]{new File(file + "/" + x).isFile() ? "FILE" : "DIRECTORY", x});
            }

        } else {
            list.add(new String[]{"ROOT"});
            for (File x : findRoots()) list.add(new String[]{"DIRECTORY", (x + "").substring(0, 2)});

        }
        return list;
    }

    @Override
    public boolean createFolder(String folderName) throws RemoteException {
        File file = new File(current + folderName);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Folder is created!");
                return true;
            }
            System.out.println("Failed to create folder!");
            return false;
        }
        return false;
    }

    @Override
    public boolean delete(String filename) throws RemoteException {
        while (!new File(current + filename).delete()) ;
        return true;
    }

    @Override
    public boolean back() throws RemoteException {
        current = new File(current).getParent() == null ? "" : (new File(current).getParent());
        return true;
    }

    @Override
    public void forward(String to) throws RemoteException {
        current += to + "/";
    }

    @Override
    public File[] findRoots() throws RemoteException {
        return File.listRoots();
    }

    @Override
    public void goToRoot() throws RemoteException {
        current = "";
    }

    @Override
    public byte[] download(String filename) throws RemoteException {
        File f = new File(current + filename);
        if (!f.exists()) return null;
        try {
            byte[] bytes = new byte[(int) f.length()];

            FileInputStream fis = new FileInputStream(f);
            fis.read(bytes);

            fis.close();
            return bytes;

        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public boolean upload(byte[] data, String filename) throws RemoteException {
        try {
            File f = new File(current + filename);
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(data);

            fos.close();
            return true;

        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

}
