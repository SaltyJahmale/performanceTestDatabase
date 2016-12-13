package com.company;

import collectData.GetDataThreadOne;
import collectData.GetDataThreadTwo;
import indexing.IndexingThread;
import indexing.NoIndexingThread;
import inputData.InputThreadOne;
import inputData.InputThreadThree;
import inputData.InputThreadTwo;

public class Main {

    public static void main(String[] args) {


        /**
         * Insert random data
         */

        InputThreadOne runnableOne = new InputThreadOne("FirstUser");
        runnableOne.start();

        InputThreadTwo runnableTwo = new InputThreadTwo("SecondUser");
        runnableTwo.start();

        InputThreadThree runnableThree = new InputThreadThree("ThirdUser");
        runnableThree.start();


        /**
         * Get the data from a random student with all his courses
         */

        GetDataThreadOne getDataThread = new GetDataThreadOne("First get thread");
        getDataThread.start();

        GetDataThreadTwo getDataThreadTwo = new GetDataThreadTwo("Second get thread");
        getDataThreadTwo.start();


        /**
         * Indexing queries
         */

        IndexingThread indexingThread = new IndexingThread("Indexing query");
        indexingThread.start();

        NoIndexingThread noIndexingThread = new NoIndexingThread("No indexing query");
        noIndexingThread.start();


    }
}
