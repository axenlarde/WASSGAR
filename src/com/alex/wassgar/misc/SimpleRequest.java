package com.alex.wassgar.misc;

import java.util.List;
import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.Variables.cucmAXLVersion;
import com.alex.wassgar.utils.Variables.itemType;


/**********************************
 * Class used to contain static method for
 * simple common AXL request to the CUCM
 * 
 * @author RATEL Alexandre
 **********************************/
public class SimpleRequest
	{
	
	
	
	/**************
	 * Method aims to return the Version of the CUCM of the asked item
	 *************/
	public static String getCUCMVersion() throws Exception
		{
		if(Variables.getCUCMVersion().equals(cucmAXLVersion.version105))
			{
			com.cisco.axl.api._10.GetCCMVersionReq req = new com.cisco.axl.api._10.GetCCMVersionReq();
			com.cisco.axl.api._10.GetCCMVersionRes resp = Variables.getAXLConnectionToCUCMV105().getCCMVersion(req);//We send the request to the CUCM
			
			Variables.getLogger().info("CUCM Version : "+resp.getReturn().getComponentVersion().getVersion());
			return resp.getReturn().getComponentVersion().getVersion();
			}
		else
			{
			throw new Exception("Unsupported AXL version");
			}
		}
	
	/*****
	 * Used to get the string value of an UUID item
	 */
	public static String getUUID(itemType type, String itemName) throws Exception
		{
		if(Variables.getCUCMVersion().equals(cucmAXLVersion.version105))
			{
			return getUUIDV105(type, itemName).getUuid();
			}
		else
			{
			throw new Exception("Unsupported AXL version");
			}
		}
	
	
	/**
	 * Method used to find a UUID from the CUCM
	 * 
	 * In addition it stores all the UUID found to avoid to
	 * Interrogate the CUCM twice
	 * @throws Exception 
	 */
	public static com.cisco.axl.api._10.XFkType getUUIDV105(itemType type, String itemName) throws Exception
		{
		Variables.getLogger().debug("Get UUID from CUCM : "+type+" "+itemName);
		
		if((itemName == null) || (itemName.equals("")))
			{
			return getXFKV105("", itemName, type);
			}
		
		String id = type.name()+itemName;
		
		for(storedUUID s : Variables.getUuidList())
			{
			if(s.getComparison().equals(id))
				{
				Variables.getLogger().debug("UUID known");
				return getXFKWithoutStoringItV105(s.getUUID(), itemName, type);
				}
			}
		
		if(type.equals(itemType.phone))
			{
			com.cisco.axl.api._10.GetPhoneReq req = new com.cisco.axl.api._10.GetPhoneReq();
			com.cisco.axl.api._10.RPhone returnedTags = new com.cisco.axl.api._10.RPhone();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetPhoneRes resp = Variables.getAXLConnectionToCUCMV105().getPhone(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getPhone().getUuid(), itemName, type);
			}
		else if(type.equals(itemType.udp))
			{
			com.cisco.axl.api._10.GetDeviceProfileReq req = new com.cisco.axl.api._10.GetDeviceProfileReq();
			com.cisco.axl.api._10.RDeviceProfile returnedTags = new com.cisco.axl.api._10.RDeviceProfile();
			req.setName(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetDeviceProfileRes resp = Variables.getAXLConnectionToCUCMV105().getDeviceProfile(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getDeviceProfile().getUuid(), itemName, type);
			}
		else if(type.equals(itemType.user))
			{
			com.cisco.axl.api._10.GetUserReq req = new com.cisco.axl.api._10.GetUserReq();
			com.cisco.axl.api._10.RUser returnedTags = new com.cisco.axl.api._10.RUser();
			req.setUserid(itemName);
			returnedTags.setUuid("");
			req.setReturnedTags(returnedTags);
			com.cisco.axl.api._10.GetUserRes resp = Variables.getAXLConnectionToCUCMV105().getUser(req);//We send the request to the CUCM
			return getXFKV105(resp.getReturn().getUser().getUuid(), itemName, type);
			}
		
		
		throw new Exception("ItemType \""+type+"\" not found");
		}
	
	
	/***************
	 * Store and return an XFXType item from an UUID
	 */
	private static com.cisco.axl.api._10.XFkType getXFKV105(String UUID, String itemName, itemType type)
		{
		com.cisco.axl.api._10.XFkType xfk = new com.cisco.axl.api._10.XFkType();
		//UUID = UUID.toLowerCase();//Temp
		Variables.getUuidList().add(new storedUUID(UUID, itemName, type));//We add the item to the uuid stored slist
		xfk.setUuid(UUID);
		Variables.getLogger().debug("Returned UUID from CUCM : "+xfk.getUuid());
		return xfk;
		}
	
	/***************
	 * return an XFXType item
	 */
	private static com.cisco.axl.api._10.XFkType getXFKWithoutStoringItV105(String UUID, String itemName, itemType type)
		{
		com.cisco.axl.api._10.XFkType xfk = new com.cisco.axl.api._10.XFkType();
		xfk.setUuid(UUID);
		Variables.getLogger().debug("Returned UUID from CUCM : "+xfk.getUuid());
		return xfk;
		}
	
	
	/**
	 * Method used to reach the method of the good version
	 */
	public static List<Object> doSQLQuery(String request) throws Exception
		{
		if(Variables.getCUCMVersion().equals(cucmAXLVersion.version105))
			{
			return doSQLQueryV105(request);
			}
		
		throw new Exception("Unsupported AXL Version");
		}
	
	/**
	 * Method used to reach the method of the good version
	 */
	public static void doSQLUpdate(String request) throws Exception
		{
		if(Variables.getCUCMVersion().equals(cucmAXLVersion.version105))
			{
			doSQLUpdateV105(request);
			}
		else
			{
			throw new Exception("Unsupported AXL Version");
			}
		}
	
	
	/***
	 * Method used to launch a SQL request to the CUCM and get
	 * a result as an ArrayList<String>
	 * 
	 * each "String" is a list of result
	 */
	private static List<Object> doSQLQueryV105(String request) throws Exception
		{
		Variables.getLogger().debug("SQL request sent : "+request);
		
		com.cisco.axl.api._10.ExecuteSQLQueryReq req = new com.cisco.axl.api._10.ExecuteSQLQueryReq();
		req.setSql(request);
		com.cisco.axl.api._10.ExecuteSQLQueryRes resp = Variables.getAXLConnectionToCUCMV105().executeSQLQuery(req);//We send the request to the CUCM
		
		List<Object> myList = resp.getReturn().getRow();
		
		return myList;
		}
	
	/***
	 * Method used to launch a SQL request to the CUCM and get
	 * a result as an ArrayList<String>
	 * 
	 * each "String" is a list of result
	 */
	private static void doSQLUpdateV105(String request) throws Exception
		{
		Variables.getLogger().debug("SQL request sent : "+request);
		
		com.cisco.axl.api._10.ExecuteSQLUpdateReq req = new com.cisco.axl.api._10.ExecuteSQLUpdateReq();
		req.setSql(request);
		com.cisco.axl.api._10.ExecuteSQLUpdateRes resp = Variables.getAXLConnectionToCUCMV105().executeSQLUpdate(req);//We send the request to the CUCM
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}

