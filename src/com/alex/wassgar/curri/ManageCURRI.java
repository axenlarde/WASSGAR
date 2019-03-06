package com.alex.wassgar.curri;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.Variables.callType;

/**
 * Class used to store static method about CURRI
 * 
 * @author Alexandre
 */
public class ManageCURRI
	{
	
	/**
	 * Used to parse CURRI request
	 */
	public static CURRIRequest parseCURRI(String xmlContent) throws Exception
		{
		CURRIRequest r = new CURRIRequest(callType.incoming);
		
		Variables.getLogger().debug("Starting parsing");
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(new StringReader(xmlContent)));
		
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("Attribute");
		
		for(int i=0; i<nList.getLength(); i++)
			{
			Node n = nList.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE)
				{
				Element e = (Element)n;
				
				String[] tab = e.getAttribute("AttributeId").split(":");
				String attributeName = tab[tab.length-1];//The attribute name is the last entry
				
				if(attributeName.equals("callingnumber"))
					{
					r.setCallingNumber(e.getElementsByTagName("AttributeValue").item(0).getTextContent());
					}
				else if(attributeName.equals("callednumber"))
					{
					r.setCalledNumber(e.getElementsByTagName("AttributeValue").item(0).getTextContent());
					}
				}
			}
		
		Variables.getLogger().debug("CURRI Request Parsed : Calling : "+r.getCallingNumber()+" Called : "+r.getCalledNumber());
		
		return r;
		}
	
	

	}

/*2019*//*RATEL Alexandre 8)*/