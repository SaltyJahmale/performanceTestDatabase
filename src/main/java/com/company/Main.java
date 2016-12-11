package com.company;

public class Main {

    public static void main(String[] args) {
        RunnableOne runnableOne = new RunnableOne("FirstUser");
        runnableOne.start();

        RunnableTwo runnableTwo = new RunnableTwo("SecondUser");
        runnableTwo.start();

        RunnableThree runnableThree = new RunnableThree("ThirdUser");
        runnableThree.start();

    }
}
