package com.dev.controllers;

import com.dev.objects.Group;
import com.dev.objects.LiveGame;
import com.dev.objects.UserObject;
import com.dev.responses.BasicResponse;
import com.dev.responses.SignInReponse;
import com.dev.utils.Persist;
import com.dev.utils.Utils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
//add to git


@RestController
public class TestController {

    //  private List<UserObject> myUsers = new ArrayList<>();

    @Autowired
    public Utils utils;


    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String getCheck() {
        return "Success from get request";
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public String postCheck() {
        return "Success from post request";
    }

  /*  @RequestMapping(value = "/get-goals", method = RequestMethod.POST)
    public LiveGame postCheck(String team1, String team2, int team1Goals, int team2Goals) {
        LiveGame liveGame = new LiveGame(team1, team2, team1Goals, team2Goals);
        return liveGame;
    }*/

    @RequestMapping(value = "/get-static-table", method = RequestMethod.GET)
    public List<Group> getStaticTable() {
        return persist.getAllGroups();
    }

    @RequestMapping(value = "/get-names-groups", method = RequestMethod.GET)
    public List<String> getGroupsNames() {
        return persist.getAllGroupsName();
    }
    @RequestMapping(value = "/get-all-users", method = {RequestMethod.GET, RequestMethod.POST})

    public List<UserObject> getAllUsers() {
        List<UserObject> allUsers = persist.getAllUsersH();
        return allUsers;
    }

    @RequestMapping(value = "/add-goals-team1", method = RequestMethod.POST)
    public int addGoalsTeam1(String team1, int goals){
        persist.addTeam1Goals(team1,goals);
        return goals;
    }

    @RequestMapping(value = "/add-goals-team2", method = RequestMethod.POST)
    public int addGoalsTeam2(String team2, int goals){
        persist.addTeam2Goals(team2,goals);
        return goals;
    }

    @RequestMapping(value = "/save-match", method = RequestMethod.POST)
    public LiveGame saveMatch(String team1, String team2){
        LiveGame liveGame = new LiveGame(team1,team2);
        persist.addLiveGame(team1,team2,0,0);
        return liveGame;
    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public BasicResponse signIn(String username, String password) {
        BasicResponse basicResponse = null;
        String token = createHash(username, password);
        token = persist.getUserByCredsH(username, token);
        if (token == null) {
            if (persist.usernameAvailable(username)) {
                basicResponse = new BasicResponse(false, 1);
            } else {
                basicResponse = new BasicResponse(false, 2);
            }
        } else {
            UserObject user = persist.getUserByToken(token);
            basicResponse = new SignInReponse(true, null, user);
        }
        return basicResponse;
    }


   @RequestMapping(value = "/create-account", method = {RequestMethod.GET, RequestMethod.POST})
    public UserObject createAccount(String username, String password) {
        UserObject newAccount = null;
        if (utils.validateUsername(username)) {
            if (utils.validatePassword(password)) {
                if (persist.usernameAvailable(username)) {
                    String token = createHash(username, password);
                    newAccount = new UserObject(username, token);
                    persist.addUser(username, token);
                } else {
                    System.out.println("username already exits");
                }
            } else {
                System.out.println("password is invalid");
            }
        } else {
            System.out.println("username is invalid");
        }
        return newAccount;
    }


    public String createHash(String username, String password) {
        String raw = String.format("%s_%s", username, password);
        String myHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(raw.getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return myHash;
    }






}
