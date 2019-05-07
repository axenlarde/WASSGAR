package com.alex.wassgar.utils;

import java.util.regex.Pattern;

/**
 * Used to manipulate phone number
 * 
 * @author Alexandre
 */
public class ExtensionManipulation
	{
	
	public static String validate(String extension)
		{
		if(extension.startsWith("+33"))
			{
			return extension.substring(3, extension.length());
			}
		else if(extension.startsWith("00"))
			{
			return extension.substring(1, extension.length());
			}
		else if(extension.startsWith("+"))
			{
			//We add a \ before sending to salesforce
			return "\\"+extension;
			}
		
		
		return extension;
		}
	
	/**
	 * To check if we have to resolve this extension
	 */
	public static boolean goodToGo(String extension)
		{
		try
			{
			if(Pattern.matches(UsefulMethod.getTargetOption("internalnumberpattern"), extension))
				{
				Variables.getLogger().debug("Extension "+extension+" match the internal pattern number : We will not resolve it");
				return false;
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : Unable to check the extension "+extension+" : "+e.getMessage(),e);
			}
		
		return true;
		}
	}

/*2019*//*RATEL Alexandre 8)*/