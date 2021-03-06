package indexing;

import com.company.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by dewi on 13.12.16.
 */
public class NoIndexingThread implements Runnable {

    private Thread thread;
    private String threadName;
    Connection connection = Database.connect();

    public NoIndexingThread(String name) {
        threadName = name;
        System.out.println("Creating " + threadName);
    }


    public void run() {
        System.out.println("Running " + threadName);

        int startingIterations = 1;
        int maxIterations = 2;

        long startTimeNoIndex;
        long endTime;
        long duration = 0;


        while (startingIterations <= maxIterations) {
            startTimeNoIndex = 0;

            try {

                startTimeNoIndex = System.currentTimeMillis();

                String disableIndex = "set enable_seqscan = true;";
                PreparedStatement indexPreparedStatement = connection.prepareStatement(disableIndex);
                indexPreparedStatement.executeQuery();

                String retrieveVoornaamAndAchternaam = "SELECT voornaam, achternaam, telefoonnummer FROM persona WHERE voornaam LIKE 'a%' ";
                PreparedStatement secondPreparedStatement = connection.prepareStatement(retrieveVoornaamAndAchternaam);
                secondPreparedStatement.executeQuery();

                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }

            startingIterations += 1;
            endTime = System.currentTimeMillis();
            duration = endTime - startTimeNoIndex;
        }

       

        System.out.println(threadName + " took this much seconds " + duration);

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