package com.alex.wassgar.misc;

import com.alex.wassgar.jtapi.Call;
import com.alex.wassgar.salesforce.SalesForceManager;
import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.Variables.callType;

/**
 * Class used to store static method about user useful features
 * @author Alexandre
 */
public class ManageUser
	{
	
	/**
	 * Used to process new calls
	 */
	public static void processNewCall(User user, Call call)
		{
		if(call.getType().equals(callType.incoming))
			{
			
			}
		else
			{
			
			}
		}
	
	/**
	 * Used to log a new call for a given user
	 */
	public static void logNewCall(User user, Call call)
		{
		if(call.getRelatedSFObject() != null)//we check if the call is about a known contact
			{
			Variables.getLogger().debug("Logging new call for "+user.getInfo()+" : "+call.getInfo());
			SalesForceManager.logNewSFCall(user.getSalesforceID(), call);
			}
		else
			{
			Variables.getLogger().debug("This call was not related to a SF contact so we do not log it : "+call.getInfo());
			}
		}
	
	
	/**
	 * Used to display the alerting name of an in progress call
	 */
	public static void displayAlertingName(User user, String AlertingName)
		{
		//To be written
		}
	
	
	
	
	
	

	}

/*2019*//*RATEL Alexandre 8)*/