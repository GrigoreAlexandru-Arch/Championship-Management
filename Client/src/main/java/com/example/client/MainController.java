package com.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class MainController {
    public Button login;
    public Button register;
    public TextField usernameInput;
    public Button elevatePerson;
    public Button joinCompetition;
    public Button participate;
    public Button updateScore;
    public TextField scoreInput;
    public Button joinTeam;
    public Button addTeam;
    public Button getCompetitorsLeaderBoard;
    public Button getTeamsLeaderBoard;
    public Button sendNotifications;
    public Button getCompetitorsLeaderBoardByStage;
    public Button getTeamsLeaderBoardByStage;
    public Button endStage;
    public TextField stageNameInput;
    public TextField teamNameInput;
    public TextArea responseArea;

    @FXML
    protected void loginOnClick() {
        try {
            Application.outputStream.writeObject("login " + usernameInput.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerOnClick() {
        try {
            Application.outputStream.writeObject("register " + usernameInput.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void participateOnClick() {
        try {
            Application.outputStream.writeObject("participate");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateScoreOnClick() {
        try {
            Application.outputStream.writeObject("updateScore " + scoreInput.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void joinTeamOnClick() {
        try {
            Application.outputStream.writeObject("joinTeam " + teamNameInput.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTeamOnClick() {
        try {
            Application.outputStream.writeObject("addTeam " + teamNameInput.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getCompetitorsLeaderBoardOnClick() {
        try {
            Application.outputStream.writeObject("getCompetitorsLeaderBoard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getTeamsLeaderBoardOnClick() {
        try {
            Application.outputStream.writeObject("getTeamsLeaderBoard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getCompetitorsLeaderBoardByStageOnClick() {
        try {
            Application.outputStream.writeObject("getCompetitorsLeaderBoardByStage " + stageNameInput.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendNotificationsOnClick() {
        try {
            Application.outputStream.writeObject("sendNotifications");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getTeamsLeaderBoardByStageOnClick() {
        try {
            Application.outputStream.writeObject("getTeamsLeaderBoardByStage " + stageNameInput.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endStageOnClick() {
        try {
            Application.outputStream.writeObject("endStage " + stageNameInput.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void joinCompetitionOnClick() {
        try {
            Application.outputStream.writeObject("joinCompetition");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void elevatePersonOnClick() {
        try {
            Application.outputStream.writeObject("elevatePerson " + usernameInput.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setResponse(String text) {
        long lineCount = responseArea.getText().split("\n").length;

        if (lineCount >= 12L || responseArea.getText().length()==0) {
            responseArea.setText(text);
        } else {
            responseArea.setText(responseArea.getText() + '\n' + text);
        }
    }
}