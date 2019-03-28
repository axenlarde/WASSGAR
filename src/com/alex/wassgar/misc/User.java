package com.alex.wassgar.misc;

import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;

import com.alex.wassgar.server.ClientConnection;
import com.alex.wassgar.server.ClientListener;
import com.alex.wassgar.utils.Variables;


public class User
	{
	/**
	 * Variables
	 */
	private String ID, firstName, lastName, extension, email, cucmID, salesforceID, defaultBrowser;
	private boolean incomingCallPopup, reverseLookup, emailReminder;
	private ClientConnection connection;
	private ClientListener clientListener;
	
	public User(String firstName, String lastName, String extension, String email, String cucmID, String salesforceID,
			boolean incomingCallPopup, boolean reverseLookup, boolean emailReminder, String defaultBrowser)
		{
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.extension = extension;
		this.email = email;
		this.cucmID = cucmID;
		this.salesforceID = salesforceID;
		this.incomingCallPopup = incomingCallPopup;
		this.reverseLookup = reverseLookup;
		this.emailReminder = emailReminder;
		this.defaultBrowser = defaultBrowser;
		
		this.ID = DigestUtils.md5Hex(this.getInfo());
		}
	
	public String getInfo()
		{
		return firstName+" "+lastName;
		}
	
	/**
	 * Used to close everything related to the user before removing it
	 */
	public void prepareRemoval()
		{
		try
			{
			this.clientListener.tchao();
			this.connection.close();
			}
		catch (IOException e)
			{
			Variables.getLogger().error("Failed to close user connection : "+this.getInfo()+" : "+e.getMessage(),e);
			}
		}

	public String getFirstName()
		{
		return firstName;
		}

	public void setFirstName(String firstName)
		{
		this.firstName = firstName;
		}

	public String getLastName()
		{
		return lastName;
		}

	public void setLastName(String lastName)
		{
		this.lastName = lastName;
		}

	public String getExtension()
		{
		return extension;
		}

	public void setExtension(String extension)
		{
		this.extension = extension;
		}

	public String getEmail()
		{
		return email;
		}

	public void setEmail(String email)
		{
		this.email = email;
		}

	public String getCucmID()
		{
		return cucmID;
		}

	public void setCucmID(String cucmID)
		{
		this.cucmID = cucmID;
		}

	public String getSalesforceID()
		{
		return salesforceID;
		}

	public void setSalesforceID(String salesforceID)
		{
		this.salesforceID = salesforceID;
		}

	public boolean isIncomingCallPopup()
		{
		return incomingCallPopup;
		}

	public void setIncomingCallPopup(boolean incomingCallPopup)
		{
		this.incomingCallPopup = incomingCallPopup;
		}

	public boolean isReverseLookup()
		{
		return reverseLookup;
		}

	public void setReverseLookup(boolean reverseLookup)
		{
		this.reverseLookup = reverseLookup;
		}

	public boolean isEmailReminder()
		{
		return emailReminder;
		}

	public void setEmailReminder(boolean emailReminder)
		{
		this.emailReminder = emailReminder;
		}

	public String getDefaultBrowser()
		{
		return defaultBrowser;
		}

	public void setDefaultBrowser(String defaultBrowser)
		{
		this.defaultBrowser = defaultBrowser;
		}

	public ClientListener getClientListener()
		{
		return clientListener;
		}

	public void setClientListener(ClientListener clientListener)
		{
		this.clientListener = clientListener;
		}

	public synchronized ClientConnection getConnection()
		{
		return connection;
		}

	public void setConnection(ClientConnection connection)
		{
		this.connection = connection;
		}

	public String getID()
		{
		return ID;
		}

	public void setID(String iD)
		{
		ID = iD;
		}

	
	}

/*2019*//*RATEL Alexandre 8)*/