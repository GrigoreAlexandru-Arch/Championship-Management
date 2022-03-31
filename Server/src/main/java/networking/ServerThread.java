package networking;

import database.dao.*;
import database.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ServerThread extends Thread {
    PersonEntity currentUser = null;
    Long currentStageId = 0L;
    ConcurrentHashMap<Long, ObjectOutputStream> outputMap = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public ServerThread(Socket socket, ConcurrentHashMap<Long, ObjectOutputStream> outputMap) {
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.outputMap = outputMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {

                String message = (String) this.in.readObject();
                execute(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isLogged() throws IOException {
        if (currentUser == null) {
            out.writeObject("You are not logged in");
            return false;
        }
        return true;
    }

    private void updateStageId() {
        currentStageId = StageDao.getInstance().getAll().stream().map(StageEntity::getId).max(Comparator.naturalOrder()).get();
    }

    private void login(List<String> parameters) throws IOException {

        if (parameters.size() != 2) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        List<PersonEntity> collect = PersonDao.getInstance().getAll().stream().filter(personEntity ->
                personEntity.getName().equals(parameters.get(1))).collect(Collectors.toList());

        if (collect.size() == 0) {
            out.writeObject("No such account exists");
        } else {
            if (currentUser != null) {
                if (CompetitorDao.getInstance().getAll().stream().anyMatch(competitorEntity -> competitorEntity.getPersonId() == currentUser.getId())) {
                    long previousCompetitorId = CompetitorDao.getInstance().getAll().stream()
                            .filter(competitorEntity -> competitorEntity.getPersonId() == currentUser.getId()).collect(Collectors.toList())
                            .get(0).getId();

                    outputMap.remove(previousCompetitorId);
                }
            }

            currentUser = collect.get(0);

            if (CompetitorDao.getInstance().getAll().stream().anyMatch(competitorEntity -> competitorEntity.getPersonId() == currentUser.getId())) {
                long currentCompetitorId = CompetitorDao.getInstance().getAll().stream()
                        .filter(competitorEntity -> competitorEntity.getPersonId() == currentUser.getId()).collect(Collectors.toList())
                        .get(0).getId();

                outputMap.put(currentCompetitorId, out);
            }

            out.writeObject("Logged in successfully");
        }
    }

    private void register(List<String> parameters) throws IOException {

        if (parameters.size() != 2) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        if (PersonDao.getInstance().getAll().stream().anyMatch(personEntity -> personEntity.getName().equals(parameters.get(1)))) {
            out.writeObject("Name already taken");
        } else {
            PersonEntity person = new PersonEntity();
            person.setName(parameters.get(1));

            PersonDao.getInstance().create(person);
            out.writeObject("Account registered successfully");
        }
    }

    private void joinCompetition(List<String> parameters) throws IOException {
        if (!isLogged())
            return;

        if (parameters.size() != 1) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        if (CompetitorDao.getInstance().getAll().stream().anyMatch(competitorEntity -> competitorEntity.getPersonId() == currentUser.getId())) {
            out.writeObject("You have already joined the competition");
        } else {
            CompetitorEntity competitor = new CompetitorEntity();
            competitor.setPersonId(currentUser.getId());

            CompetitorDao.getInstance().create(competitor);
            out.writeObject("You have joined the competition");
        }
    }

    private void elevatePerson(List<String> parameters) throws IOException {
        if (!isLogged())
            return;

        if (parameters.size() != 2) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        if (PersonDao.getInstance().getAll().stream().noneMatch(personEntity -> personEntity.getName().equals(parameters.get(1)))) {
            out.writeObject("There is no person with that name");
            return;
        }

        PersonEntity person = PersonDao.getInstance().getAll().stream().filter(personEntity -> personEntity.getName().equals(parameters.get(1))).collect(Collectors.toList()).get(0);

        if (AdministratorDao.getInstance().getAll().stream().anyMatch(administratorEntity -> administratorEntity.getPersonId() == person.getId())) {
            out.writeObject("That person is already an administrator");
        } else {
            AdministratorEntity administrator = new AdministratorEntity();
            administrator.setPersonId(person.getId());

            AdministratorDao.getInstance().create(administrator);
            out.writeObject("Person elevated successfully");
        }
    }

    private void joinTeam(List<String> parameters) throws IOException {
        if (!isLogged())
            return;

        if (parameters.size() != 2) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        if (TeamDao.getInstance().getAll().stream().noneMatch(teamEntity -> teamEntity.getName().equals(parameters.get(1)))) {
            out.writeObject("Nonexistent team");
            return;
        }

        if (CompetitorDao.getInstance().getAll().stream().noneMatch(competitorEntity -> competitorEntity.getPersonId() == currentUser.getId())) {
            out.writeObject("You are not a competitor");
            return;
        }

        CompetitorEntity competitor = CompetitorDao.getInstance().getAll().stream().
                filter(competitorEntity -> competitorEntity.getPersonId() == currentUser.getId()).collect(Collectors.toList()).get(0);

        competitor.setTeamId(TeamDao.getInstance().getAll().stream().filter(teamEntity -> teamEntity.getName().equals(parameters.get(1))).collect(Collectors.toList()).get(0).getId());

        CompetitorDao.getInstance().update(competitor);
        out.writeObject("Joined team successfully");
    }

    private void addTeam(List<String> parameters) throws IOException {
        if (!isLogged())
            return;

        if (parameters.size() != 2) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        if (AdministratorDao.getInstance().getAll().stream().noneMatch(administratorEntity -> administratorEntity.getPersonId() == currentUser.getId())) {
            out.writeObject("You do not have permission to execute this command");
            return;
        }

        if (TeamDao.getInstance().getAll().stream().anyMatch(teamEntity -> teamEntity.getName().equals(parameters.get(1)))) {
            out.writeObject("Team name already taken");
            return;
        }

        TeamEntity team = new TeamEntity();

        team.setName(parameters.get(1));
        TeamDao.getInstance().create(team);

        out.writeObject("Team added successfully");
    }

    private void participate(List<String> parameters) throws IOException {
        if (!isLogged())
            return;

        if (parameters.size() != 1) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        if (CompetitorDao.getInstance().getAll().stream().noneMatch(competitorEntity -> competitorEntity.getPersonId() == currentUser.getId())) {
            out.writeObject("You are not a competitor");
            return;
        }

        CompetitorEntity competitor = CompetitorDao.getInstance().getAll().stream().
                filter(competitorEntity -> competitorEntity.getPersonId() == currentUser.getId()).collect(Collectors.toList()).get(0);

        ParticipationEntityPK pk = new ParticipationEntityPK();
        pk.setStageId(currentStageId);
        pk.setCompetitorId(competitor.getId());

        ParticipationEntity participation = ParticipationDao.getInstance().get(pk);


        if (participation != null) {
            out.writeObject("You already participate");
            return;
        }

        participation = new ParticipationEntity();
        participation.setStageId(currentStageId);
        participation.setCompetitorId(competitor.getId());

        ParticipationDao.getInstance().create(participation);

        out.writeObject("You have joined the current stage");
    }

    private void updateScore(List<String> parameters) throws IOException {
        if (!isLogged())
            return;

        if (parameters.size() != 2) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        if (CompetitorDao.getInstance().getAll().stream().noneMatch(competitorEntity -> competitorEntity.getPersonId() == currentUser.getId())) {
            out.writeObject("You are not a competitor");
            return;
        }

        CompetitorEntity competitor = CompetitorDao.getInstance().getAll().stream().
                filter(competitorEntity -> competitorEntity.getPersonId() == currentUser.getId()).collect(Collectors.toList()).get(0);

        ParticipationEntityPK pk = new ParticipationEntityPK();
        pk.setStageId(currentStageId);
        pk.setCompetitorId(competitor.getId());

        ParticipationEntity participation = ParticipationDao.getInstance().get(pk);


        if (participation == null) {
            out.writeObject("You have to participate in the stage to update your score");
            return;
        }

        participation.setPoints(Integer.parseInt(parameters.get(1)));

        ParticipationDao.getInstance().update(participation);

        out.writeObject("Score update successfully");
    }

    private void getCompetitorsLeaderBoard(List<String> parameters) throws IOException {
        if (!isLogged())
            return;

        if (parameters.size() != 1) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        Map<String, Integer> leaderBoard = ParticipationDao.getInstance().getAll().stream()
                .collect(Collectors.toMap(participationEntity -> PersonDao.getInstance().get(CompetitorDao.getInstance().get(participationEntity.getCompetitorId()).getPersonId()).getName(),
                        ParticipationEntity::getPoints, Integer::sum));

        out.writeObject(leaderBoard);
    }

    private void getCompetitorsLeaderBoardByStage(List<String> parameters) throws IOException {
        if (!isLogged())
            return;

        if (parameters.size() != 2) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        if (StageDao.getInstance().getAll().stream().noneMatch(stageEntity -> stageEntity.getName().equals(parameters.get(1)))) {
            out.writeObject("Nonexistent stage");
            return;
        }

        Long stageId = StageDao.getInstance().getAll().stream().filter(stageEntity -> stageEntity.getName().equals(parameters.get(1)))
                .collect(Collectors.toList()).get(0).getId();

        Map<String, Integer> leaderBoard = ParticipationDao.getInstance().getAll().stream()
                .filter(participationEntity -> participationEntity.getStageId() == stageId)
                .collect(Collectors.toMap(participationEntity -> PersonDao.getInstance().get(CompetitorDao.getInstance().get(participationEntity.getCompetitorId()).getPersonId()).getName(),
                        ParticipationEntity::getPoints, Integer::sum));

        out.writeObject(leaderBoard);

    }

    private void getTeamsLeaderBoard(List<String> parameters) throws IOException {
        if (!isLogged())
            return;

        if (parameters.size() != 1) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        Map<String, Integer> leaderBoard = ParticipationDao.getInstance().getAll().stream()
                .filter(participationEntity -> CompetitorDao.getInstance().get(participationEntity.getCompetitorId()).getTeamId() != null)
                .collect(Collectors.toMap(participationEntity -> TeamDao.getInstance().get(CompetitorDao.getInstance().get(participationEntity.getCompetitorId()).getTeamId()).getName(),
                        ParticipationEntity::getPoints, Integer::sum));

        out.writeObject(leaderBoard);
    }

    private void getTeamsLeaderBoardByStage(List<String> parameters) throws IOException {
        if (!isLogged())
            return;

        if (parameters.size() != 2) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        if (StageDao.getInstance().getAll().stream().noneMatch(stageEntity -> stageEntity.getName().equals(parameters.get(1)))) {
            out.writeObject("Nonexistent stage");
            return;
        }

        Long stageId = StageDao.getInstance().getAll().stream().filter(stageEntity -> stageEntity.getName().equals(parameters.get(1)))
                .collect(Collectors.toList()).get(0).getId();

        Map<String, Integer> leaderBoard = ParticipationDao.getInstance().getAll().stream()
                .filter(participationEntity -> CompetitorDao.getInstance().get(participationEntity.getCompetitorId()).getTeamId() != null)
                .filter(participationEntity -> participationEntity.getStageId() == stageId)
                .collect(Collectors.toMap(participationEntity -> TeamDao.getInstance().get(CompetitorDao.getInstance().get(participationEntity.getCompetitorId()).getTeamId()).getName(),
                        ParticipationEntity::getPoints, Integer::sum));

        out.writeObject(leaderBoard);
    }

    private void endStage(List<String> parameters) throws IOException {
        if (!isLogged())
            return;

        if (parameters.size() != 2) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        if (AdministratorDao.getInstance().getAll().stream().noneMatch(administratorEntity -> administratorEntity.getPersonId() == currentUser.getId())) {
            out.writeObject("You do not have permission to execute this command");
            return;
        }

        if (CompetitorDao.getInstance().getAll().stream().anyMatch(competitorEntity -> competitorEntity.getTeamId() == null)) {
            out.writeObject("Not all competitors have joined a team");
            return;
        }

        if (CompetitorDao.getInstance().getAll().stream().collect(Collectors.toMap(CompetitorEntity::getTeamId, competitorEntity -> 1, Integer::sum)).entrySet().stream()
                .anyMatch(longIntegerEntry -> longIntegerEntry.getValue() < 2 || longIntegerEntry.getValue() > 5)) {
            out.writeObject("Stage can not end, there are invalid number of competitors in a team");
            return;
        }

        if (ParticipationDao.getInstance().getAll().stream().anyMatch(participationEntity -> participationEntity.getPoints() == -1)) {
            out.writeObject("Stage can not end, not every competitor has submitted his score");
            return;
        }

        StageEntity stageEntity = new StageEntity();
        stageEntity.setName(parameters.get(1));

        StageDao.getInstance().create(stageEntity);
        out.writeObject("Stage has ended successfully");
    }

    private void sendNotification(List<String> parameters) throws IOException {
        if (!isLogged())
            return;

        if (parameters.size() != 1) {
            out.writeObject("Wrong number of parameters");
            return;
        }

        if (AdministratorDao.getInstance().getAll().stream().noneMatch(administratorEntity -> administratorEntity.getPersonId() == currentUser.getId())) {
            out.writeObject("You do not have permission to execute this command");
            return;
        }

        List<Long> receiversList = ParticipationDao.getInstance().getAll().stream()
                .filter(participationEntity -> participationEntity.getPoints() == -1)
                .map(ParticipationEntity::getCompetitorId).collect(Collectors.toList());

        receiversList.forEach(aLong -> {
            try {
                if (outputMap.containsKey(aLong)) {
                    outputMap.get(aLong).writeObject("Please insert your score.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        out.writeObject("Notifications sent");
    }

    private void execute(String message) throws IOException {
        List<String> parameters = List.of(message.split(" "));

        updateStageId();
        switch (parameters.get(0)) {
            case "login":
                login(parameters);
                break;

            case "register":
                register(parameters);
                break;
            case "joinCompetition":
                joinCompetition(parameters);
                break;

            case "joinTeam":
                joinTeam(parameters);
                break;

            case "addTeam":
                addTeam(parameters);
                break;

            case "participate":
                participate(parameters);
                break;

            case "updateScore":
                updateScore(parameters);
                break;

            case "getCompetitorsLeaderBoard":
                getCompetitorsLeaderBoard(parameters);
                break;

            case "getCompetitorsLeaderBoardByStage":
                getCompetitorsLeaderBoardByStage(parameters);
                break;

            case "getTeamsLeaderBoard":
                getTeamsLeaderBoard(parameters);
                break;

            case "getTeamsLeaderBoardByStage":
                getTeamsLeaderBoardByStage(parameters);
                break;

            case "endStage":
                endStage(parameters);
                break;

            case "elevatePerson":
                elevatePerson(parameters);
                break;

            case "sendNotifications":
                sendNotification(parameters);
                break;

            default:
                out.writeObject("Unknown command");
        }
    }
}