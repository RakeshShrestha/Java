package com.catgen;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

public class Utils 
{
	public static String getReferralURL(String relativePath, String referralID, String companyCode, String type, String url, String extraData) throws UnsupportedEncodingException
	{
		if(referralID != null && referralID.trim().length() > 0 && url != null && url.toLowerCase().startsWith("http") )
			return "" + relativePath + "ReferralEvent.html?refid=" + URLEncoder.encode(referralID, "UTF-8") + "&companycode=" + URLEncoder.encode(companyCode, "UTF-8") + "&type=" + URLEncoder.encode(type, "UTF-8") + "&url=" + URLEncoder.encode(url, "UTF-8") + "&extra=" + extraData;
		else
			return url;
	}
	
	public static String getCookieValue(String cookieName, HttpServletRequest request)
	{
		Cookie[] cookies = request.getCookies();

		if(cookies != null)
		{
			for(int i = 0; i < cookies.length; i++) { 
				Cookie c = cookies[i];
				if (c != null && "refid".equals(c.getName())) 
					return c.getValue();
			}
		}
		
		return null;
	}
	
	public static void SetCookieValue(String name, String value, HttpServletResponse response)
	{
		Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(-1); //7*24*60*60);
        cookie.setPath("/");
        
		response.addCookie(cookie);
	}
	
	public static String getSafeString(String s)
	{
		if(s == null)
			return "";
		else
			return s;
	}
	
	public static int StrToIntDef(String s, int d)
	{
		int result = d;
		try
		{
			result = Integer.parseInt(s);
		}
		catch (Exception e) {
		}
		
		return result;		
	}
	
	public static String getHtmlEscape(String s)
	{
		if(s == null)
			return "";
		else
			return StringEscapeUtils.escapeHtml( s );
	}

	public static String getHtmlEscapeWithBR(String s)
	{
		String r = s;
		if(r == null)
			r = "";
		else
			r = StringEscapeUtils.escapeHtml( s );
		
		return r.replace("\r", "").replace("\n", "<br/>");
	}
	
	public static String StripHtmlBody(String html)
	{
		String result = null;
		if(html != null)
		{
			Pattern p = Pattern.compile("^.*<body[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(html);
			
			result = m.replaceFirst("");
			
			p = Pattern.compile("</body[^>]*>.*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			m = p.matcher(result);
			result = m.replaceFirst("").replace("\u00A0", " ");
		}
		
		return result;		
	}

	public static String StripGoogleSites(String html)
	{
		String result = null;
		if(html != null)
		{
			Pattern p = Pattern.compile("^.*<table[^>]*jot-content-table[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(html);
			
			result = m.replaceFirst("<table>");
			
			p = Pattern.compile("<div[^>]*goog-ws-bottom[^>]*>.*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			m = p.matcher(result);
			result = m.replaceFirst("").replace("\u00A0", " ");
		}
		
		return result;		
	}

	public static String StripGoogleDocs(String html)
	{
		String result = null;
		if(html != null)
		{
			Pattern p = Pattern.compile("^.*<div[^>]*doc-contents[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(html);
			
			result = m.replaceFirst("<div id=\"doc-contents\">");
			
			p = Pattern.compile("<div[^>]*google-view-footer[^>]*>.*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			m = p.matcher(result);
			result = m.replaceFirst("");
			
			result = result.replace("src=\"File?", "src=\"http://docs.google.com/File?").replace("\u00A0", " ");
		}
		
		return result;		
	}
	
	public static String getProductURL(Company company, Product product, String relativePath, String referralID) throws UnsupportedEncodingException
	{
		String productURL = "";
	
		if(product.URL != null && product.URL.length() > 0)
			productURL = product.URL.replace( "$$productcode$$", product.Code );
		else if(company.ProductURL != null && company.ProductURL.length() > 0)
			productURL = company.ProductURL.replace( "$$productcode$$", product.Code );
		else
			productURL = "" + relativePath + URLEncoder.encode(product.CompanyCode, "UTF-8") + "/Product.html?productid=" + URLEncoder.encode(product.Code, "UTF-8");

		if(referralID != null && referralID.length() > 0)
		{
			if(productURL.indexOf("?") > 0)
				productURL = productURL.replaceFirst("\\?", "?refid=" + URLEncoder.encode(referralID, "UTF-8") + "&");
			else
				productURL += "?refid=" + URLEncoder.encode(referralID, "UTF-8");
		}
		
		return productURL;
	}

	public static String getCompanyURL(Company company, String relativePath, String referralID) throws UnsupportedEncodingException
	{
		String companyURL = "";
	
		if(company.CompanyURL != null && company.CompanyURL.length() > 0)
			companyURL = company.CompanyURL;
		else
			companyURL = "" + relativePath + URLEncoder.encode(company.Code, "UTF-8" ) + "/Member.html";

		if(referralID != null && referralID.length() > 0)
		{
			if(companyURL.indexOf("?") > 0)
				companyURL = companyURL.replaceFirst("\\?", "?refid=" + URLEncoder.encode(referralID, "UTF-8") + "&");
			else
				companyURL += "?refid=" + URLEncoder.encode(referralID, "UTF-8");
		}
		
		return companyURL;
	}
	
	public static String getCurrencySymbol(Company company, Product product) 
	{
		String currency = product.Currency;
		if(currency == null || currency.trim().length() == 0)
		{
			currency = company.CurrencySymbol;
			if(currency == null || currency.trim().length() == 0)
				currency = company.Currency;
		}
		
		if(currency == null)
			currency = "";
		
		return currency;
	}
}
