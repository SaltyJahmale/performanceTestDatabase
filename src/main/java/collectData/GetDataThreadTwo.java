package collectData;

import com.company.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by dewi on 12.12.16.
 */
public class GetDataThreadTwo implements Runnable {

    private Thread thread;
    private String threadName;
    Connection connection = Database.connect();

    public GetDataThreadTwo(String name) {
        threadName = name;
        System.out.println("Creating " + threadName);
    }


    public void run() {
        System.out.println("Running " + threadName);

        int startingIterations = 1;
        int maxIterations = 600;
        List<Long> durationThread = new ArrayList();
        long startTime = 0;
        long endTime;
        long duration;

        while (startingIterations <= maxIterations) {

            try {

                String retrieveVoornaamAndAchternaam = "SELECT voornaam, achternaam FROM persona ORDER BY RANDOM() LIMIT 1";
                PreparedStatement firstPreparedStatement = connection.prepareStatement(retrieveVoornaamAndAchternaam);
                ResultSet rsOfVoornaamAndAchternaam = firstPreparedStatement.executeQuery();

                String voornaam = null;
                String achternaam = null;
                while (rsOfVoornaamAndAchternaam.next()) {
                    voornaam = rsOfVoornaamAndAchternaam.getString("voornaam");
                    achternaam = rsOfVoornaamAndAchternaam.getString("achternaam");
                }

                startTime = System.currentTimeMillis();


                String retrieveStudentWithCursus = "SELECT groep_heeft_cursus.cursus_cursuscode \n" +
                        "FROM groep_heeft_cursus, groep, cursus, student_in_groep, student, persona\n" +
                        "WHERE groep.groepsnaam = groep_heeft_cursus.groep_groepsnaam AND\n" +
                        "cursus.cursuscode = groep_heeft_cursus.cursus_cursuscode AND\n" +
                        "student_in_groep.groep_naam = groep.groepsnaam AND\n" +
                        "student.studentnummer = student_in_groep.student_studentnummer AND\n" +
                        "student.studentnummer = persona.studentnummer AND\n" +
                        "persona.voornaam = ? AND\n" +
                        "persona.achternaam = ? ";


                PreparedStatement secondPreparestatement = connection.prepareStatement(retrieveStudentWithCursus);
                secondPreparestatement.setString(1, voornaam);
                secondPreparestatement.setString(2, achternaam);

                ResultSet rsOfStudentWithCursus = secondPreparestatement.executeQuery();

                while (rsOfStudentWithCursus.next()) {
                    String cursus_cursuscode = rsOfStudentWithCursus.getString("cursus_cursuscode");
                    System.out.println(cursus_cursuscode);
                }

                Thread.sleep(120);
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
