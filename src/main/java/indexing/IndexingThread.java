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
public class IndexingThread implements Runnable {

    private Thread thread;
    private String threadName;
    Connection connection = Database.connect();

    public IndexingThread(String name) {
        threadName = name;
        System.out.println("Creating " + threadName);
    }

    /**
     * Indexing voor achternaam en voornaam
     * Voor unique indexes zou ik emails en telefoonnummers kiezen omdat die ook meestal unique moeten zijn
     */


    public void run() {
        System.out.println("Running " + threadName);

        int startingIterations = 1;
        int maxIterations = 2;

        long startTimeIndex;
        long endTime;
        long duration = 0;


        while (startingIterations <= maxIterations) {
            startTimeIndex = 0;

            try {

                startTimeIndex = System.currentTimeMillis();

//                String createIndex = " CREATE UNIQUE INDEX voornaam_index ON persona (voornaam, achternaam, telefoonnummer);";
//                PreparedStatement indexPreparestatement = connection.prepareStatement(createIndex);
//                indexPreparestatement.executeQuery();

                String retrieveVoornaamAndAchternaam = "EXPLAIN SELECT voornaam, achternaam, telefoonnummer FROM persona WHERE voornaam LIKE 'a%' ";
                PreparedStatement secondPreparedStatement = connection.prepareStatement(retrieveVoornaamAndAchternaam);
                secondPreparedStatement.executeQuery();

                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }

            startingIterations += 1;
            endTime = System.currentTimeMillis();
            duration = endTime - startTimeIndex;
        }

        System.out.println(threadName + " took this much miliseconds " + duration);

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
