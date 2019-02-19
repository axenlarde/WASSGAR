package com.alex.wassgar.utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JOptionPane;
import javax.xml.ws.BindingProvider;

import org.apache.log4j.Level;

import com.alex.wassgar.misc.SimpleRequest;
import com.alex.wassgar.misc.User;
import com.alex.wassgar.utils.Variables.cucmAXLVersion;
import com.alex.wassgar.utils.Variables.itemType;


/**********************************
 * Class used to store the useful static methods
 * 
 * @author RATEL Alexandre
 **********************************/
public class UsefulMethod
	{
	
	/*****
	 * Method used to read the main config file
	 * @throws Exception 
	 */
	public static ArrayList<String[][]> readMainConfigFile(String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			file = xMLReader.fileRead(".\\"+fileName);
			
			listParams.add("config");
			return xMLGear.getResultListTab(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the file : "+fileName+" : "+exc.getMessage());
			}
		
		}
	
	
	/***************************************
	 * Method used to get a specific value
	 * in the user preference XML File
	 ***************************************/
	public synchronized static String getTargetOption(String node) throws Exception
		{
		for(String[] s : Variables.getMainConfig().get(0))
			{
			if(s[0].equals(node))return s[1];
			}
		
		/***********
		 * If this point is reached, the option looked for was not found
		 */
		throw new Exception("Option \""+node+"\" not found"); 
		}
	/*************************/
	
	
	
	/************************
	 * Check if java version
	 * is correct
	 ***********************/
	public static void checkJavaVersion()
		{
		try
			{
			String jVer = new String(System.getProperty("java.version"));
			Variables.getLogger().info("Detected JRE version : "+jVer);
			
			/*Need to use the config file value*/
			
			if(jVer.contains("1.6"))
				{
				
				if(Integer.parseInt(jVer.substring(6,8))<16)
					{
					Variables.getLogger().info("JRE version is not compatible. The application will now exit. system.exit(0)");
					System.exit(0);
					}
				}
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().info("ERROR : It has not been possible to detect JRE version",exc);
			}
		}
	/***********************/
	
	
	/******
	 * Method used to initialize the AXL Connection to the CUCM
	 */
	public static synchronized void initAXLConnectionToCUCM() throws Exception
		{
		try
			{
			UsefulMethod.disableSecurity();//We first turned off security
			
			if(Variables.getCUCMVersion().equals(cucmAXLVersion.version105))
				{
				com.cisco.axlapiservice10.AXLAPIService axlService = new com.cisco.axlapiservice10.AXLAPIService();
				com.cisco.axlapiservice10.AXLPort axlPort = axlService.getAXLPort();
				
				// Set the URL, user, and password on the JAX-WS client
				String validatorUrl = "https://"+UsefulMethod.getTargetOption("axlhost")+":"+UsefulMethod.getTargetOption("axlport")+"/axl/";
				
				((BindingProvider) axlPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, validatorUrl);
				((BindingProvider) axlPort).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, UsefulMethod.getTargetOption("axlusername"));
				((BindingProvider) axlPort).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, UsefulMethod.getTargetOption("axlpassword"));
				
				Variables.setAXLConnectionToCUCMV105(axlPort);
				}
			
			Variables.getLogger().debug("WSDL Initialization done");
			
			/**
			 * We now check if the CUCM is reachable by asking him its version
			 */
			SimpleRequest.getCUCMVersion();
			Variables.setCUCMReachable(true);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Error while initializing CUCM connection : "+e.getMessage(),e);
			Variables.setCUCMReachable(false);
			throw e;
			}
		}
	
	/**
	 * Method used when the application failed to 
	 * initialize
	 */
	public static void failedToInit(Exception exc)
		{
		exc.printStackTrace();
		JOptionPane.showMessageDialog(null,"Application failed to init :\r\n"+exc.getMessage()+"\r\nThe software will now exit","ERROR",JOptionPane.ERROR_MESSAGE);
		Variables.getLogger().error(exc.getMessage());
		Variables.getLogger().error("Application failed to init : System.exit(0)");
		System.exit(0);
		}
	
	/**
	 * Initialization of the internal variables from
	 * what we read in the configuration file
	 * @throws Exception 
	 */
	public static void initInternalVariables() throws Exception
		{
		/***********
		 * Logger
		 */
		String level = UsefulMethod.getTargetOption("log4j");
		if(level.compareTo("DEBUG")==0)
			{
			Variables.getLogger().setLevel(Level.DEBUG);
			}
		else if (level.compareTo("INFO")==0)
			{
			Variables.getLogger().setLevel(Level.INFO);
			}
		else if (level.compareTo("ERROR")==0)
			{
			Variables.getLogger().setLevel(Level.ERROR);
			}
		else
			{
			//Default level is INFO
			Variables.getLogger().setLevel(Level.INFO);
			}
		Variables.getLogger().info("Log level found in the configuration file : "+Variables.getLogger().getLevel().toString());
		/*************/
		
		/***********
		 * Advanced log
		 */
		Variables.setAdvancedLogs(Boolean.parseBoolean(UsefulMethod.getTargetOption("advancedcalllogs")));
		/************/
		
		/************
		 * Etc...
		 */
		//If needed, just write it here
		/*************/
		}
	
	
	/**
	 * Method which convert a string into cucmAXLVersion
	 */
	public static cucmAXLVersion convertStringToCUCMAXLVersion(String version)
		{
		if(version.contains("105"))
			{
			return cucmAXLVersion.version105;
			}
		else if(version.contains("110"))
			{
			return cucmAXLVersion.version110;
			}
		else if(version.contains("115"))
			{
			return cucmAXLVersion.version115;
			}
		else if(version.contains("120"))
			{
			return cucmAXLVersion.version120;
			}
		else if(version.contains("125"))
			{
			return cucmAXLVersion.version125;
			}
		else
			{
			return cucmAXLVersion.version105;
			}
		}
	
	
	/**************
	 * Method aims to get a template item value by giving its name
	 * @throws Exception 
	 *************/
	public static String getItemByName(String name, String[][] itemDetails) throws Exception
		{
		for(int i=0; i<itemDetails.length; i++)
			{
			if(itemDetails[i][0].equals(name))
				{
				return itemDetails[i][1];
				}
			}
		//throw new Exception("Item not found : "+name);
		Variables.getLogger().debug("Item not found : "+name);
		return "";
		}
	
	/**********************************************************
	 * Method used to disable security in order to accept any
	 * certificate without trusting it
	 */
	public static void disableSecurity()
		{
		try
        	{
            X509TrustManager xtm = new HttpsTrustManager();
            TrustManager[] mytm = { xtm };
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, mytm, null);
            SSLSocketFactory sf = ctx.getSocketFactory();

            HttpsURLConnection.setDefaultSSLSocketFactory(sf);
            
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
            	{
                public boolean verify(String hostname, SSLSession session)
                	{
                    return true;
                	}
            	}
            );
        	}
        catch (Exception e)
        	{
            e.printStackTrace();
        	}
		}
	
	
	
	/********************************************
	 * Method used to init the class eMailsender
	 * @throws Exception 
	 ********************************************/
	public synchronized static void initEMailServer() throws Exception
		{
		Variables.seteMSender(new eMailSender(UsefulMethod.getTargetOption("smtpemailport"),
				 UsefulMethod.getTargetOption("smtpemailprotocol"),
				 UsefulMethod.getTargetOption("smtpemailserver"),
				 UsefulMethod.getTargetOption("smtpemail"),
				 UsefulMethod.getTargetOption("smtpemailpassword")));
		}
	
	/**
	 * Method used to send an email to the admin group
	 */
	public synchronized static void sendEmailToTheAdminList(String desc, String siteName, String content)
		{
		try
			{
			String adminEmails = UsefulMethod.getTargetOption("smtpemailadmin");
			
			String subject = LanguageManagement.getString("emailreportsubject")+siteName;
			String eMailDesc = desc+" - "+siteName;
			
			if(adminEmails.contains(";"))
				{
				//There are many emails to send
				String[] adminList = adminEmails.split(";");
				for(String s : adminList)
					{
					Variables.geteMSender().send(s,
							subject,
							content,
							eMailDesc);
					}
				}
			else
				{
				//There is only one email to send
				Variables.geteMSender().send(adminEmails,
						subject,
						content,
						eMailDesc);
				}
			
			JOptionPane.showMessageDialog(null,LanguageManagement.getString("emailsentsuccess"),"",JOptionPane.INFORMATION_MESSAGE);
			}
		catch (Exception e)
			{
			e.printStackTrace();
			Variables.getLogger().error("",e);
			Variables.getLogger().error("Failed to send emails to the admin list : "+e.getMessage());
			}
		}
	
	
	/**
	 * Method used to find the file name from a file path
	 * For intance :
	 * C:\JAVAELILEX\YUZA\Maquette_CNAF_TA_FichierCollecteDonneesTelephonie_v1.7_mac.xls
	 * gives :
	 * Maquette_CNAF_TA_FichierCollecteDonneesTelephonie_v1.7_mac.xls
	 */
	public static String extractFileName(String fullFilePath)
		{
		String[] tab =  fullFilePath.split("\\\\");
		return tab[tab.length-1];
		}
	
	/***
	 * Method used to get the AXL version from the CUCM
	 * We contact the CUCM using a very basic request and therefore get the version
	 * @throws Exception 
	 */
	public static cucmAXLVersion getAXLVersionFromTheCUCM() throws Exception
		{
		/**
		 * In this method version we just read the version from the configuration file
		 * This has to be improved to match the method description
		 **/
		cucmAXLVersion AXLVersion;
		
		AXLVersion = UsefulMethod.convertStringToCUCMAXLVersion("version"+getTargetOption("axlversion"));
		
		return AXLVersion;
		}
	
	/**
	 * Methos used to check if a value is null or empty
	 */
	public static boolean isNotEmpty(String s)
		{
		if((s == null) || (s.equals("")))
			{
			return false;
			}
		else
			{
			return true;
			}
		}
	
	/**
	 * Methos used to check if a value is null or empty
	 */
	public static boolean isNotEmpty(ArrayList<String> as)
		{
		if((as == null) || (as.size() == 0))
			{
			return false;
			}
		else
			{
			return true;
			}
		}
	
	/******
	 * Used to convert itemType values into more verbose ones
	 * @throws Exception 
	 */
	public static String convertItemTypeToVerbose(itemType type) throws Exception
		{
		switch(type)
			{
			case phone:return "Phone";
			case udp:return "User device profile";
			case user:return "End User";
			case line:return "Line";
			}
		
		throw new Exception("Item type not found");
		}
	
	
	/******
	 * Method used to determine if the fault description means
	 * that the item was not found or something else
	 * If it is not found we return true
	 * For any other reason we return false
	 * @param faultDesc
	 * @return
	 */
	public static boolean itemNotFoundInTheCUCM(String faultDesc)
		{
		ArrayList<String> faultDescList = new ArrayList<String>();
		faultDescList.add("was not found");
		for(String s : faultDescList)
			{
			if(faultDesc.contains(s))return true;
			}
		
		return false;
		}
	
	/**
	 * Method used to initialize user list
	 * @throws Exception 
	 */
	public static ArrayList<User> initUserList(String fileName) throws Exception
		{
		ArrayList<User> myList = new ArrayList<User>();
		ArrayList<String[][]> myTempList;
		
		myTempList = readUserFile(fileName);
		
		for(String[][] sTab: myTempList)
			{
			myList.add(new User(UsefulMethod.getItemByName("firstname", sTab),
					UsefulMethod.getItemByName("lastname", sTab),
					UsefulMethod.getItemByName("extension", sTab),
					UsefulMethod.getItemByName("email", sTab),
					UsefulMethod.getItemByName("cucmid", sTab),
					UsefulMethod.getItemByName("salesforceid", sTab),
					Boolean.parseBoolean(UsefulMethod.getItemByName("incomingcallpopup", sTab)),
					Boolean.parseBoolean(UsefulMethod.getItemByName("reverselookup", sTab)),
					Boolean.parseBoolean(UsefulMethod.getItemByName("emailreminder", sTab))));
			}
		
		return myList;
		}
	
	
	/**
	 * Method used to read the user file
	 */
	private static ArrayList<String[][]> readUserFile(String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			file = xMLReader.fileRead(".\\"+fileName);
			
			listParams.add("users");
			listParams.add("user");
			return xMLGear.getResultListTab(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the file : "+fileName+" : "+exc.getMessage());
			}
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}

