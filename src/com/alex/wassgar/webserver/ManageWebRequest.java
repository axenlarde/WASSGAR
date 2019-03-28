package com.alex.wassgar.webserver;

import java.util.ArrayList;

import com.alex.wassgar.misc.ManageUserFile;
import com.alex.wassgar.misc.User;
import com.alex.wassgar.salesforce.SalesForceManager;
import com.alex.wassgar.utils.UsefulMethod;
import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.Variables.webRequestType;
import com.alex.wassgar.utils.xMLGear;
import com.cisco.axl.api._10.LUser;
import com.cisco.axlapiservice10.AXLError;


/**
 * Used to Manage web requests
 *
 * @author Alexandre RATEL
 */
public class ManageWebRequest
	{
	
	/**
	 * Will decode web request
	 * 
	 * Web request format :
	 * <xml>
	 * 	<request>
	 * 		<type>webRequestType<type>
	 * 		<content>
	 * 			<...>
	 * 		</content>
	 * 	</request>
 	 * </xml> 
	 * @throws Exception 
	 */
	public synchronized static WebRequest parseWebRequest(String content) throws Exception
		{
		ArrayList<String> params = new ArrayList<String>();
		
		//We parse the request type
		params.add("request");
		ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
		webRequestType type = webRequestType.valueOf(UsefulMethod.getItemByName("type", parsed.get(0)));
		Variables.getLogger().debug("Web request type found : "+type.name());
		
		return new WebRequest(content, type);
		}
	
	/**
	 * Get all the CUCM users
	 */
	public synchronized static WebRequest getCUCMUsers(String content)
		{
		try
			{
			com.cisco.axl.api._10.ListUserReq req = new com.cisco.axl.api._10.ListUserReq();
			com.cisco.axl.api._10.ListUserReq.SearchCriteria sc = new com.cisco.axl.api._10.ListUserReq.SearchCriteria();
			com.cisco.axl.api._10.LUser rt = new com.cisco.axl.api._10.LUser();
			
			sc.setUserid("");
			rt.setUuid("");
			rt.setLastName("");
			rt.setFirstName("");
			rt.setUserid("");
			rt.setMailid("");
			rt.setTelephoneNumber("");//Should be improved, we should instead retrieve the associated line
			
			req.setSearchCriteria(sc);
			req.setReturnedTags(rt);
			
			com.cisco.axl.api._10.ListUserRes resp = Variables.getAXLConnectionToCUCMV105().listUser(req);
			
			ArrayList<CUCMUser> ul = new ArrayList<CUCMUser>();
			
			for(LUser u : resp.getReturn().getUser())
				{
				ul.add(new CUCMUser(u.getFirstName(),
						u.getLastName(),
						u.getUserid(),
						u.getMailid(),
						u.getTelephoneNumber(),
						u.getUuid()));
				}
			
			Variables.getLogger().debug(ul.size()+" user retrieved form CUCM");
			
			return WebRequestBuilder.buildGetCUCMUsersReply(ul);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Error while fetching CUCM users : "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * Get all the Salesforce users
	 */
	public synchronized static WebRequest getSalesforceUsers(String content)
		{
		ArrayList<SalesForceUser> ul = SalesForceManager.getUserList();
		if(ul.size() == 0)return null;
		
		return WebRequestBuilder.buildGetSalesforceUsersReply(ul);
		}
	
	/**
	 * Get the internal user list
	 */
	public synchronized static WebRequest getUserList()
		{
		return WebRequestBuilder.buildGetUserList();
		}
	
	/**
	 * Add a new user
	 * 
	 * Web request format :
	 * <xml>
	 * 	<request>
	 * 		<type>addUser<type>
	 * 		<content>
	 * 			<user>
	 * 				<...>
	 * 			</user>
	 * 		</content>
	 * 	</request>
 	 * </xml> 
	 */
	public synchronized static void addUser(String content)
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			params.add("user");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			ManageUserFile.addUser(UsefulMethod.getItemByName("firstname", t),
					UsefulMethod.getItemByName("lastname", t),
					UsefulMethod.getItemByName("extension", t),
					UsefulMethod.getItemByName("email", t),
					UsefulMethod.getItemByName("cucmid", t),
					UsefulMethod.getItemByName("salesforceid", t),
					Boolean.parseBoolean(UsefulMethod.getItemByName("incomingcallpopup", t)),
					Boolean.parseBoolean(UsefulMethod.getItemByName("reverselookup", t)),
					Boolean.parseBoolean(UsefulMethod.getItemByName("emailreminder", t)),
					UsefulMethod.getItemByName("defaultbrowser", t));
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to add a new user : "+e.getMessage(),e);
			}
		}
	
	/**
	 * Update a given user
	 * 
	 * Web request format :
	 * <xml>
	 * 	<request>
	 * 		<type>updateUser<type>
	 * 		<content>
	 * 			<user>
	 * 				<id>
	 * 				<extension>
	 * 				<incomingCallPopup>
	 * 				<reverseLookup>
	 * 				<emailReminder>
	 * 			</user>
	 * 		</content>
	 * 	</request>
 	 * </xml> 
	 */
	public synchronized static void updateUser(String content)
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			params.add("user");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			ManageUserFile.updateUser(UsefulMethod.getItemByName("id", t),
					UsefulMethod.getItemByName("extension", t),
					Boolean.parseBoolean(UsefulMethod.getItemByName("incomingcallpopup", t)),
					Boolean.parseBoolean(UsefulMethod.getItemByName("reverselookup", t)),
					Boolean.parseBoolean(UsefulMethod.getItemByName("emailreminder", t)));
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to add a new user : "+e.getMessage(),e);
			}
		}
	
	/**
	 * Delete a given user
	 * 
	 * Web request format :
	 * <xml>
	 * 	<request>
	 * 		<type>updateUser<type>
	 * 		<content>
	 * 			<user>
	 * 				<id>
	 * 			</user>
	 * 		</content>
	 * 	</request>
 	 * </xml> 
	 */
	public synchronized static void deleteUser(String content)
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			params.add("user");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			ManageUserFile.deleteUser(UsefulMethod.getItemByName("id", t));
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to delete user : "+e.getMessage(),e);
			}
		}
	
	/**
	 * Get a given user
	 * 
	 * Web request format :
	 * <xml>
	 * 	<request>
	 * 		<type>getUser<type>
	 * 		<content>
	 * 			<user>
	 * 				<id>
	 * 			</user>
	 * 		</content>
	 * 	</request>
 	 * </xml> 
	 */
	public synchronized static WebRequest getUser(String content)
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			params.add("user");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			for(User u : Variables.getUserList())
				{
				if(u.getID().equals(UsefulMethod.getItemByName("id", t)))
					{
					return WebRequestBuilder.buildGetUserReply(u);
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to add a new user : "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * get settings
	 */
	public synchronized static WebRequest getSettings()
		{
		//To be written
		
		return null;
		}
	
	/**
	 * update settings
	 */
	public synchronized static void updateSettings(String content)
		{
		//To be written
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
