package edu.sjsu.cmpe275.aop.tweet.aspect;

import edu.sjsu.cmpe275.aop.tweet.TweetService;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;

import java.io.IOException;
import java.lang.reflect.Method;

@Aspect
@Order(1)
public class RetryAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     * @throws Throwable 
     */
    int retryAttemptForTweet = 0;
    int retryAttemptForRetweet = 0;

    @Autowired
    TweetService tweetService;


	@Around("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))")
	public int dummyAdviceOne(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.printf("In Retry aspect , Prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Integer result = null;

		try {
			result = (Integer) joinPoint.proceed();
			System.out.printf("Finished the executuion of the metohd %s with result %s\n", joinPoint.getSignature().getName(), result);
		} catch (IOException e) {
			//e.printStackTrace();
            retryAttemptForTweet ++;
            if(retryAttemptForTweet < 4)
                {
                    Object[] signatureArgs = joinPoint.getArgs();
                    tweetService.tweet(signatureArgs[0].toString(),signatureArgs[1].toString());
                }
            else
			    System.out.printf("Aborted the executuion of the method %s\n", joinPoint.getSignature().getName()+" Retry attempt count"+retryAttemptForTweet);
			throw e;
		}
		catch(IllegalArgumentException iae)
        {
            throw iae;
        }
		return result.intValue();
	}



    @Around("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
    public void dummyAdviceTwo(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.printf("In Retry aspect , Prior to the execution of the method %s\n", joinPoint.getSignature().getName());
       // Integer result = null;

        try {
            joinPoint.proceed();
            //result = (Integer) joinPoint.proceed();
            //System.out.printf("Finished the executuion of the metohd %s with result %s\n", joinPoint.getSignature().getName());
        } catch (IOException e)
        {
            //e.printStackTrace();
            retryAttemptForRetweet ++;
            if(retryAttemptForRetweet < 4)
            {
                Object[] signatureArgs = joinPoint.getArgs();
                tweetService.follow(signatureArgs[0].toString(),signatureArgs[1].toString());
            }
            else
                System.out.printf("Aborted the executuion of the method %s\n", joinPoint.getSignature().getName()+" Retry attempt count"+retryAttemptForRetweet);
            throw e;
        }

    }


    @Around("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.block(..))")
    public void dummyAdviceThree(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.printf("In Retry aspect , Prior to the execution of the method %s \n", joinPoint.getSignature().getName());
        //Integer result = null;
        try {
            joinPoint.proceed();
            //result = (Integer) joinPoint.proceed();
            //System.out.printf("Finished the executuion of the metohd %s with result %s \n", joinPoint.getSignature().getName());
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            retryAttemptForRetweet ++;
            if(retryAttemptForRetweet < 4)
            {
                Object[] signatureArgs = joinPoint.getArgs();
                tweetService.block(signatureArgs[0].toString(),signatureArgs[1].toString());
            }
            else
                System.out.printf("Aborted the executuion of the method %s\n", joinPoint.getSignature().getName()+" Retry attempt count"+retryAttemptForRetweet);
            throw e;
        }

    }



    @Around("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.retweet(..))")
    public int dummyAdviceFour(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.printf("In Retry aspect , Prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());
        Integer result = null;

        try {
            result = (Integer) joinPoint.proceed();
            System.out.printf("Finished the executuion of the metohd %s with result %s\n", joinPoint.getSignature().getName(), result);
        }
        catch(IOException ioe)
        {
            retryAttemptForTweet ++;
            if(retryAttemptForTweet < 4)
            {
                Object[] signatureArgs = joinPoint.getArgs();
                tweetService.retweet(signatureArgs[0].toString(),signatureArgs[1].toString().length());
            }
            else
                System.out.printf("Aborted the executuion of the method %s\n", joinPoint.getSignature().getName()+" Retry attempt count"+retryAttemptForTweet);
            throw ioe;
        }
        catch (Throwable e) {

                throw e;


        }
        return result.intValue();
    }









}
