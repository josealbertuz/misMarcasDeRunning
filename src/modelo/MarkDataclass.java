package modelo;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class MarkDataclass {
    private String name;
    private LocalTime time;
    private int year;

    private boolean published;

    MarkDataclass(String name, LocalTime time, int year, boolean published){
        this.name = name;
        this.time = time;
        this.year = year;
        this.published = published;
    }

    MarkDataclass(MarkDataclass markDataclass){
        this.name = markDataclass.getName();
        this.time = markDataclass.getTime();
        this.year = markDataclass.getYear();
        this.published = markDataclass.isPublished();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public int compareTo (MarkDataclass markDataclass){
        int result;

        if (time.isBefore(markDataclass.getTime())) result = -1;
        else if (time.isAfter(markDataclass.getTime())) result = 1;
        else result = 0;

        return result;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm:ss");
        name = published? name : "No publica";
        return  "\n=============================" +
                "\nUsuario: " + name +
                "\nMarca: " + dtf.print(time) +
                "\nAño: " + year +
                "\n=============================";
    }
}
