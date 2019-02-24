package hust.service;

import hust.constants.WeatherConstants;
import hust.model.Weather;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.Date;

public class WeatherServiceImpl extends UnicastRemoteObject implements WeatherService {
    private static final long serialVersionUID = 1L;

    public WeatherServiceImpl() throws RemoteException {
    }

    @Override
    public synchronized  Weather getWeather(Date date, String location) throws RemoteException {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        // Sunday, Monday
        if (dayOfWeek == 1 || dayOfWeek == 2) {
            if (location.equals(WeatherConstants.LOCATION_CHICAGO)) {
                // Rain
                return new Weather(date, location, WeatherConstants.WEATHER_RAIN);
            } else if (location.equals(WeatherConstants.LOCATION_HANOI)) {
                // Sunny
                return new Weather(date, location, WeatherConstants.WEATHER_SUNNY);
            } else if (location.equals(WeatherConstants.LOCATION_TOKYO)) {
                // Sunny
                return new Weather(date, location, WeatherConstants.WEATHER_SUNNY);
            }
            return new Weather(date, location, WeatherConstants.WEATHER_SUNNY);
        } else {
            return new Weather(date, location, WeatherConstants.WEATHER_SUNNY);
        }
    }
}
