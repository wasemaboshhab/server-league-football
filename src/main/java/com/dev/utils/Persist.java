
package com.dev.utils;

import com.dev.objects.Group;
import com.dev.objects.LiveGame;
import com.dev.objects.UserObject;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//add to git

@Component
public class Persist {

    private Connection connection;

    private final SessionFactory sessionFactory;

    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }


    @PostConstruct
    public void createConnectionToDatabase() {

        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/football_project", "root", "1234");
            System.out.println("Successfully connected to DB");
            if (checkIfTableEmpty()) {

                initGroups();
            }

//            addTeam1Goals("Milan",1); doesn't work
            updateTeam1Goals("ad", 2);
            updateTeam2Goals("ae", 3);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateTeam1Goals(String team , int updateGoals) {

        List<Integer> idList =  sessionFactory.openSession().createQuery("select id from LiveGame where team1 = : team")
                .setParameter("team", team).list();
        int matchId = idList.get(0);
        System.out.println();
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        tx = session.beginTransaction();
        LiveGame liveGame = session.get(LiveGame.class, matchId);
        liveGame.setTeam1Goals(updateGoals);
        session.update(liveGame);
        tx.commit();
    }
    public void updateTeam2Goals(String team, int updateGoals) {
        List<Integer> idList =  sessionFactory.openSession().createQuery("select id from LiveGame where team2 = : team")
                .setParameter("team", team).list();
        int matchId = idList.get(0);
        System.out.println();
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        tx = session.beginTransaction();
        LiveGame liveGame = session.get(LiveGame.class, matchId);
        liveGame.setTeam2Goals(updateGoals);
        session.update(liveGame);
        tx.commit();
    }



    //Transaction test
    public void updateUserName(String username , String token ) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        UserObject userObject = (UserObject) session.get(UserObject.class, username);
        userObject.setToken(token);
        session.update(userObject);
        tx.commit();

    }



    private boolean checkIfTableEmpty() {
        boolean empty = false;
        List<Group> groups = sessionFactory.openSession()
                .createQuery("FROM Group").list();
        if (groups.isEmpty()) {
            empty = true;
        }
        return empty;
    }

    public List<Group> getAllGroups() {
        return sessionFactory.openSession().createQuery("From Group ORDER BY points DESC ").list();

    }

    public List<String> getAllGroupsName () {
//        return sessionFactory.openSession().createQuery("from Group ").list();

        return (List<String>) sessionFactory.openSession().createQuery("SELECT name FROM Group").list();
    }
        private void initGroups() {
        List<Group> groupList = new ArrayList<>();
        groupList.add(new Group("Maccabi-Ashdod", 6, 20, 3, 10, 2));
        groupList.add(new Group("Hapoel-Afula", 2, 12, 4, 4, 7));
        groupList.add(new Group("Shaaraiim", 12, 8, 3, 3, 9));
        groupList.add( new Group("Bnai-Reina", 4, 2, 5, 9, 1));
        groupList.add(new Group("Kiryat-Gat", 6, 13, 3, 10, 2));
        groupList.add(new Group("Arayot-Rahat", 5, 14, 5, 8, 2));
        groupList.add(new Group("Bnai-Ashkelon", 7, 10, 8, 5, 2) );
        groupList.add(new Group("Netivot", 2, 20, 0, 11, 4) );
        groupList.add(new Group("Leviot-Yeruham", 4, 40, 7, 4, 4) );
        groupList.add(new Group("Totahi-Ramle", 2, 15, 5, 5, 5) );
        groupList.add(new Group("Hapoel-Natanya", 33, 33, 1, 0, 14));
        groupList.add(new Group("Milan", 28, 7, 10, 3, 2));


        for (Group group:groupList) {
            group.setRatioOfGoals();
            group.setPoints();
        }
        List<Group> groupList1 = setPositionAndAddToDatabase(groupList);
        System.out.println();

}

    private List<Group> setPositionAndAddToDatabase(List<Group> groupList) {

        int positions = 1;
        List<Group> sortTeamsByPoints = new ArrayList<>();
        for (int i = 0; i < groupList.size(); i++) {
            int indexOfGroupWithMostPoints = 0;
            int mostPoints = groupList.get(i).getPoints();
            Group team1 = groupList.get(i);
            for (int j = i+1; j < groupList.size(); j++) {
                Group team2 = groupList.get(j);
                // check if team 1 has more Points than2
                if (team1.comparePoints(team2)) {
                    indexOfGroupWithMostPoints = j;
                    break;
                } else if (team1.equalsPoints(team2)) {
                    //check if they have the same Points

                        //Check rationGoals (dose t1 have more)
                        System.out.println();
                        if (team1.compareGoalsDifference(team2)) {
                            mostPoints = team2.getPoints();
                            indexOfGroupWithMostPoints = j;

                        } else if (team1.getRatioOfGoals() == team2.getRatioOfGoals()) {
                            if (team1.lexicographical(team2) > 0) {      // ab  cd
                                mostPoints = groupList.get(j).getPoints();
                                indexOfGroupWithMostPoints = j;
                            }
                        }

//                    mostPoints = groupList.get(j).getPoints();
//                    indexOfGroupWithMostPoints = j;
                }
            }
            groupList.get(indexOfGroupWithMostPoints).setPosition(positions++);
            sortTeamsByPoints.add(groupList.get(indexOfGroupWithMostPoints));
            groupList.remove(indexOfGroupWithMostPoints);
            i = 0;
        }
        groupList.get(0).setPosition(positions);
        sortTeamsByPoints.add(groupList.get(0));
        for (Group group : sortTeamsByPoints) {
            sessionFactory.openSession().save(group);
        }
        return sortTeamsByPoints;
    }

    public List<UserObject> getAllUsersH() {

        List<UserObject> userObjectList = sessionFactory.openSession()
                .createQuery("FROM UserObject ").list();
        return userObjectList;
    }

    public void saveUser(UserObject userObject) {
        sessionFactory.openSession()
                .save(userObject);
    }

    public boolean usernameAvailableH(String username) {
        boolean available = true;
        List<UserObject> userObjects = sessionFactory.openSession()
                .createQuery("from UserObject where username =: username")
                .setParameter("username", username).list();

        if (userObjects.size() > 0) {
            available = false;
        }
        return available;
    }

    public UserObject getUserByTokenH(String token) {
        UserObject userObject = null;
        List<UserObject> userObjectList = sessionFactory.openSession()
                .createQuery("FROM UserObject WHERE token = :token")
                .setParameter("token", token)
                .list();

        if (userObjectList.size() > 0) {
            userObject = userObjectList.get(0);
        }
        return userObject;
    }

    public void addUser(String username, String token) {
        try {
            PreparedStatement preparedStatement =
                    this.connection
                            .prepareStatement("INSERT INTO users (username, token) VALUE (?,?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, token);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void addLiveGame (String team1, String team2,int team1Goals,int team2Goals) {
        try {
            PreparedStatement preparedStatement =
                    this.connection.prepareStatement("INSERT INTO live_games (team1, team2,team1Goals, team2Goals) VALUE (?,?,?,?)");
            preparedStatement.setString(1, team1);
            preparedStatement.setString(2, team2);
            preparedStatement.setInt(3,team1Goals);
            preparedStatement.setInt(4,team2Goals);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addLiveGameH (LiveGame liveGame) {
        sessionFactory.openSession().save(liveGame);
    }

    public void addTeam1Goals (String team1, int team1Goals){
        try {
            PreparedStatement preparedStatement =
                    this.connection.prepareStatement("INSERT INTO live_games (team1Goals) VALUE (?) WHERE team1=team1");
            preparedStatement.setInt(1, team1Goals);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void addTeam2Goals (String team2, int team2Goals){
        try {
            PreparedStatement preparedStatement =
                    this.connection.prepareStatement("INSERT INTO live_games (team2Goals) VALUE (?) WHERE team2=team2");
            preparedStatement.setInt(1, team2Goals);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public boolean usernameAvailable(String username) {
        boolean available = false;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT id " +
                            "FROM users " +
                            "WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                available = false;
            } else {
                available = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return available;
    }

    public UserObject getUserByToken(String token) {
        UserObject user1 = null;
        try {
            PreparedStatement preparedStatement = this.connection
                    .prepareStatement(
                            "SELECT id, username FROM users WHERE token = ?");
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
//
                UserObject user = new UserObject();
                user.setUsername(username);
                user.setToken(token);
                user.setId(id);
                user1 = user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user1;
    }

    public String getUserByCredsH(String username, String token) {
        String response = null;
        List<UserObject> userObjectList = sessionFactory.openSession()
                .createQuery("from UserObject where username =: username and token = : token")
                .setParameter("username", username).setParameter("token", token).list();
        if (userObjectList.size() > 0) {
            UserObject userObject = userObjectList.get(0);
            response = userObject.getToken();
        }
        return response;
    }

    public List<LiveGame> getLiveMatches() {
        return sessionFactory.openSession().createQuery("from LiveGame ").list();
    }

    /*public String getUserByCreds (String username, String token) {
        String response = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND token = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                response = token;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return response;
    }*/
}
