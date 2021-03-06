package com.alex.wassgar.main;

import org.apache.log4j.Level;

import com.alex.wassgar.action.Action;
import com.alex.wassgar.utils.InitLogging;
import com.alex.wassgar.utils.LanguageManagement;
import com.alex.wassgar.utils.UsefulMethod;
import com.alex.wassgar.utils.Variables;


/**********************************
 * WASSGAR Main Class 14/02/2019
 * 
 * @author RATEL Alexandre
 **********************************/
public class Main
	{
	/**
	 * Variables
	 */
	
	
	/***************
	 * Constructor
	 ***************/
	public Main()
		{
		//Set the software name
		Variables.setSoftwareName("WASSGAR");
		//Set the software version
		Variables.setSoftwareVersion("1.0");
		
		/****************
		 * Initialization of the logging
		 */
		Variables.setLogger(InitLogging.init(Variables.getSoftwareName()+"_LogFile.txt"));
		Variables.getLogger().info("\r\n");
		Variables.getLogger().info("#### Entering application");
		Variables.getLogger().info("## Welcome to : "+Variables.getSoftwareName()+" version "+Variables.getSoftwareVersion());
		Variables.getLogger().info("## Author : RATEL Alexandre : 2019");
		/*******/
		
		/******
		 * Initialization of the variables
		 */
		new Variables();
		/************/
		
		/**********
		 * We check if the java version is compatible
		 */
		//UsefulMethod.checkJavaVersion();
		/***************/
		
		/**********************
		 * Reading of the configuration file
		 */
		try
			{
			//Config files reading
			Variables.setMainConfig(UsefulMethod.readMainConfigFile(Variables.getConfigFileName()));
			}
		catch(Exception exc)
			{
			UsefulMethod.failedToInit(exc);
			}
		/********************/
		
		/*****************
		 * Setting of the inside variables from what we read in the configuration file
		 */
		try
			{
			UsefulMethod.initInternalVariables();
			}
		catch(Exception exc)
			{
			Variables.getLogger().error(exc.getMessage());
			Variables.getLogger().setLevel(Level.INFO);
			}
		/*********************/
		
		/****************
		 * Init email server
		 */
		try
			{
			UsefulMethod.initEMailServer();
			}
		catch (Exception e)
			{
			e.printStackTrace();
			Variables.getLogger().error("Failed to init the eMail server : "+e.getMessage());
			}
		/*************/
		
		/****************
		 * Initialization of the call logging
		 */
		Variables.setCallLogger(InitLogging.infoModeInit(Variables.getSoftwareName()+"_CallLogs.txt"));
		Variables.getCallLogger().setLevel(Level.INFO);
		/*************/
		
		/*******************
		 * Start main interface
		 */
		try
			{
			Variables.getLogger().info("End init, Launching main process");
			new Action();
			}
		catch (Exception exc)
			{
			UsefulMethod.failedToInit(exc);
			}
		/******************/
		
		//End of the main class
		}
	
	public static void main(String[] args)
		{
		new Main();
		}

	/*2019*//*RATEL Alexandre 8)*/
	}
