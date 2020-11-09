# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

package com.catgen;

import org.w3c.dom.Element;

import com.catgen.factories.NetMarketFactory;

public class NetworkMarket extends BasicDatastore {
	public String NetworkMarketID;
	public String Name;
	public String Description;
	public String HomePageDescription;
	public String LogoImage;
	public String Address;
	public String City;
	public String State;
	public String Zip;
	public String Contact;
	public String ContactEmail;
	public String PayPalEmail;
	public String GoogleMerchantID;
	public String TwoCOSID;
	public String Header;
	public String Footer;
	public String HeaderURL;
	public String FooterURL;
	public String CSS;
	public String Currency;
	public String InquiryURL;
	public String ThumbnailSize;
	public String TemplateWidth;
	public String Template;
	public String Font;
	public String FontSize;
	public String Color;
	public String SecondaryColor;
	public String TetriaryColor;
	public String Background;
	public String Background2;
	public String Background3;
	public String BackgroundImage;
	public String ProductImageSize;
	public String DomainName;
	public String MarketsURL;
	public String MarketInfoURL;
	public String PagesURL;

	public void Load() 
	{
		NetMarketFactory.Load( this );
	}

	public void Save() 
	{
		NetMarketFactory.Save( this );
	}
	
	public void AddToXml(Element element)
	{
		Element e;
		
		if(Name != null)
		{
			e = element.getOwnerDocument().createElement("CompanyName");
			e.appendChild(element.getOwnerDocument().createTextNode(Name));
			element.appendChild(e);
		}

		if(Description != null)
		{
			e = element.getOwnerDocument().createElement("Description");
			e.appendChild(element.getOwnerDocument().createTextNode(Description));
			element.appendChild(e);
		}

		if(LogoImage != null)
		{
			e = element.getOwnerDocument().createElement("LogoImage");
			e.appendChild(element.getOwnerDocument().createTextNode(LogoImage));
			element.appendChild(e);
		}

		if(NetworkMarketID != null)
		{
			e = element.getOwnerDocument().createElement("NetworkMarketID");
			e.appendChild(element.getOwnerDocument().createTextNode(NetworkMarketID));
			element.appendChild(e);
		}

		if(Currency != null)
		{
			e = element.getOwnerDocument().createElement("Currency");
			e.appendChild(element.getOwnerDocument().createTextNode(Currency));
			element.appendChild(e);
		}
	}
}
