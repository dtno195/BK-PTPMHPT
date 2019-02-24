package hust.server;

import hust.service.WeatherService;
import hust.service.WeatherServiceImpl;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WeatherServer {
    private static final int PORT = 1099;
    private static Registry registry;

    public static void startRegistry() throws RemoteException {

        // Tạo một bộ đăng ký (Registry) tại Server.
        registry =  LocateRegistry.createRegistry(PORT);
    }

    public static void registerObject(String name, Remote remoteObj)
            throws RemoteException, AlreadyBoundException {
        // Đăng ký đối tượng vào bộ đăng ký.
        // Nó được gắn với cái tên nào đó.
        // Client sẽ tìm trên bộ đăng ký với tên này để có thể gọi đối tượng.


        registry.bind(name, remoteObj);
        System.out.println("Registered: " + name + " -> "
                + remoteObj.getClass().getName() + "[" + remoteObj + "]");
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Server starting...");
        startRegistry();
        registerObject(WeatherService.class.getSimpleName(), new WeatherServiceImpl());

        // Server đã được start, và đang lắng nghe các request từ Client.
        System.out.println("Server started!");
    }
}
