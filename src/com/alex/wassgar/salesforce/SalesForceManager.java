package com.alex.wassgar.salesforce;

import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.GetUserInfoResult;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SearchRecord;
import com.sforce.soap.enterprise.SearchResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.bind.XmlObject;
import com.alex.wassgar.jtapi.Call;
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
		
		/** To test only
		DescribeGlobalResult dgr = connection.describeGlobal();
		
		for(DescribeGlobalSObjectResult result : dgr.getSobjects())
			{
			Variables.getLogger().debug("# "+result.getName());
			}
		*/
		/**
		DescribeSObjectResult[] dsrArray = Variables.getSfConnection().describeSObjects(new String[] { "Contact" });
		
		for(DescribeSObjectResult contact : dsrArray)
			{
			Variables.getLogger().debug("Name : "+contact.getName());
			Variables.getLogger().debug("Label : "+contact.getLabel());
			
			Variables.getLogger().debug("Fields : ");
			for(Field f : contact.getFields())
				{
				Variables.getLogger().debug(f.getName());
				Variables.getLogger().debug(f.getLabel());
				}
			}
			*/
		
		/*
		//Temp
		String query = "SELECT Id, FirstName, LastName, OwnerId FROM Contact";
		//String query = "SELECT Id, FirstName, LastName FROM User";
		QueryResult qr = Variables.getSFConnection().query(query);
		for(SObject so : qr.getRecords())
			{
			Contact c = (Contact)so;
			Variables.getLogger().debug("Contact Info :");
			Variables.getLogger().debug("ID : "+c.getId());
			Variables.getLogger().debug("Last Name : "+c.getLastName());
			Variables.getLogger().debug("First Name : "+c.getFirstName());
			Variables.getLogger().debug("Mobile Phone : "+c.getMobilePhone());
			Variables.getLogger().debug("Owner ID : "+c.getOwnerId());
			}
		
		GetUserInfoResult r = Variables.getSFConnection().getUserInfo();
		Variables.getLogger().debug("User Info : ");
		Variables.getLogger().debug(r.getUserName());
		Variables.getLogger().debug(r.getUserId());*/
		}
	
	/**
	 * Used to log a new call for a given user
	 */
	public static void logNewSFCall(String userID, Call call)
		{
		//To be written
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
	 */
	public static void displaySFToast(String userID, SFObject object)
		{
		if(object.getType().equals(sfObjectType.contact))
			{
			
			}
		else if(object.getType().equals(sfObjectType.lead))
			{
			
			}
		else if(object.getType().equals(sfObjectType.account))
			{
			
			}
		}
	
	/**
	 * Used to propose to the user to create a new entry
	 */
	public static void createNewEntry(String userID, String Extension)
		{
		//Has to be written
		}

	}

/*2019*//*RATEL Alexandre 8)*/