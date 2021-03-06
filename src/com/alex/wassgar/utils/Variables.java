package com.alex.wassgar.utils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.alex.wassgar.curri.CURRIHTTPServer;
import com.alex.wassgar.jtapi.Call;
import com.alex.wassgar.jtapi.Monitor;
import com.alex.wassgar.jtapi.Observer;
import com.alex.wassgar.misc.User;
import com.alex.wassgar.misc.storedUUID;
import com.alex.wassgar.salesforce.ConnectionManager;
import com.alex.wassgar.server.ListenerManager;
import com.alex.wassgar.server.Watchman;
import com.alex.wassgar.webserver.WebListenerManager;
import com.sforce.soap.enterprise.EnterpriseConnection;




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
	
	/**
	 * Call status
	 */
	public enum callStatus
		{
		starting,
		inProgress,
		ended
		};
		
	/**
	 * Call type
	 */
	public enum callType
		{
		incoming,
		outgoing
		};
		
	/**
	 * SF Object type
	 */
	public enum sfObjectType
		{
		contact,
		lead,
		account,
		task
		};
		
	/**
	 * Contact search area
	 */
	public enum searchArea
		{
		all,
		user
		};
		
	/**
	 * web request
	 */
	public enum webRequestType
		{
		getCUCMUsers,
		getSalesforceUsers,
		getUserList,
		getUser,
		addUser,
		updateUser,
		deleteUser,
		getSettings,
		updateSettings,
		success,
		error
		}
		
	/**	MISC	**/
	private static String softwareName;
	private static String softwareVersion;
	private static cucmAXLVersion CUCMVersion;
	private static Logger logger;
	private static Logger callLogger;
	private static eMailSender eMSender;
	private static String mainConfigFileDirectory;
	private static ArrayList<String[][]> mainConfig;
	private static ArrayList<User> userList;
	private static String configFileName;
	private static String userFileName;
	private static boolean CUCMReachable;
	private static ArrayList<storedUUID> uuidList;
	private static boolean advancedLogs;
	private static ArrayList<Call> callList;
	
	/**	Language management	**/
	public enum language{english,french};
	private static String languageFileName;
	private static ArrayList<ArrayList<String[][]>> languageContentList;
	
	/**	AXL	**/
	private static com.cisco.axlapiservice10.AXLPort AXLConnectionToCUCMV105;//Connection to CUCM version 105
	
	/** JTAPI **/
	private static ArrayList<Observer> observerList;
	private static Monitor jtapiMonitor;
	
	/** SalesForce **/
	private static ConnectionManager sFConnectionManager;
	private static EnterpriseConnection sFConnection;
	
	/** Client management**/
	private static Watchman watchman;
	private static ListenerManager clientMonitor;
	
	/** CURRI management**/
	private static CURRIHTTPServer curriServer;
	
	/** Web Management **/
	private static WebListenerManager webServer;
	
	/**************
     * Constructor
     **************/
	public Variables()
		{
		configFileName = "configFile.xml";
		userFileName = "userFile.xml";
		languageFileName = "languages.xml";
		observerList = new ArrayList<Observer>();
		callList = new ArrayList<Call>();
		mainConfigFileDirectory = ".";
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

	public synchronized static Logger getLogger()
		{
		return logger;
		}

	public static void setLogger(Logger logger)
		{
		Variables.logger = logger;
		}

	public synchronized static eMailSender geteMSender()
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

	public static ArrayList<ArrayList<String[][]>> getLanguageContentList() throws Exception
		{
		if(languageContentList == null)
			{
			Variables.getLogger().debug("Initialisation of languageContentList");
			Variables.setLanguageContentList(UsefulMethod.readExtFile("language", Variables.getLanguageFileName()));
			}
		
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

	public static com.cisco.axlapiservice10.AXLPort getAXLConnectionToCUCMV105() throws Exception
		{
		if(AXLConnectionToCUCMV105 == null)
			{
			UsefulMethod.initAXLConnectionToCUCM();
			}
		return AXLConnectionToCUCMV105;
		}

	public static void setAXLConnectionToCUCMV105(com.cisco.axlapiservice10.AXLPort aXLConnectionToCUCMV105)
		{
		AXLConnectionToCUCMV105 = aXLConnectionToCUCMV105;
		}

	public synchronized static ArrayList<User> getUserList() throws Exception
		{
		if(userList == null)
			{
			Variables.getLogger().debug("Initialisation of userlist");
			Variables.setUserList(UsefulMethod.initUserList(Variables.getUserFileName()));
			}
		return userList;
		}

	public static void setUserList(ArrayList<User> userList)
		{
		Variables.userList = userList;
		}

	public static ArrayList<Observer> getObserverList()
		{
		return observerList;
		}

	public static void setObserverList(ArrayList<Observer> observerList)
		{
		Variables.observerList = observerList;
		}

	public static boolean isAdvancedLogs()
		{
		return advancedLogs;
		}

	public static void setAdvancedLogs(boolean advancedLogs)
		{
		Variables.advancedLogs = advancedLogs;
		}

	public static Logger getCallLogger()
		{
		return callLogger;
		}

	public static void setCallLogger(Logger callLogger)
		{
		Variables.callLogger = callLogger;
		}

	public static ArrayList<Call> getCallList()
		{
		if(callList == null)
			{
			callList = new ArrayList<Call>();
			}
		return callList;
		}

	public static void setCallList(ArrayList<Call> callList)
		{
		Variables.callList = callList;
		}

	public static EnterpriseConnection getSFConnection()
		{
		return sFConnection;
		}

	public static void setSFConnection(EnterpriseConnection sFConnection)
		{
		Variables.sFConnection = sFConnection;
		}

	public static Monitor getJtapiMonitor()
		{
		return jtapiMonitor;
		}

	public static void setJtapiMonitor(Monitor jtapiMonitor)
		{
		Variables.jtapiMonitor = jtapiMonitor;
		}

	public static ListenerManager getClientMonitor()
		{
		return clientMonitor;
		}

	public static void setClientMonitor(ListenerManager clientMonitor)
		{
		Variables.clientMonitor = clientMonitor;
		}

	public static Watchman getWatchman()
		{
		return watchman;
		}

	public static void setWatchman(Watchman watchman)
		{
		Variables.watchman = watchman;
		}

	public static CURRIHTTPServer getCurriServer()
		{
		return curriServer;
		}

	public static void setCurriServer(CURRIHTTPServer curriServer)
		{
		Variables.curriServer = curriServer;
		}

	public static ConnectionManager getsFConnectionManager()
		{
		return sFConnectionManager;
		}

	public static void setsFConnectionManager(ConnectionManager sFConnectionManager)
		{
		Variables.sFConnectionManager = sFConnectionManager;
		}

	public static WebListenerManager getWebServer()
		{
		return webServer;
		}

	public static void setWebServer(WebListenerManager webServer)
		{
		Variables.webServer = webServer;
		}
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
