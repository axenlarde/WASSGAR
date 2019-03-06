package com.alex.wassgar.misc;

import com.alex.wassgar.jtapi.Call;
import com.alex.wassgar.salesforce.SFObject;
import com.alex.wassgar.salesforce.SalesForceManager;
import com.alex.wassgar.server.ManageRequest;
import com.alex.wassgar.server.Request;
import com.alex.wassgar.server.RequestBuilder;
import com.alex.wassgar.utils.LanguageManagement;
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
					String alertingName = UsefulMethod.getAlertingNameFromSFObject(sfo);
					ManageUser.displayAlertingName(user, alertingName, call);
					
					//Fire notification
					ManageUser.fireNotification(user, sfo);
					
					//We log the call in salesforce
					String taskID = SalesForceManager.logNewSFCall(user.getSalesforceID(), call, sfo);
					
					//We ask the user to add a comment
					ManageUser.sendCommentRequest(user, call, sfo, taskID);
					}
				else
					{
					//If not found we propose to create a new entry in salesforce
					if(UsefulMethod.shouldIProposeNewEntry(callType.incoming))ManageUser.proposeNewEntry(user, call.getCallingParty().getAddress().getName());
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
					String alertingName = UsefulMethod.getAlertingNameFromSFObject(sfo);
					ManageUser.displayAlertingName(user, alertingName, call);
					
					//We log the call in salesforce
					SalesForceManager.logNewSFCall(user.getSalesforceID(), call, sfo);
					}
				else
					{
					//If not found we propose to create a new entry in salesforce
					if(UsefulMethod.shouldIProposeNewEntry(callType.incoming))ManageUser.proposeNewEntry(user, call.getCalledParty().getAddress().getName());
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
		try
			{
			//We do it only if activated for this particular user
			if(user.isReverseLookup())
				{
				//To be written
				}
			}
		catch(Exception e)
			{
			Variables.getLogger().error("ERROR : While displaying alerting name on the phone : "+e.getMessage(),e);
			}
		}
	
	/**
	 * Send a comment request to the user
	 * @throws Exception 
	 */
	public static void sendCommentRequest(User user, Call call, SFObject sfo, String objectID)
		{
		try
			{
			//We do it only if activated for this particular user
			if(user.isEmailReminder())
				{
				String alertingName = sfo.getInfo();
				
				/**
				 * Here we just send an email at the moment
				 * 
				 * We could change that later
				 */
				StringBuffer eMailContent = new StringBuffer();
				eMailContent.append(LanguageManagement.getString("emailhello"));
				eMailContent.append("\r\n");
				eMailContent.append(LanguageManagement.getString("emailcommentcontent"));
				eMailContent.append("\r\n");
				eMailContent.append(LanguageManagement.getString("incomingcall"));
				eMailContent.append(LanguageManagement.getString("calldescriptionincoming"));
				eMailContent.append(" "+alertingName);
				eMailContent.append(" "+call.getFormatStartTime());
				eMailContent.append("\r\n");
				eMailContent.append(UsefulMethod.getFormattedURL(UsefulMethod.getTargetOption("sfcommenturlpattern"),objectID));
				eMailContent.append("\r\n");
				eMailContent.append("\r\n");
				eMailContent.append(LanguageManagement.getString("emailsignature"));
				
				Variables.geteMSender().send(user.getEmail(),
						LanguageManagement.getString("emailcommentobject"),
						eMailContent.toString(),
						"Email sent for user "+user.getInfo()+" call from "+alertingName);
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : While sending a comment request : "+e.getMessage(),e);
			}
		}
	
	/**
	 * Method used to propose to the user to create a new entry
	 * @throws Exception 
	 */
	public static void proposeNewEntry(User user, String extension)
		{
		/**
		 * At the moment we just send an email to remind to create a new entry
		 */
		try
			{
			StringBuffer eMailContent = new StringBuffer();
			eMailContent.append(LanguageManagement.getString("emailhello"));
			eMailContent.append("\r\n");
			eMailContent.append("\r\n");
			eMailContent.append(LanguageManagement.getString("emailnewentrycontent"));
			eMailContent.append(" : "+extension);
			eMailContent.append("\r\n");
			eMailContent.append("\r\n");
			eMailContent.append(LanguageManagement.getString("emailsignature"));
			
			Variables.geteMSender().send(user.getEmail(),
					LanguageManagement.getString("emailnewentryobject"),
					eMailContent.toString(),
					"Email sent for user "+user.getInfo()+" propose to create a new entry for extension : "+extension);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : While proposing a new entry : "+e.getMessage(),e);
			}
		}
	
	/**
	 * Used to fire notification
	 */
	public static void fireNotification(User user, SFObject sfo)
		{
		try
			{
			/**
			 * At the moment we just open a new tab to display the notification
			 */
			//We do it only if activated for this particular user
			if(user.isIncomingCallPopup())
				{
				String urlToDisplay = UsefulMethod.getFormattedURL(UsefulMethod.getSFObjectURL(sfo.getType()),sfo.getID());
				
				//Runtime.getRuntime().exec("\""+user.getDefaultBrowser()+"\" "+urlToDisplay);
				Request r = RequestBuilder.buildPopup("\""+user.getDefaultBrowser()+"\" "+urlToDisplay);
				ManageRequest.send(user, r);
				
				//The best would be to trigger a SF Toast
				SalesForceManager.displaySFToast(user.getSalesforceID(), sfo);
				}
			}
		catch(Exception e)
			{
			Variables.getLogger().error("ERROR : While firing a notification Toast : "+e.getMessage(),e);
			}
		}
	
	}

/*2019*//*RATEL Alexandre 8)*/