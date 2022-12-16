
package com.dev.utils;

import com.dev.objects.Group;
import com.dev.objects.UserObject;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfTableEmpty() {
        boolean empty = false;
        List<Group> groups = sessionFactory.openSession()
                .createQuery("from Group").list();
        if (groups.isEmpty()) {
            empty = true;
        }
        return empty;
    }

    public List<Group> getAllGroups() {
        return sessionFactory.openSession().createQuery("from Group ORDER BY points desc ").list();

    }

    public void initGroups() {

        Group[] groupList={new Group("Maccabi-Ashdod", 6, 20, 3, 10, 2),
                new Group("Hapoel-Afula", 2, 12, 4, 4, 7),
                new Group("Shaaraiim", 12, 8, 3, 3, 9),
                new Group("Bnai-Reina", 4, 2, 5, 9, 1),
                new Group("Kiryat-Gat", 6, 13, 3, 10, 2),
                new Group("Arayot-Rahat", 5, 14, 5, 8, 2),
                new Group("Bnai-Ashkelon", 7, 10, 8, 5, 2),
                new Group("Netivot", 2, 20, 0, 11, 4),
                new Group("Leviot-Yeruham", 4, 40, 7, 4, 4),
                new Group("Totahi-Ramle", 2, 15, 5, 5, 5),
                new Group("Hapoel-Natanya", 33, 33, 1, 0, 14),
                new Group("Milan", 28, 7, 10, 3, 2)};


        for (Group group:groupList) {
            group.setRatioOfGoals();
            group.setPoints();
            sessionFactory.openSession().save(group);
        }
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
