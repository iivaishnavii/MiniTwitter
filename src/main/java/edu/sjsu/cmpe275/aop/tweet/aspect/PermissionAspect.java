package edu.sjsu.cmpe275.aop.tweet.aspect;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.security.AccessControlException;

@Aspect
@Order(2)
public class PermissionAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */
    @Autowired
    TweetStatsServiceImpl stats;

	@Before("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.retweet(..))")
	public void checkPermissionToRetweet(JoinPoint joinPoint) {
        Object[] signatureArgs = joinPoint.getArgs();
        Integer messageId = (Integer)signatureArgs[1];
        String user = signatureArgs[0].toString();
        System.out.println("User "+user+"tries to retweets "+messageId);

        String userWhoTweeted = stats.getUserForMessage(user,messageId);
        System.out.println("User who tweeted message "+messageId+" is "+userWhoTweeted);
        if(stats.checkIfUserIsBlocked(user,userWhoTweeted))
            throw new AccessControlException("");
        else
            System.out.println("We are continuing");
	}
	
}
