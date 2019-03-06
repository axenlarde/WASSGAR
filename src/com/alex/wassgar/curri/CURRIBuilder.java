package com.alex.wassgar.curri;

import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.Variables.callType;

/**
 * Static classes used to build CURRI Request
 * 
 * @author Alexandre
 */
public class CURRIBuilder
	{
	
	/**
	 * To build accept request
	 */
	public static CURRIRequest buildAccept()
		{
		Variables.getLogger().debug("CURRI : Building ACCEPT request");
		
		CURRIRequest r = new CURRIRequest(callType.incoming);
		String content = "<?xml encoding=\"UTF-8\" version=\"1.0\"?> \r\n" + 
				"<Response>\r\n" + 
				"	<Result>\r\n" + 
				"		<Decision>Permit</Decision>\r\n" + 
				"		<Obligations>\r\n" + 
				"			<Obligation FulfillOn=\"Permit\" obligationId=\"continue.simple\">\r\n" + 
				"				<AttributeAssignment AttributeId=\"Policy:continue.simple\">\r\n" + 
				"					<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">\r\n" + 
				"						&lt;cixml version=\"1.0&gt;\r\n" + 
				"							&lt;continue&gt;&lt;/continue&gt;\r\n" + 
				"						&lt;/cixml&gt;\r\n" + 
				"					</AttributeValue>\r\n" + 
				"				</AttributeAssignment>\r\n" + 
				"			</Obligation>\r\n" + 
				"		</Obligations>\r\n" + 
				"	</Result>\r\n" + 
				"</Response>";
		
		r.setContent(content);
		return r;
		}
	
	/**
	 * To build accept and modify alerting name request
	 */
	public static CURRIRequest buildAcceptAndDisplayAN(String callingNumber, String calledNumber, String alertingName, callType type)
		{
		Variables.getLogger().debug("CURRI : Building ACCEPT & MODIFY request");
		
		CURRIRequest r = new CURRIRequest(callingNumber, calledNumber, alertingName, null, type);
		String content = "<?xml encoding=\"UTF-8\" version=\"1.0\"?> \r\n" + 
				"<Response>\r\n" + 
				"	<Result>\r\n" + 
				"	<Decision>Permit</Decision>\r\n" + 
				"		<Obligations>\r\n" + 
				"			<Obligation FulfillOn=\"Permit\" obligationId=\"continue.simple\">\r\n" + 
				"				<AttributeAssignment AttributeId=\"Policy:continue.simple\">\r\n" + 
				"					<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">\r\n" + 
				"						&lt;cixml version=\"1.0\"&gt;\r\n" + 
				"							&lt;continue&gt;\r\n" + 
				"								&lt;modify callingname=\""+alertingName+"\"/&gt;\r\n" + 
				"							&lt;/continue&gt;\r\n" + 
				"						&lt;/cixml&gt;\r\n" + 
				"					</AttributeValue>\r\n" + 
				"				</AttributeAssignment>\r\n" + 
				"			</Obligation>\r\n" + 
				"		</Obligations>\r\n" + 
				"	</Result>\r\n" + 
				"</Response>";
		
		r.setContent(content);
		return r;
		}
	
	/**
	 * To build deny request
	 */
	public static CURRIRequest buildDeny()
		{
		Variables.getLogger().debug("CURRI : Building DENY request");
		//To be written if necessary
		
		return null;
		}
	
	
	}

/*2019*//*RATEL Alexandre 8)*/