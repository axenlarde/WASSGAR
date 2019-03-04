package com.alex.wassgar.server;

import java.io.Serializable;

import com.alex.wassgar.utils.Variables.requestType;

/**
 * Represent a simple request
 */
public class Request implements Serializable
	{
	/**
	 * Variables
	 */
	requestType type;
	String content;
	
	public Request(requestType type, String content)
		{
		super();
		this.type = type;
		this.content = content;
		}
	
	public requestType getType()
		{
		return type;
		}
	public String getContent()
		{
		return content;
		}
	
	}

/*2019*//*RATEL Alexandre 8)*/