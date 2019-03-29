package com.alex.wassgar.misc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.alex.wassgar.jtapi.Observer;
import com.alex.wassgar.server.Request;
import com.alex.wassgar.server.RequestBuilder;
import com.alex.wassgar.utils.UsefulMethod;
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
				if(u.getID().equals(newUser.getID()))
					{
					Variables.getLogger().debug("The user \""+newUser.getInfo()+"\"already exists so we abort the creation");
					return false;
					}
				}
			//If we reach this point it means that the user doesn't already exists, so we add it
			Variables.getUserList().add(newUser);
			
			//We start the JTAPI monitoring
			if(Variables.getJtapiMonitor() != null)Variables.getJtapiMonitor().updateMonitoring();
			
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
	public synchronized static boolean deleteUser(String ID)
		{
		try
			{
			for(User u : Variables.getUserList())
				{
				if(u.getID().equals(ID))
					{
					/**
					 * We now close all the user dependencies
					 */
					//Closing client socket
					u.prepareRemoval();
					
					//Closing JTAPI observer
					if(Variables.getJtapiMonitor() != null)Variables.getJtapiMonitor().deleteUserMonitoring(u);
					
					//Finally we remove the user from the list
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
			return false;
			}
		
		Variables.getLogger().debug("User ID \""+ID+"\" not found");
		return false;
		}
	
	/**
	 * Update a user in the userFile and in
	 * the users currently loaded in memory
	 */
	public synchronized static boolean updateUser(String ID, String extension, String defaultBrowser, boolean incomingCallPopup, boolean reverseLookup, boolean emailReminder)
		{
		try
			{
			for(User u : Variables.getUserList())
				{
				if(u.getID().equals(ID))
					{
					if((extension != null) && (!extension.equals("")))
						{
						if(!extension.equals(u.getExtension()))//We check that the new extension is not the same
							{
							u.setExtension(extension);
							
							//If the extension changed we restart the JTAPI monitoring
							if(Variables.getJtapiMonitor() != null)
								{
								Variables.getJtapiMonitor().deleteUserMonitoring(u);
								Variables.getJtapiMonitor().updateMonitoring();
								}
							}
						}
					
					if((defaultBrowser != null) && (!defaultBrowser.equals("")))
						{
						if(!defaultBrowser.equals(u.getDefaultBrowser()))//We check that the new extension is not the same
							{
							u.setDefaultBrowser(defaultBrowser);
							}
						}
					
					u.setIncomingCallPopup(incomingCallPopup);
					u.setReverseLookup(reverseLookup);
					u.setEmailReminder(emailReminder);
					
					//We notify the user client
					if(u.getConnection() != null)
						{
						Request optionUpdate = RequestBuilder.buildOptionUpdate(UsefulMethod.encodeOptionList(u));
						u.getConnection().getOut().writeObject(optionUpdate);
						u.getConnection().getOut().flush();
						}
					else
						{
						Variables.getLogger().debug("No client found for user "+u.getInfo()+" so no option update were sent");
						}
					
					//Now we rewrite the userFile;
					rewriteUserFile();
							
					Variables.getLogger().debug("User \""+u.getInfo()+"\" updated !");
					return true;
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Error : "+e.getMessage(),e);
			}
		
		Variables.getLogger().debug("Failed to update the user with ID \""+ID+"\"");
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
					"			<id>"+u.getID()+"</id>\r\n" + 
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