package com.alex.wassgar.misc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.alex.wassgar.utils.Variables;

/**
 * Class used to manage the user file
 * 
 * @author Alexandre
 */
public class ManageUserFile
	{

	/**
	 * Add a new user in the userFile and in
	 * the users currently loaded in memory
	 */
	public synchronized static boolean addUser(String firstName, String lastName, String extension, String email, String cucmID, String salesforceID, boolean incomingCallPopup, boolean reverseLookup, boolean emailReminder, String defaultBrowser)
		{
		try
			{
			/******
			 * Add the new user in memory
			 */
			User newUser = new User(firstName,
					lastName,
					extension,
					email,
					cucmID,
					salesforceID,
					incomingCallPopup,
					reverseLookup,
					emailReminder,
					defaultBrowser);
			
			//we check that the user doesn't already exists
			for(User u : Variables.getUserList())
				{
				if(u.getInfo().equals(newUser.getInfo()))
					{
					Variables.getLogger().debug("The user \""+newUser.getInfo()+"\"already exists so we abort the creation");
					return false;
					}
				}
			//If we reach this point it means that the user doesn't already exists, so we add it
			Variables.getUserList().add(newUser);
			
			/******
			 * Add the new user in the user file
			 */
			rewriteUserFile();
			
			Variables.getLogger().debug("User \""+newUser.getInfo()+"\" added with success !");
			return true;
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Error : "+e.getMessage(),e);
			}
		return false;
		}
	
	/**
	 * Delete a user in the userFile and in
	 * the users currently loaded in memory
	 */
	public synchronized static boolean deleteUser(String userInfo)
		{
		try
			{
			for(User u : Variables.getUserList())
				{
				if(u.getInfo().equals(userInfo))
					{
					Variables.getUserList().remove(u);
					
					//Now we rewrite the userFile;
					rewriteUserFile();
					
					Variables.getLogger().debug("User \""+u.getInfo()+"\" deleted with success !");
					return true;
					}
				}
			} 
		catch (Exception e)
			{
			Variables.getLogger().error("Error : "+e.getMessage(),e);
			}
		
		Variables.getLogger().debug("User \""+userInfo+"\" not found");
		return false;
		}
	
	/**
	 * Update a user in the userFile and in
	 * the users currently loaded in memory
	 */
	public synchronized static boolean updateUser(User u)
		{
		try
			{
			//Now we rewrite the userFile;
			rewriteUserFile();
					
			Variables.getLogger().debug("User \""+u.getInfo()+"\" updated !");
			return true;
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Error : "+e.getMessage(),e);
			}
		
		Variables.getLogger().debug("Failed to update the user \""+u.getInfo()+"\"");
		return false;
		}
	
	/**
	 * Used to rewrite the user file based on the
	 * user list
	 * @throws Exception 
	 */
	private static void rewriteUserFile() throws Exception
		{
		//We create the content
		StringBuffer content = new StringBuffer();
		content.append("<!--\r\n" + 
				"User file\r\n" + 
				"\r\n" + 
				"Contains all the activated users\r\n" + 
				"-->\r\n" + 
				"\r\n" + 
				"<xml>\r\n" + 
				"	<users>\r\n");
		
		for(User u : Variables.getUserList())
			{
			content.append("		<user>\r\n" + 
					"			<firstname>"+u.getFirstName()+"</firstname>\r\n" + 
					"			<lastname>"+u.getLastName()+"</lastname>\r\n" + 
					"			<extension>"+u.getExtension()+"</extension>\r\n" + 
					"			<email>"+u.getEmail()+"</email>\r\n" + 
					"			<cucmid>"+u.getCucmID()+"</cucmid>\r\n" + 
					"			<salesforceid>"+u.getSalesforceID()+"</salesforceid>\r\n" + 
					"			<defaultbrowser>"+u.getDefaultBrowser()+"</defaultbrowser>\r\n" + 
					"			<incomingcallpopup>"+u.isIncomingCallPopup()+"</incomingcallpopup>\r\n" + 
					"			<reverselookup>"+u.isReverseLookup()+"</reverselookup>\r\n" + 
					"			<emailreminder>"+u.isEmailReminder()+"</emailreminder>\r\n" + 
					"		</user>\r\n");
			}
		content.append("	</users>\r\n" + 
				"</xml>");
		
		//We write the file
		File userFile = new File(Variables.getMainConfigFileDirectory()+"/"+Variables.getUserFileName());
		BufferedWriter buf = new BufferedWriter(new FileWriter(userFile, false));
		buf.write(content.toString());
		buf.flush();
		buf.close();
		Variables.getLogger().debug("User file created with success !");
		}
	
	
	}

/*2019*//*RATEL Alexandre 8)*/