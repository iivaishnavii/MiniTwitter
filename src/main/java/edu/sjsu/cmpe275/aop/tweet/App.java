package edu.sjsu.cmpe275.aop.tweet;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        /***
         * Following is a dummy implementation of App to demonstrate bean creation with Application context.
         * You may make changes to suit your need, but this file is NOT part of the submission.
         */

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        TweetService tweeter = (TweetService) ctx.getBean("tweetService");
        TweetStatsService stats = (TweetStatsService) ctx.getBean("tweetStatsService");

        try {
            tweeter.follow("Zhang", "Alex");
            tweeter.follow("Paul", "Alex");
            tweeter.follow("Vaish", "Alex");
            int msg = tweeter.tweet("Alex", "FirstTweet\n");

            tweeter.follow("Zhang", "Bob");
            tweeter.follow("Paul", "Bob");
            tweeter.follow("Paula", "Bob");

            int msg2 = tweeter.tweet("Bob", "AcondTweet");


            //tweeter.retweet("Alex",msg2);
            //tweeter.block("Alex", "Bob");
           // tweeter.retweet("Bob", msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Most productive user: " + stats.getMostProductiveUser());
        System.out.println("Most popular user: " + stats.getMostFollowedUser());
        System.out.println("Length of the longest tweet: " + stats.getLengthOfLongestTweet());
        System.out.println("Most popular message: " + stats.getMostPopularMessage());
        ctx.close();
    }
}
