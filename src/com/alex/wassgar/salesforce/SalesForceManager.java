package com.alex.wassgar.salesforce;

import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.SearchRecord;
import com.sforce.soap.enterprise.SearchResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.enterprise.sobject.Task;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import java.io.IOException;

import com.alex.wassgar.jtapi.Call;
import com.alex.wassgar.utils.LanguageManagement;
import com.alex.wassgar.utils.UsefulMethod;
import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.Variables.callType;
import com.alex.wassgar.utils.Variables.searchArea;
import com.alex.wassgar.utils.Variables.sfObjectType;





/*******
 * Class used to store static method about the salesforce connection
 * @author Alexandre
 */
public class SalesForceManager
	{
	
	/**
	 * Method used to start a new SalesForce connection
	 * @throws ConnectionException 
	 */
	public static void connection(String username, String password, String securityToken) throws ConnectionException
		{
		Variables.getLogger().info("SF : Connection to SalesForce attempt");
		ConnectorConfig config = new ConnectorConfig();
		config.setUsername(username);
		config.setPassword(password+securityToken);
		
		Variables.setSFConnection(Connector.newConnection(config));
		Variables.getLogger().info("SF : Connected to SalesForce !");
		Variables.getLogger().debug("SF : Auth EndPoint: "+config.getAuthEndpoint());
		Variables.getLogger().debug("SF : Service EndPoint: "+config.getServiceEndpoint());
		Variables.getLogger().debug("SF : Username: "+config.getUsername());
		Variables.getLogger().debug("SF : SessionId: "+config.getSessionId());
		}
	
	/**
	 * Used to log a new call for a given user
	 */
	public static String logNewSFCall(String userID, Call call, SFObject sfo)
		{
		try
			{
			String alertingName = UsefulMethod.getAlertingNameFromSFObject(sfo);
			
			Task task = new Task();
			task.setOwnerId(userID);
			task.setTaskSubtype("Call");
			if(call.getType().equals(callType.incoming))
				{
				String ic = LanguageManagement.getString("incomingcall");
				task.setSubject(ic);
				
				StringBuffer description = new StringBuffer();
				description.append(ic);
				description.append(LanguageManagement.getString("calldescriptionincoming"));
				description.append(alertingName);
				
				task.setDescription(description.toString());
				}
			else
				{
				String oc = LanguageManagement.getString("outgoingcall");
				task.setSubject(oc);
				
				StringBuffer description = new StringBuffer();
				description.append(oc);
				description.append(LanguageManagement.getString("calldescriptionoutgoing"));
				description.append(alertingName);
				
				task.setDescription(description.toString());
				}
			
			if(sfo.getType().equals(sfObjectType.account))
				{
				task.setWhatId(sfo.getID());//To validate
				}
			else
				{
				task.setWhoId(sfo.getID());
				}
			
			SaveResult[] results = Variables.getSFConnection().create(new Task[] {task});
			
			if(results[0].getSuccess())
				{
				Variables.getLogger().debug("New Call created for user "+userID+" from "+alertingName);
				return results[0].getId();
				}
			else
				{
				throw new Exception("The task creation failed");
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : While creating a new task : "+e.getMessage(),e);
			}
		return "";
		}
	
	/**
	 * Used to look for an extension number in salesforce
	 * Will return directly an alerting name if something is found
	 */
	public static SFObject lookForExtension(String userID, String extension) throws Exception
		{
		SFObject sfo = null;
		
		/**
		 * If multiple items are found we just return the first one
		 * starting with contact
		 * 
		 * We are looking for extensions in the following items :
		 * - Contacts
		 * - Leads
		 * - Accounts
		 */
		
		String query;
		if(UsefulMethod.getSearchArea().equals(searchArea.all))
			{
			query = "FIND {"+extension+"} IN Phone FIELDS RETURNING "+
					"Contact(Id, Phone, FirstName, LastName),"+
					"Lead(Id, Phone, FirstName, LastName),"+
					"Account(Id, Phone, Name)";
			}
		else //User only
			{
			query = "FIND {"+extension+"} IN Phone FIELDS RETURNING "+ 
					"Contact(Id, Phone, FirstName, LastName WHERE OwnerId='"+userID+"'),"+ 
					"Lead(Id, Phone, FirstName, LastName WHERE OwnerId='"+userID+"'),"+ 
					"Account(Id, Phone, Name)";
			}
		
		SearchResult result = Variables.getSFConnection().search(query);
		
		for(SearchRecord sr : result.getSearchRecords())
			{
			SObject r = sr.getRecord();
			
			if(r instanceof Contact)
				{
				sfo = new SFObject(sfObjectType.contact, r.getId(), r);
				}
			else if(r instanceof Lead)
				{
				sfo = new SFObject(sfObjectType.lead, r.getId(), r);
				}
			else if(r instanceof Account)
				{
				sfo = new SFObject(sfObjectType.account, r.getId(), r);
				}
			}
		
		if(sfo != null)Variables.getLogger().debug("Data found for extention : "+extension);
		
		return sfo;
		}
	
	/**
	 * Used to display a toast in salesforce to display the salesforce object information to the given user
	 * For instance : the SFObject is a contact, so we display the contact page to the user
	 * @throws IOException 
	 */
	public static void displaySFToast(String userID, SFObject sfo)
		{
		/**
		 * At the moment we just display a new tab in the choosen browser
		 */
		try
			{
			//To be written
			}
		catch(Exception e)
			{
			Variables.getLogger().error("ERROR : While firing a SF notification Toast : "+e.getMessage(),e);
			}
		}

	}

/*2019*//*RATEL Alexandre 8)*/