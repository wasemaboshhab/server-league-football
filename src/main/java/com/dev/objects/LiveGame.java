package com.dev.objects;

import javax.persistence.*;
 // not finish
@Entity
@Table(name = "live")
public class LiveGame {

    @Id
    int id;
    String team1;
    String team2;

     public LiveGame(String team1, String team2, int team1Goals, int team2Goals) {
         this.team1 = team1;
         this.team2 = team2;
         this.team1Goals = team1Goals;
         this.team2Goals = team2Goals;
     }

     public LiveGame() {

     }

     public int getId() {
         return id;
     }

     public void setId(int id) {
         this.id = id;
     }

     public String getTeam1() {
         return team1;
     }

     public void setTeam1(String team1) {
         this.team1 = team1;
     }

     public String getTeam2() {
         return team2;
     }

     public void setTeam2(String team2) {
         this.team2 = team2;
     }

     public int getTeam1Goals() {
         return team1Goals;
     }

     public void setTeam1Goals(int team1Goals) {
         this.team1Goals = team1Goals;
     }

     public int getTeam2Goals() {
         return team2Goals;
     }

     public void setTeam2Goals(int team2Goals) {
         this.team2Goals = team2Goals;
     }

     int team1Goals;
    int team2Goals;

}
