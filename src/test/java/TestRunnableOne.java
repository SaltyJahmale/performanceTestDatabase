import com.company.Database;
import org.junit.Test;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Bogust on 10-12-2016.
 */
public class TestRunnableOne {

    LocalDate localdate = LocalDate.now();
    Connection connection = Database.connect();
    LocalDateTime dateOfTodayWithTime = LocalDateTime.now();
    LocalDate dateOfToday = LocalDate.now();


    @Test
    public void test1() throws SQLException {

        String insertStudentId = "INSERT INTO student VALUES(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertStudentId);

        preparedStatement.setString(1, "1");
        preparedStatement.executeUpdate();

    }

    @Test
    public void test2() throws SQLException {
        String insertPersona = "INSERT INTO docent VALUES(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertPersona);

        preparedStatement.setString(1, "4");
        preparedStatement.executeUpdate();

    }

    @Test
    public void test3() throws SQLException {

        String insertPersona = "INSERT INTO persona VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertPersona);

        preparedStatement.setInt(1, 4);
        preparedStatement.setInt(2, 4);
        preparedStatement.setString(3, "Rudi");
        preparedStatement.setString(4, "Cools");
        preparedStatement.setString(5, "");
        preparedStatement.setDate(6, Date.valueOf(localdate.minusDays(5)));
        preparedStatement.setString(7, "man");
        preparedStatement.setString(8, "0123456789");
        preparedStatement.setString(9, "Diddenstraat");
        preparedStatement.setInt(10, 11);
        preparedStatement.setString(11, "");
        preparedStatement.setString(12, "6369CR");
        preparedStatement.setString(13, "Rotterdam");

        preparedStatement.executeUpdate();
        connection.close();

    }

    @Test
    public void createClass() throws SQLException {
        int id = 1;

        String insertClass = "INSERT INTO groep VALUES(?, ?, ?)";
        PreparedStatement firstPreparedStatement = connection.prepareStatement(insertClass);
        firstPreparedStatement.setString(1, "INF4C" + id);
        firstPreparedStatement.setDate(2, Date.valueOf(dateOfToday));
        firstPreparedStatement.setDate(3, Date.valueOf(dateOfToday.plusDays(1)));
        firstPreparedStatement.executeUpdate();

    }

    @Test
    public void createCursus() throws SQLException {

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
        secondPreparedStatement.setInt(3, 1);
        secondPreparedStatement.setDate(4, Date.valueOf(dateOfToday));
        secondPreparedStatement.setDate(5, Date.valueOf(dateOfToday.plusDays(1)));
        secondPreparedStatement.executeUpdate();

    }

    @Test
    public void getGroepsnaamFromTable() throws SQLException {

        List<String> listOfGroupNames = new ArrayList();
        Random r = new Random();
        int id = 1;

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

    @Test
    public void test4() throws SQLException {

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
}
