package com.alex.wassgar.webserver;

/**
 * Used to store a Salesforce user data
 *
 * @author Alexandre RATEL
 */
public class SalesForceUser
	{
	/**
	 * Variables
	 */
	private String firstName, lastName, userID, email, ID;

	public SalesForceUser(String firstName, String lastName, String userID, String email, String iD)
		{
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userID = userID;
		this.email = email;
		ID = iD;
		}
	
	public String getInfo()
		{
		return firstName+" "+lastName;
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

	public String getUserID()
		{
		return userID;
		}

	public void setUserID(String userID)
		{
		this.userID = userID;
		}

	public String getEmail()
		{
		return email;
		}

	public void setEmail(String email)
		{
		this.email = email;
		}

	public String getID()
		{
		return ID;
		}

	public void setID(String iD)
		{
		ID = iD;
		}
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
