package com.alex.wassgar.curri;

import com.alex.wassgar.utils.Variables.callType;

/**
 * Class used to store a CURRI request
 * 
 * @author Alexandre
 */
public class CURRIRequest
	{
	/**
	 * Variables
	 */
	private String callingNumber, calledNumber, alertingName;
	private String content;
	private callType type;
	
	public CURRIRequest(callType type)
		{
		this.type = type;
		}
	
	public CURRIRequest(String callingNumber, String calledNumber, String alertingName, String content, callType type)
		{
		this.callingNumber = callingNumber;
		this.calledNumber = calledNumber;
		this.alertingName = alertingName;
		this.content = content;
		this.type = type;
		}

	public String getCallingNumber()
		{
		return callingNumber;
		}

	public void setCallingNumber(String callingNumber)
		{
		this.callingNumber = callingNumber;
		}

	public String getCalledNumber()
		{
		return calledNumber;
		}

	public void setCalledNumber(String calledNumber)
		{
		this.calledNumber = calledNumber;
		}

	public String getAlertingName()
		{
		return alertingName;
		}

	public void setAlertingName(String alertingName)
		{
		this.alertingName = alertingName;
		}

	public String getContent()
		{
		return content;
		}

	public void setContent(String content)
		{
		this.content = content;
		}

	public callType getType()
		{
		return type;
		}

	public void setType(callType type)
		{
		this.type = type;
		}

	
	}

/*2019*//*RATEL Alexandre 8)*/