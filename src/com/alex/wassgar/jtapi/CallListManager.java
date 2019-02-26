package com.alex.wassgar.jtapi;

import com.alex.wassgar.misc.ManageUser;
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
		boolean found = false;
		for(Call c : Variables.getCallList())
			{
			if(c.getInfo().equals(call.getLine().getName()+call.getCallID()))
				{
				found = true;
				break;
				}
			}
		
		if(!found)
			{
			Variables.getCallList().add(call);
			Variables.getLogger().debug("JTAPI : Line "+call.getLine().getName()+", new call added to the list : "+call.getCallID());
			Variables.getLogger().debug("Line "+call.getLine().getName()+" Type "+call.getType().name()+" Called "+call.getCalledParty().getAddress().getName()+" Calling "+call.getCallingParty().getAddress().getName());
			}
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
				
				ManageUser.logNewCall(c.getUser(), c);//We log the call in salesforce
				
				/**
				 * Each time a call ends, we remove it from the list
				 */
				Variables.getCallList().remove(c);
				break;
				}
			}
		}
	
	
	
	
	}

/*2019*//*RATEL Alexandre 8)*/