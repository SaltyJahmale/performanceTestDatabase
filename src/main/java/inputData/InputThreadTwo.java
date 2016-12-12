package inputData;

import com.company.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bogust on 11-12-2016.
 */
public class InputThreadTwo implements Runnable {
    private Thread thread;
    private String threadName;
    LocalDate dateOfToday = LocalDate.now();

    Connection connection = Database.connect();

    public InputThreadTwo(String name) {
        threadName = name;
        System.out.println("Creating " + threadName);
    }

    public void createStudent(int id) throws SQLException {
        String insertStudent = "INSERT INTO student VALUES(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertStudent);

        preparedStatement.setString(1, String.valueOf(id));
        preparedStatement.execute();

    }

    public void createDocent(int id) throws SQLException {

        String insertDocent = "INSERT INTO docent VALUES(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertDocent);

        preparedStatement.setString(1, String.valueOf(id));
        preparedStatement.execute();

    }

    public void createClass(int id) throws SQLException {

        String insertClass = "INSERT INTO groep VALUES(?, ?, ?)";
        PreparedStatement firstPreparedStatement = connection.prepareStatement(insertClass);
        firstPreparedStatement.setString(1, "INF4C" + id);
        firstPreparedStatement.setDate(2, Date.valueOf(dateOfToday));
        firstPreparedStatement.setDate(3, Date.valueOf(dateOfToday.plusDays(1)));
        firstPreparedStatement.executeUpdate();

    }

    public void createCursus(int id) throws SQLException {

        Random randomNumber = new Random();
        char[] chars = "INFANL".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            char c = chars[randomNumber.nextInt(chars.length)];
            stringBuilder.append(c);
        }

        String insertModule = "INSERT INTO cursus VALUES(?, ?, ?, ?, ?)";
        PreparedStatement secondPreparedStatement = connection.prepareStatement(insertModule);
        secondPreparedStatement.setString(1, String.valueOf(stringBuilder));
        secondPreparedStatement.setString(2, "Vak Advance database");
        secondPreparedStatement.setInt(3, id);
        secondPreparedStatement.setDate(4, Date.valueOf(dateOfToday));
        secondPreparedStatement.setDate(5, Date.valueOf(dateOfToday.plusDays(1)));
        secondPreparedStatement.executeUpdate();

    }

    public void createPersona(int studentId, int teacherId) throws SQLException {

        Random randomNumber = new Random();
        char[] chars = "Martin".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            char c = chars[randomNumber.nextInt(chars.length)];
            stringBuilder.append(c);
        }

        String insertPersona = "INSERT INTO persona VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertPersona);

        preparedStatement.setInt(1, studentId);
        preparedStatement.setInt(2, teacherId);
        preparedStatement.setString(3, String.valueOf(stringBuilder));
        preparedStatement.setString(4, "Cools");
        preparedStatement.setString(5, "");
        preparedStatement.setDate(6, Date.valueOf(dateOfToday.minusDays(5)));
        preparedStatement.setString(7, "man");
        preparedStatement.setString(8, "0123456789");
        preparedStatement.setString(9, "Diddenstraat");
        preparedStatement.setInt(10, 11);
        preparedStatement.setString(11, "");
        preparedStatement.setString(12, "6369CR");
        preparedStatement.setString(13, "Rotterdam");
        preparedStatement.executeUpdate();

    }

    public void pairStudentToClass(int id) throws SQLException {

        List<String> listOfGroupNames = new ArrayList();
        Random r = new Random();

        String retrieveGroepsnaam = "SELECT groepsnaam FROM groep ";
        PreparedStatement firstPreparedStatement = connection.prepareStatement(retrieveGroepsnaam);
        ResultSet rsOfGroepsnaam = firstPreparedStatement.executeQuery();

        while(rsOfGroepsnaam.next()) {
            String groepsnaam = rsOfGroepsnaam.getString("groepsnaam");
            listOfGroupNames.add(groepsnaam);
        }

        String insertStudentInGroup = "INSERT INTO student_in_groep VALUES(?, ?)";
        PreparedStatement thirdPreparedStatement = connection.prepareStatement(insertStudentInGroup);

        if(!listOfGroupNames.isEmpty()) {
            thirdPreparedStatement.setString(1, listOfGroupNames.get(r.nextInt(listOfGroupNames.size())));
            thirdPreparedStatement.setString(2, String.valueOf(id));
            thirdPreparedStatement.executeUpdate();
        }
    }

    public void pairClassToCursus() throws SQLException {

        List<String> listOfGroupNames = new ArrayList();
        List<String> listOfCususen = new ArrayList();
        Random r = new Random();

        String retrieveGroepsnaam = "SELECT groepsnaam FROM groep ";
        PreparedStatement firstPreparedStatement = connection.prepareStatement(retrieveGroepsnaam);
        ResultSet rsOfGroepsnaam = firstPreparedStatement.executeQuery();

        while(rsOfGroepsnaam.next()) {
            String groepsnaam = rsOfGroepsnaam.getString("groepsnaam");
            listOfGroupNames.add(groepsnaam);
        }


        String retrieveCursussen = "SELECT cursuscode FROM cursus ";
        PreparedStatement secondPreparedStatement = connection.prepareStatement(retrieveCursussen);
        ResultSet rsOfCursussen = secondPreparedStatement.executeQuery();

        while(rsOfCursussen.next()) {
            String cursuscode = rsOfCursussen.getString("cursuscode");
            listOfCususen.add(cursuscode);

        }

        String insertGroupWithCursus = "INSERT INTO groep_heeft_cursus VALUES(?, ?)";
        PreparedStatement thirdPreparedStatement = connection.prepareStatement(insertGroupWithCursus);

        if(!listOfGroupNames.isEmpty() && !listOfCususen.isEmpty()) {
            thirdPreparedStatement.setString(1, listOfCususen.get(r.nextInt(listOfCususen.size())));
            thirdPreparedStatement.setString(2, listOfGroupNames.get(r.nextInt(listOfGroupNames.size())));
            thirdPreparedStatement.executeUpdate();
        }
    }

    public void run() {
        System.out.println("Running " + threadName);

        int startingIterations = 601;
        int maxIterations = 1200;
        List<Long> durationThread = new ArrayList();
        long startTime;
        long endTime;
        long duration;

        while (startingIterations <= maxIterations) {
            startTime = System.currentTimeMillis();

            try {
                createStudent(startingIterations);
                createDocent(startingIterations);
                createPersona(startingIterations, startingIterations);
                pairStudentToClass(startingIterations);

                boolean chanceToCreateAClass = new Random().nextInt(30) == 0;
                if(chanceToCreateAClass == true) {
                    createClass(startingIterations);
                }

                boolean chanceToCreateACursus = new Random().nextInt(30) == 15;
                if(chanceToCreateACursus == true) {
                    createCursus(startingIterations);
                }

                boolean chanceToPairAClassToCursus = new Random().nextInt(7) == 3;
                if(chanceToPairAClassToCursus == true) {
                    pairClassToCursus();
                }

                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }

            startingIterations += 1;
            endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            durationThread.add(duration);
        }

        long totalTime = 0;
        for (long totalDuration : durationThread) {
            totalTime += totalDuration;
        }

        long totalConvertToSeconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);
        System.out.println(threadName + " took this much seconds " + totalConvertToSeconds);

        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Thread " + threadName + " exiting");
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }

}
