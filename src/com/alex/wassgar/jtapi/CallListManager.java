package com.alex.wassgar.jtapi;


import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.Variables.callStatus;

/**
 * Calls used to store static method used to manage call list
 * @author Alexandre
 */
public class CallListManager
	{

	/**
	 * Add call to the list
	 */
	public synchronized static void addCall(Call call)
		{
		Variables.getCallList().add(call);
		Variables.getLogger().debug("JTAPI : Line "+call.getLine().getName()+", new call added to the list : "+call.getCallID());
		Variables.getLogger().debug("Line "+call.getLine().getName()+" Type "+call.getType().name()+" Called "+call.getCalledParty().getAddress().getName()+" Calling "+call.getCallingParty().getAddress().getName());
		}
	
	/**
	 * Used to get a call
	 * @throws Exception 
	 */
	public synchronized static Call getCall(String extension, String callID) throws Exception
		{
		for(Call c : Variables.getCallList())
			{
			if(c.getInfo().equals(extension+callID))return c;
			}
		
		throw new Exception("JTAPI : For the line "+extension+", a call was not found in the list : "+callID);
		}
	
	/**
	 * Used to end a call
	 * 
	 * In this case, we just need the call ID. Indeed, if multiple party are using the same callID
	 * it means that they are all part of the same call. So if the call ends for one, it ends for all
	 */
	public synchronized static void endCall(String extension, String callID)
		{
		for(Call c : Variables.getCallList())
			{
			if((c.getInfo().equals(extension+callID)) && c.getStatus().equals(callStatus.inProgress))
				{
				c.callEnds();
				Variables.getLogger().debug("Line "+c.getLine().getName()+" call "+callID+" ends, duration : "+c.getFormatDuration());
				
				/**
				 * Each time a call ends, we remove it from the list
				 */
				Variables.getCallList().remove(c);
				break;
				}
			}
		}
	
	/**
	 * Will end all calls related to an extension
	 * Used before removing line monitoring
	 */
	public static void endCalls(String extension)
		{
		for(Call c : Variables.getCallList())
			{
			if(c.getUser().getExtension().equals(extension))
				{
				c.callEnds();
				Variables.getLogger().debug("Line "+c.getLine().getName()+" call "+c.getCallID()+" ends, duration : "+c.getFormatDuration());
				Variables.getCallList().remove(c);
				endCalls(extension);//We call it again to clear all the calls
				}
			}
		
		Variables.getLogger().debug("All call cleared for extension : "+extension);
		}
	
	/**
	 * Used to check if the call already exist in the call list
	 */
	public synchronized static boolean isThisCallExisting(Call call)
		{
		for(Call c : Variables.getCallList())
			{
			if(c.getInfo().equals(call.getLine().getName()+" "+call.getCallID()))
				{
				return true;
				}
			}
		
		return false;
		}
	
	
	
	}

/*2019*//*RATEL Alexandre 8)*/