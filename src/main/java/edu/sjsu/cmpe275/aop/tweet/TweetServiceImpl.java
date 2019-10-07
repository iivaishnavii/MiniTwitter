package edu.sjsu.cmpe275.aop.tweet;

import java.io.IOException;
import java.security.AccessControlException;

import org.springframework.beans.factory.annotation.Autowired;

public class TweetServiceImpl implements TweetService {

    /***
     * Following is a dummy implementation.
     * You can tweak the implementation to suit your need, but this file is NOT part of the submission.
     */
    static int count = 0;



	//@Override
    public int tweet(String user, String message) throws IllegalArgumentException, IOException {
    	System.out.printf("User %s tweeted message: %s\n", user, message);
//    	if(count ==0) {
//			count++;
//			throw new IOException("IOException");
//		}

    	return ++count;
    }

	//@Override
    public void follow(String follower, String followee) throws IOException {

       	//System.out.printf("User %s followed user %s \n", follower, followee);
    }

	//@Override
	public void block(String user, String follower) throws IOException {
       	//System.out.printf("User %s blocked user %s \n", user, follower);
	}

	//@Override
	public int retweet(String user, int messageId)
			throws AccessControlException, IllegalArgumentException, IOException {
		// TODO Auto-generated method stub
//		if(count < 10)
//			throw new IOException("Yellow");
		return ++count;
	}

}
