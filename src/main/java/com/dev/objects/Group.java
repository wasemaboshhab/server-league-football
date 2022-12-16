package com.dev.objects;

import javax.persistence.*;


@Entity
@Table(name = "static_table")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private int position;
    @Column
    private String name;
    @Column
    private int inFavorGoals;
    @Column
    private int goalsAgainst;
    @Column
    private int ratioOfGoals;
    @Column
    private int numberOfWins;
    @Column
    private int draws;
    @Column
    private int numberOfLosses;


    @Column
    private int points;

    public Group(String name, int goalsAgainst, int inFavorGoals, int numberOfLosses, int numberOfWins, int draws) {
        this.name = name;
        this.goalsAgainst = goalsAgainst;
        this.inFavorGoals = inFavorGoals;
        this.numberOfLosses = numberOfLosses;
        this.numberOfWins = numberOfWins;
        this.draws = draws;
    }

    public int getNumberOfLosses() {
        return numberOfLosses;
    }

    public void setNumberOfLosses(int numberOfLosses) {
        this.numberOfLosses = numberOfLosses;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public Group() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints() {
        this.points = this.draws + (this.numberOfWins * 3);
    }

    public int getRatioOfGoals() {
        return ratioOfGoals;
    }

    public void setRatioOfGoals() {
        this.ratioOfGoals = this.inFavorGoals - this.goalsAgainst;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getInFavorGoals() {
        return inFavorGoals;
    }

    public void setInFavorGoals(int goals) {
        this.inFavorGoals = goals;
    }
}
