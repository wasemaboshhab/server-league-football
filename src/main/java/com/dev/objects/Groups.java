package com.dev.objects;

import javax.persistence.*;


@Entity
@Table(name = "static_table")
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String name;
    @Column
    private int position;
    @Column
    private int points;
    @Column
    private int goalsDifferences;
    @Column
    private int goalsAgainst;
    @Column
    private int goals;


    public Groups(String name,  int points,  int goalsAgainst, int goals) {

        this.name = name;
        this.points = points;
        this.goalsAgainst = goalsAgainst;
        this.goals = goals;
    }

    public Groups() {

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

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGoalsDifferences() {
        return goalsDifferences;
    }

    public void setGoalsDifferences(int goalsDifferences) {
        this.goalsDifferences = goalsDifferences;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }
}
