package com.alex.wassgar.misc;

import com.alex.wassgar.jtapi.Call;
import com.alex.wassgar.salesforce.SFObject;
import com.alex.wassgar.salesforce.SalesForceManager;
import com.alex.wassgar.utils.UsefulMethod;
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
			/**
			 * To do when an incoming call is in progress
			 */
			try
				{
				//Look for a SalesForce contact
				SFObject sfo = SalesForceManager.lookForExtension(user.getSalesforceID(), call.getCallingParty().getAddress().getName());
				
				if(sfo != null)
					{
					//If found we display an alerting name on the phone
					ManageUser.displayAlertingName(user, UsefulMethod.getAlertingNameFromSFObject(sfo), call);
					
					//Display the contact in salesforce
					SalesForceManager.displaySFToast(user.getSalesforceID(), sfo);
					
					//We log the call in salesforce
					SalesForceManager.logNewSFCall(user.getSalesforceID(), call);
					}
				else
					{
					//If not found we propose to create a new entry in salesforce
					SalesForceManager.createNewEntry(user.getSalesforceID(), call.getLine().getName());
					}
				}
			catch (Exception e)
				{
				Variables.getLogger().error("ERROR : While processing a new incoming call : "+e.getMessage(),e);
				}
			}
		else
			{
			/**
			 * To do when an outgoing call is in progress
			 */
			try
				{
				//Look for a SalesForce contact
				SFObject sfo = SalesForceManager.lookForExtension(user.getSalesforceID(), call.getCalledParty().getAddress().getName());
				
				if(sfo != null)
					{
					//If found we display an alerting name on the phone
					ManageUser.displayAlertingName(user, UsefulMethod.getAlertingNameFromSFObject(sfo), call);
					
					//We log the call in salesforce
					SalesForceManager.logNewSFCall(user.getSalesforceID(), call);
					}
				else
					{
					//If not found we propose to create a new entry in salesforce
					SalesForceManager.createNewEntry(user.getSalesforceID(), call.getLine().getName());
					}
				}
			catch (Exception e)
				{
				Variables.getLogger().error("ERROR : While processing a new outgoing call : "+e.getMessage(),e);
				}
			}
		}
	
	
	/**
	 * Used to display the alerting name of an in progress call
	 */
	public static void displayAlertingName(User user, String AlertingName, Call call)
		{
		//To be written
		}
	
	
	
	
	
	

	}

/*2019*//*RATEL Alexandre 8)*/