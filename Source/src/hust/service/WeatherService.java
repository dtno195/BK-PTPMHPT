package hust.service;

import hust.model.Weather;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface WeatherService extends Remote {
    public Weather getWeather(Date date , String location) throws RemoteException;
}
