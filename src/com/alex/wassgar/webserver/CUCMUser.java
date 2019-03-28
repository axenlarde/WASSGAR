package com.alex.wassgar.webserver;

/**
 * Used to store a CUCM User data
 *
 * @author Alexandre RATEL
 */
public class CUCMUser
	{
	/**
	 * Variables
	 */
	private String firstName, lastName, userID, email, extension, UUID;

	public CUCMUser(String firstName, String lastName, String userID, String email, String extension, String uUID)
		{
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userID = userID;
		this.email = email;
		this.extension = extension;
		UUID = uUID;
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

	public String getEmail()
		{
		return email;
		}

	public void setEmail(String email)
		{
		this.email = email;
		}

	public String getExtension()
		{
		return extension;
		}

	public void setExtension(String extension)
		{
		this.extension = extension;
		}

	public String getUUID()
		{
		return UUID;
		}

	public void setUUID(String uUID)
		{
		UUID = uUID;
		}

	public String getUserID()
		{
		return userID;
		}

	public void setUserID(String userID)
		{
		this.userID = userID;
		}
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
