package edu.sjsu.cmpe275.aop.tweet.aspect;

import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

import javax.swing.*;

@Aspect
@Order(0)
public class StatsAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

	@Autowired TweetStatsServiceImpl stats;

	@AfterReturning(pointcut = "execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))",
	returning = "messageId"
	)
	public void tweetStatsConcern(JoinPoint joinPoint,Integer messageId)
	{
		Object[] signatureArgs = joinPoint.getArgs();
		String message = signatureArgs[1].toString();
		String user = signatureArgs[0].toString();
		System.out.println("Message"+message);
		int messageLength = signatureArgs[1].toString().length();
		stats.createMessageMap(message,messageId);
		stats.checkLongestMessageLength(messageLength);
		stats.totalMessageLength(user,messageLength);
		stats.createMessageUserMap(messageId,user);
		stats.getMessageFollowerCount(messageId,user);

	}
	@AfterReturning(pointcut = "execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.retweet(..))",
			returning = "retweetMessageId"
	)
	public void retweetStatsConcern(JoinPoint joinPoint,Integer retweetMessageId)
	{
		Object[] signatureArgs = joinPoint.getArgs();
		String user = signatureArgs[0].toString();
		Integer messageId = (Integer)signatureArgs[1];
		System.out.println("User "+user+" retweets the messageid"+ messageId );
		stats.getMessageFollowerCount(messageId,user);


	}


	@AfterReturning(pointcut = "execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
	public void getMostFollowedUser(JoinPoint joinPoint)
	{
		//System.out.println("In the getMostFollowedUser aspect");
		Object[] signatureArgs = joinPoint.getArgs();
		String follower =signatureArgs[0].toString();

		String followee = signatureArgs[1].toString();

		stats.checkMostFollowedUser(follower,followee);

	}

	@AfterReturning(pointcut = "execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.block(..))")
	public void blockerUserList(JoinPoint joinpoint)
	{
		Object[] signatureArgs = joinpoint.getArgs();
		String user = signatureArgs[0].toString();
		String blockedUser = signatureArgs[1].toString();
		stats.createBlockedUserList(user,blockedUser);
	}


//	@AfterReturning(pointcut ="execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*tweet(..))",
//	returning= "messageId")
//	public void getMessageFollowerCount(JoinPoint joinPoint,Integer messageId) {
//
//		Object[] signatureArgs = joinPoint.getArgs();
//		String user = signatureArgs[0].toString();
//		stats.getMessageFollowerCount(messageId,user);
//
//
//		//System.out.printf("After the executuion of the metohd %s\n", joinPoint.getSignature().getName());
//		//stats.resetStats();
//	}
//
//	@Before("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
//	public void dummyBeforeAdvice(JoinPoint joinPoint) {
//		System.out.printf("Before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
//	}
	
}
