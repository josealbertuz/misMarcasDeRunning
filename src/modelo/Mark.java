package modelo;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;



public class Mark implements Serializable {
    private String id;
    private Race race;
    private int year;
    private int overallRaking;
    private int categoryRanking;
    private LocalTime mark;
    private boolean published;


    public Mark(Race race, int year, int overallRaking, int categoryRanking, LocalTime mark, boolean published) {
        id = Long.toHexString(System.currentTimeMillis() / 100000).toUpperCase();
        this.race = new Race(race);
        this.year = year;
        this.overallRaking = overallRaking;
        this.categoryRanking = categoryRanking;
        this.mark = mark;
        this.published = published;
    }
    public Mark (Mark mark){
        this.id = mark.getId();
        this.race = mark.getRace();
        this.year = mark.getYear();
        this.overallRaking = mark.getOverallRaking();
        this.categoryRanking = mark.getCategoryRanking();
        this.mark = mark.getMark();
        this.published = mark.isPublished();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getOverallRaking() {
        return overallRaking;
    }

    public void setOverallRaking(int overallRaking) {
        this.overallRaking = overallRaking;
    }

    public int getCategoryRanking() {
        return categoryRanking;
    }

    public void setCategoryRanking(int categoryRanking) {
        this.categoryRanking = categoryRanking;
    }

    public LocalTime getMark() {
        return mark;
    }

    public void setMark(LocalTime mark) {
        this.mark = mark;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean checkMark(String mark) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("hh:mm:ss");
        try {
            dtf.parseLocalTime(mark);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int compareTo (Mark m){
        int result;

        if (mark.isBefore(m.getMark())) result = -1;
        else if (mark.isAfter(m.getMark())) result = 1;
        else result = 0;

        return result;
    }

    @Override
    public String toString() {
        DateTimeFormatter dft = DateTimeFormat.forPattern("HH:mm:ss");
        String overallRanking = this.overallRaking == 0 ? "No existen datos" : Integer.toString(this.overallRaking);
        String categoryRanking = this.categoryRanking == 0? "No existen datos" : Integer.toString(this.categoryRanking);
        String published = this.published? "Si" : "No";
        return  "\n=============================" +
                "\nId: " + id +
                "\nNombre de la carrera: " + race.getName() +
                "\nAño: " + year +
                "\nClasificación general: " + overallRanking  +
                "\nClasificacion por categoria: " + categoryRanking +
                "\nMarca: " + dft.print(mark) +
                "\nPublica: " + published +
                "\n=============================";
    }
}
