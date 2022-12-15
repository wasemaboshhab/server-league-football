
package com.dev.utils;

import com.dev.objects.Groups;
import com.dev.objects.UserObject;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.List;

@Component
public class Persist {

    private Connection connection;

    private final SessionFactory sessionFactory;

    @Autowired
    public Persist (SessionFactory sf) {
        this.sessionFactory = sf;
    }




    @PostConstruct
    public void createConnectionToDatabase () {

        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/football_project", "root", "1234");
            System.out.println("Successfully connected to DB");
            if (checkIfTableEmpty()) {
                initGroups();
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfTableEmpty() {
        boolean empty = false;
        List<Groups> groups = sessionFactory.openSession()
                .createQuery("from Groups").list();
        if (groups.isEmpty()) {
            empty = true;
        }
        return empty;
    }

    public List<Groups> getAllGroups() {
        return sessionFactory.openSession().createQuery("from Groups ORDER BY points desc ").list();

    }

    public void initGroups() {
        Groups team1 = new Groups("barcelona", 40, 0, 30);
        Groups team2 = new Groups("Real madred", 15, 40, 5);
        Groups team3 = new Groups("city", 33, 8, 22);
        Groups team4 = new Groups("Milan", 28, 11, 17);

        sessionFactory.openSession().save(team1);
        sessionFactory.openSession().save(team2);
        sessionFactory.openSession().save(team3);
        sessionFactory.openSession().save(team4);

    }

    public List<UserObject> getAllUsersH () {

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
        List<UserObject> userObjects =  sessionFactory.openSession()
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

    public void addUser (String username, String token) {
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
    public boolean usernameAvailable (String username) {
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

    public UserObject getUserByToken (String token) {
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
