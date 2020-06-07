package modelo;

import java.io.Serializable;

public class Race implements Serializable {
    private String id;
    private String name;
    private String place;
    private String web;
    private String country;

    public Race(String id, String name, String place, String web, String country) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.web = web;
        this.country = country;
    }
    public Race (Race race){
        this.id = race.getId();
        this.name = race.getName();
        this.place = race.getPlace();
        this.web = race.getWeb();
        this.country = race.getCountry();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return  "\n=========================================" +
                "\nId: " + id +
                "\nNombre: " + name +
                "\nLugar: " + place +
                "\nPágina web: " + web +
                "\nProvincia: " + country +
                "\n=========================================";
    }
}
