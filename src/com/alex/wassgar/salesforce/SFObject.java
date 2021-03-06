package com.alex.wassgar.salesforce;

import com.alex.wassgar.utils.UsefulMethod;
import com.alex.wassgar.utils.Variables.sfObjectType;

/**
 * Class used to store a SalesForce Object
 * @author Alexandre
 */
public class SFObject
	{
	/**
	 * Variables
	 */
	private sfObjectType type;
	private String ID;
	private Object object;
	
	public SFObject(sfObjectType type, String iD, Object object)
		{
		super();
		this.type = type;
		ID = iD;
		this.object = object;
		}
	
	/**
	 * Used to return object info
	 * @throws Exception 
	 */
	public String getInfo() throws Exception
		{
		return UsefulMethod.getAlertingNameFromSFObject(this);
		}

	public sfObjectType getType()
		{
		return type;
		}

	public String getID()
		{
		return ID;
		}

	public Object getObject()
		{
		return object;
		}
	
	}

/*2019*//*RATEL Alexandre 8)*/