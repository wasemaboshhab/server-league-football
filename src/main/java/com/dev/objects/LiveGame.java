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
    int team1Goals;
    int team2Goals;

}
