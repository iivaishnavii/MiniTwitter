package edu.sjsu.cmpe275.aop.tweet;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.util.*;

public class TweetStatsServiceImpl implements TweetStatsService {
    /***
     * Following is a dummy implementation.
     * You are expected to provide an actual implementation based on the requirements.
     */

	//public User muUser;
    public int lengthOfLongestTweet = 0;
	String mostFollowedUser=null ;
	String mostProductiveUser = null;
	String mostPopularMessage = null;


	Map<String, Integer> userFolloweMap = new TreeMap<String,Integer>();
	Map<String, ArrayList<String>> userFollowerListMap = new TreeMap<String,ArrayList<String>>();
	Map<String, Integer> userMessageCountMap = new TreeMap<String,Integer>();
	Map<Integer,Integer> messageFollowerCountMap =  new HashMap<Integer, Integer>();
	Map<Integer,String> messageMap = new HashMap<Integer, String>();
	Map<Integer,String> messageUserMap =  new HashMap<Integer, String>();
	Map<String, List<String>> blockedUserList = new HashMap<String, List<String>>();

	//@Override
	public void resetStatsAndSystem() {
		// TODO Auto-generated method stub

		lengthOfLongestTweet =0 ;
		mostFollowedUser = null;
		mostProductiveUser = null;
		mostPopularMessage = null;


		userFolloweMap.clear();
		userMessageCountMap.clear();
		messageFollowerCountMap.clear();
		messageMap.clear();
		messageUserMap.clear();
		blockedUserList.clear();
		
	}

	//@Override
	public int getLengthOfLongestTweet() {

		return lengthOfLongestTweet;
	}

	//@Override
	public String getMostFollowedUser() {

		int max = 0;

		if(userFolloweMap.isEmpty())
			return null;

		for(Map.Entry<String,Integer> entry: userFolloweMap.entrySet())
		{
			if(entry.getValue()>max)
			{
				max = entry.getValue();
				mostFollowedUser = entry.getKey();
			}
		}
		return mostFollowedUser;
	}

	//@Override
	public String getMostPopularMessage() {
		// TODO Auto-generated method stub
		int maxCount = 0;
		//System.out.println(messageFollowerCountMap);

		//equal number of followers case handle
		for(Map.Entry<Integer,Integer> entry: messageFollowerCountMap.entrySet())
		{
//			if(mostPopularMessage == null)
//				mostPopularMessage = messageMap.get(entry.getKey());
			 if(maxCount ==  entry.getValue() && mostPopularMessage != null ) //If follower count is same,return message in lexicographical order
			{
				int value = messageMap.get(entry.getKey()).compareTo(mostPopularMessage);
				if(value < 0) //string 1 is lexicographically first
					mostPopularMessage= messageMap.get(entry.getKey());
			}
			else if(maxCount < entry.getValue() && mostPopularMessage != null)
			{
				mostPopularMessage =  messageMap.get(entry.getKey());
				maxCount = entry.getValue();
			}
			else if(mostPopularMessage == null && entry.getValue()!=0)
			 {
			 	mostPopularMessage = messageMap.get(entry.getKey());
			 	maxCount = entry.getValue();
			 }


		}
//		System.out.println("Max count of Popular Message"+maxCount);
//		if (mostPopularMessage == "")
//			return null;
		return mostPopularMessage;
	}
	
	//@Override
	public String getMostProductiveUser() {
		int max = 0;

		if(userMessageCountMap.isEmpty())
			return null;
		for(Map.Entry<String,Integer> entry: userMessageCountMap.entrySet())
		{
			if(entry.getValue()>max)
			{
				max = entry.getValue();
				mostProductiveUser = entry.getKey();
			}
		}
		// TODO Auto-generated method stub
		//System.out.println("***User Message Count Map"+userMessageCountMap+"***");
		return mostProductiveUser;
	}

	public void checkLongestMessageLength(int tweetLength)
	{
		if(lengthOfLongestTweet < tweetLength)
			lengthOfLongestTweet = tweetLength;

	}

	public void checkMostFollowedUser(String follower,String followee)
	{
		if(!follower.equals(followee)) {
			if (!userFolloweMap.containsKey(followee)) {
				ArrayList<String> followerList = new ArrayList<String>();
				userFolloweMap.put(followee, 1);
				followerList.add(follower);
				userFollowerListMap.put(followee,followerList);
			} else {
				//System.out.println("THe current value is"+userFolloweMap.get(followee));
				userFolloweMap.put(followee, userFolloweMap.get(followee) + 1);
				ArrayList<String> followerList = userFollowerListMap.get(followee);
				followerList.add(follower);
				userFollowerListMap.put(followee,followerList);
			}
		}

	}

	public void totalMessageLength(String user, int messageLength)
	{
			if (!userMessageCountMap.containsKey(user)) {
				userMessageCountMap.put(user, messageLength);
			} else {
				userMessageCountMap.put(user, userMessageCountMap.get(user) +messageLength );
			}

	}

