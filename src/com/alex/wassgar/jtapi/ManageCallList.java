package com.alex.wassgar.jtapi;

import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.Variables.callStatus;

/**
 * Calls used to store static method used to manage call list
 * @author Alexandre
 */
public class ManageCallList
	{

	/**
	 * Add call to the list
	 */
	public static void addCall(Call call)
		{
		boolean found = false;
		for(Call c : Variables.getCallList())
			{
			if(c.getCallID().equals(call.getCallID()))
				{
				found = true;
				break;
				}
			}
		
		if(!found)
			{
			Variables.getCallList().add(call);
			Variables.getLogger().debug("JTAPI : New call added to the list : "+call.getCallID());
			Variables.getLogger().debug("Line "+call.getLine().getName()+" Type "+call.getType().name()+" Called "+call.getCalledParty().getAddress().getName()+" Calling "+call.getCallingParty().getAddress().getName());
			}
		}
	
	/**
	 * Used to get a call
	 * @throws Exception 
	 */
	public static Call getCall(String callID) throws Exception
		{
		for(Call c : Variables.getCallList())
			{
			if(c.getCallID().equals(callID))return c;
			}
		
		throw new Exception("JTAPI : Call not found in the list : "+callID);
		}
	
	/**
	 * Used to end a call
	 */
	public static void endCall(String callID)
		{
		for(Call c : Variables.getCallList())
			{
			if((c.getCallID().equals(callID)) && c.getStatus().equals(callStatus.inProgress))
				{
				c.callEnds();
				Variables.getLogger().debug("Line "+c.getLine().getName()+" call "+callID+" ends, duration : "+c.getFormatDuration());
				}
			}
		}
	
	
	
	
	}

/*2019*//*RATEL Alexandre 8)*/