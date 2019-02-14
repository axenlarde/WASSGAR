package com.alex.wassgar.utils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.alex.wassgar.misc.storedUUID;




/**********************************
 * Used to store static variables
 * 
 * @author RATEL Alexandre
 **********************************/
public class Variables
	{
	/**
	 * Variables
	 */
	
	/**	ENUM	**/
	
	/***
	 * itemType :
	 * Is used to give a type to the request ready to be injected
	 * This way we can manage or sort them more easily
	 * 
	 * The order is important here, indeed, it will define later
	 * how the items are injected
	 */
	public enum itemType
		{
		phone,
		udp,
		user,
		line,
		unknown
		};
	
	public enum cucmAXLVersion
		{
		version80,
		version85,
		version90,
		version91,
		version100,
		version105,
		version110,
		version115,
		version120,
		version125
		};
	
	/********************************************
	 * actionType :
	 * Is used to set the type of action is going to do a 
	 ***************************************/
	public enum actionType
		{
		inject,
		delete,
		update
		};
		
	/**	MISC	**/
	private static String softwareName;
	private static String softwareVersion;
	private static cucmAXLVersion CUCMVersion;
	private static Logger logger;
	private static eMailSender eMSender;
	private static String mainConfigFileDirectory;
	private static ArrayList<String[][]> mainConfig;
	private static String configFileName;
	private static String userFileName;
	private static boolean CUCMReachable;
	private static ArrayList<storedUUID> uuidList;
	
	/**	Language management	**/
	public enum language{english,french};
	private static String languageFileName;
	private static ArrayList<ArrayList<String[][]>> languageContentList;
	
	/**	AXL	**/
	private static com.cisco.axlapiservice10.AXLPort AXLConnectionToCUCMV105;//Connection to CUCM version 105
	
	/**************
     * Constructor
     **************/
	public Variables()
		{
		configFileName = "configFile.xml";
		userFileName = "userFile.xml";
		
		}

	public static String getSoftwareName()
		{
		return softwareName;
		}

	public static void setSoftwareName(String softwareName)
		{
		Variables.softwareName = softwareName;
		}

	public static String getSoftwareVersion()
		{
		return softwareVersion;
		}

	public static void setSoftwareVersion(String softwareVersion)
		{
		Variables.softwareVersion = softwareVersion;
		}

	public static cucmAXLVersion getCUCMVersion()
		{
		return CUCMVersion;
		}

	public static void setCUCMVersion(cucmAXLVersion cUCMVersion)
		{
		CUCMVersion = cUCMVersion;
		}

	public static Logger getLogger()
		{
		return logger;
		}

	public static void setLogger(Logger logger)
		{
		Variables.logger = logger;
		}

	public static eMailSender geteMSender()
		{
		return eMSender;
		}

	public static void seteMSender(eMailSender eMSender)
		{
		Variables.eMSender = eMSender;
		}

	public static String getMainConfigFileDirectory()
		{
		return mainConfigFileDirectory;
		}

	public static void setMainConfigFileDirectory(String mainConfigFileDirectory)
		{
		Variables.mainConfigFileDirectory = mainConfigFileDirectory;
		}

	public static String getConfigFileName()
		{
		return configFileName;
		}

	public static void setConfigFileName(String configFileName)
		{
		Variables.configFileName = configFileName;
		}

	public static String getUserFileName()
		{
		return userFileName;
		}

	public static void setUserFileName(String userFileName)
		{
		Variables.userFileName = userFileName;
		}

	public static boolean isCUCMReachable()
		{
		return CUCMReachable;
		}

	public static void setCUCMReachable(boolean cUCMReachable)
		{
		CUCMReachable = cUCMReachable;
		}

	public static String getLanguageFileName()
		{
		return languageFileName;
		}

	public static void setLanguageFileName(String languageFileName)
		{
		Variables.languageFileName = languageFileName;
		}

	public static ArrayList<ArrayList<String[][]>> getLanguageContentList()
		{
		return languageContentList;
		}

	public static void setLanguageContentList(ArrayList<ArrayList<String[][]>> languageContentList)
		{
		Variables.languageContentList = languageContentList;
		}

	public static ArrayList<String[][]> getMainConfig()
		{
		return mainConfig;
		}

	public static void setMainConfig(ArrayList<String[][]> mainConfig)
		{
		Variables.mainConfig = mainConfig;
		}

	public static ArrayList<storedUUID> getUuidList()
		{
		return uuidList;
		}

	public static void setUuidList(ArrayList<storedUUID> uuidList)
		{
		Variables.uuidList = uuidList;
		}

	public static com.cisco.axlapiservice10.AXLPort getAXLConnectionToCUCMV105()
		{
		return AXLConnectionToCUCMV105;
		}

	public static void setAXLConnectionToCUCMV105(com.cisco.axlapiservice10.AXLPort aXLConnectionToCUCMV105)
		{
		AXLConnectionToCUCMV105 = aXLConnectionToCUCMV105;
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}