package hust.client;

import hust.constants.WeatherConstants;
import hust.model.Weather;
import hust.service.WeatherService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

public class WeatherClient {
    // Host or IP of Server
    private static final String HOST = "localhost";
    private static final int PORT = 1099;
    private static Registry registry;

    public static void main(String[] args) throws Exception {

        // Search the registry in the specific Host, Port.
        registry = LocateRegistry.getRegistry(HOST, PORT);

        // Lookup WeatherService in the Registry.
        WeatherService service = (WeatherService) registry
                .lookup(WeatherService.class.getSimpleName());

        Date today = new Date();

        // Get Chicago weather info:
        Weather chicagoWeather = service.getWeather(today,
                WeatherConstants.LOCATION_CHICAGO);

        System.out.println("Chicago weather today: "
                + chicagoWeather.getWeather());

        // Get Hanoi weather info:
        Weather hanoiWeather = service.getWeather(today,
                WeatherConstants.LOCATION_HANOI);

        System.out.println("Hanoi weather today: " + hanoiWeather.getWeather());

    }
}