	public void getMessageFollowerCount(Integer messageId,String user)
	{
//		System.out.println("Message Follower count map"+messageFollowerCountMap);
//		System.out.println("User follower map"+userFolloweMap);
		if(messageFollowerCountMap.containsKey(messageId)&& userFolloweMap.containsKey(user))   //The user has followers and the message is tweeted
		{
			//System.out.println("How many messages for messageid "+messageId+" "+ messageFollowerCountMap.get(messageId));
			//System.out.println("User follower for this user is "+userFolloweMap.get(user));
			messageFollowerCountMap.put(messageId,messageFollowerCountMap.get(messageId)+userFolloweMap.get(user));
			//System.out.println("New message follower count "+messageId+" "+messageFollowerCountMap.get(messageId));
		}
		else if (!messageFollowerCountMap.containsKey(messageId)&& !userFolloweMap.containsKey(user)) //The message does not exist and the user has no followers
		{
			messageFollowerCountMap.put(messageId,0);
		}
		//The message exists and the retweted user has no followerers - take no action
		//The message does not exist and the user has followers
		else if (!messageFollowerCountMap.containsKey(messageId) && userFolloweMap.containsKey(user))
		{



			if(blockedUserList.containsKey(user))
			{
				List<String> numberofBlockedUsers =  blockedUserList.get(user);
				messageFollowerCountMap.put(messageId,userFolloweMap.get(user)-numberofBlockedUsers.size());
			}
			else
				messageFollowerCountMap.put(messageId,userFolloweMap.get(user));
		}


	}
	public void getMessageFollowerCountForRetweet(Integer messageId,String user)
	{
		String originalTweeter = messageUserMap.get(messageId); //Get the user who tweeted the original message
		System.out.println("In getMessageFollowerCountForRetweet originalTweeter for messageId "+originalTweeter + " "+messageId);
		System.out.println("Message Map"+messageMap);
		boolean isPresent = false;
		int commonCount =0;
		if(userFollowerListMap.get(user)!=null) //Check if the retweeter has followers
		{
			ArrayList<String> followers = userFollowerListMap.get(user);
			System.out.println("followers of retweeter "+user);
			for(String follower : followers)  //Check if the orignal tweeter is in the followers
			{
				System.out.printf(follower);
				if(follower == originalTweeter)
					isPresent = true;
			}
			System.out.println("Is the origianl tweeter a follower of retweeter "+isPresent);

			if(userFollowerListMap.get(originalTweeter)!=null) //CHeck if they have common followers
			{
				ArrayList<String> orgFollowers = userFollowerListMap.get(originalTweeter);
				for(String follower : followers)
				{
					for(String f:orgFollowers)
					{
						System.out.println("Comparinf "+f+" with "+follower);
						if(f == follower)
							commonCount++;
					}
				}
			}
			System.out.println("Common followers between the two"+commonCount);
		}

		if(messageFollowerCountMap.containsKey(messageId)&& userFolloweMap.containsKey(user))   //The user has followers and the message is tweeted
		{
			//System.out.println("How many messages for messageid "+messageId+" "+ messageFollowerCountMap.get(messageId));
			//System.out.println("User follower for this user is "+userFolloweMap.get(user));

			if(blockedUserList.get(user) == null)
			{
				if(isPresent)
					messageFollowerCountMap.put(messageId,messageFollowerCountMap.get(messageId)+userFolloweMap.get(user)-1-commonCount);
				else
					messageFollowerCountMap.put(messageId,messageFollowerCountMap.get(messageId)+userFolloweMap.get(user)-commonCount);
			}
			else
			{
				if(isPresent)
					messageFollowerCountMap.put(messageId,messageFollowerCountMap.get(messageId)+userFolloweMap.get(user)-1-commonCount-blockedUserList.get(user).size());
				else
					messageFollowerCountMap.put(messageId,messageFollowerCountMap.get(messageId)+userFolloweMap.get(user)-commonCount-blockedUserList.get(user).size());
			}

			//System.out.println("New message follower count "+messageId+" "+messageFollowerCountMap.get(messageId));
		}
		blockedUserList.get("Alex");
	}

	public void createMessageMap(String message,int messageId)
	{
		//creates a message map after every tweet
		messageMap.put(messageId,message);
	}

	public void createMessageUserMap(int messageId,String user){
		messageUserMap.put(messageId,user);
	}

	public void createRetweetMessageMap(int messageId,int retweetMessageId){
		String message = messageMap.get(messageId);
		messageMap.put(retweetMessageId,message);
	}



	public String getUserForMessage(String user,int messageId)
	{
		return messageUserMap.get(messageId);
	}

	public boolean checkIfUserIsBlocked(String newUser,String userWhoTweeted)
	{
		System.out.println(blockedUserList);
		if(!blockedUserList.containsKey(userWhoTweeted))
			return false;
		else
		{
			ArrayList<String> listOfBlockedUser = (ArrayList<String>) blockedUserList.get(userWhoTweeted);
			for(String str : listOfBlockedUser)
			{
				if(str.equals(newUser))
					return true;
			}
			System.out.println("did not find");
			return false;
		}

	}
	public void createBlockedUserList(String user,String blockedUser)
	{
		if(!blockedUserList.containsKey(user))
		{
			ArrayList<String> busers = new ArrayList<String>();
			busers.add(blockedUser);
			blockedUserList.put(user,busers);
		}
		else
		{
			ArrayList<String> busers = (ArrayList<String>) blockedUserList.get(user);
			busers.add(blockedUser);
			blockedUserList.put(user,busers);
		}
	}


}



