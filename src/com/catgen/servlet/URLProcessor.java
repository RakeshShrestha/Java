package com.catgen.servlet;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.text.*;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.http.*;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import  org.w3c.dom.ls.LSSerializer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;

//FOP
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;


import com.catgen.CatgenPageContext;
import com.catgen.MasterMarket;
import com.catgen.Utils;
import com.catgen.factories.*;
import com.catgen.xml.DOMErrors;
import com.catgen.xml.NetworkMarketXml;

public class URLProcessor extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Process(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Process(request, response);
	}

	private void Process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		CatgenPageContext catgenPageContext = new CatgenPageContext();

		catgenPageContext.HostName = request.getServerName();
		if(catgenPageContext.HostName == null)
			catgenPageContext.HostName = "";
		
		catgenPageContext.HostName = catgenPageContext.HostName.toLowerCase();
		if(catgenPageContext.HostName.startsWith("www."))
			catgenPageContext.HostName = catgenPageContext.HostName.substring(4);
		
		catgenPageContext.ProductID = request.getParameter("productid");
		catgenPageContext.ProductLineID = request.getParameter("productlineid");

		catgenPageContext.PageID = request.getParameter("pageid");
		catgenPageContext.PageSize = Utils.StrToIntDef( request.getParameter("pagesize"), 0 );
		catgenPageContext.PageNumber = Utils.StrToIntDef( request.getParameter("pagenumber"), 0 );
		catgenPageContext.Search = request.getParameter("search");
		catgenPageContext.Template = request.getParameter("template");
		catgenPageContext.RelativePath = "";

		catgenPageContext.OriginalReferralID = request.getParameter("refid");
		
		if(catgenPageContext.OriginalReferralID == null || catgenPageContext.OriginalReferralID.trim().length() == 0)
			catgenPageContext.OriginalReferralID =  Utils.getCookieValue( "refid", request);

		String path = request.getServletPath();

		String servetURL = null;

		try 
		{
			Connection conn = MySqlDB.getDBConnection(getServletContext());
			try
			{
				if(path == null)
				{
					response.sendRedirect("NotFound.html");
					return;
				}
				
				MasterMarket masterMarket = MasterMarketFactory.getMasterMarketByDomainName(conn, catgenPageContext.HostName );
				
				if(masterMarket != null)
					catgenPageContext.NetworkMarket = NetMarketFactory.getNetworkMarketByCode(conn, masterMarket.NetworkMarketID );  				

				if(path.startsWith("/"))
					path = path.substring( 1 );

				if(path.indexOf("/") < 0 && catgenPageContext.NetworkMarket == null)
				{
					String queryString = request.getQueryString();

					String url = request.getRequestURL() + "/";
					if(queryString != null && queryString.length() > 0)
						url = url + "?" + queryString;

					response.sendRedirect(url);
					return;
				}

				String[] pathNames;
				if(path.length() == 0)
					pathNames = new String[0];
				else
					pathNames = path.split("/");
				
				int companyIndex;
				
				if( catgenPageContext.NetworkMarket == null )
					companyIndex = 1;
				else
					companyIndex = 0;

				for(int i = 0; i < pathNames.length - 1 - companyIndex; i++)
				{
					catgenPageContext.RelativePath += "../";
				}

				if( catgenPageContext.NetworkMarket == null && pathNames.length > 0)
					catgenPageContext.NetworkMarket = NetMarketFactory.getNetworkMarketByCode(conn, pathNames[0] );

				if(catgenPageContext.NetworkMarket == null)
				{
					response.sendRedirect("NotFound.html");
					return;
				}
				
				String networkMarketID = catgenPageContext.NetworkMarket.NetworkMarketID;
				
				if(catgenPageContext.OriginalReferralID == null || catgenPageContext.OriginalReferralID.length() == 0)
					catgenPageContext.ReferralID = networkMarketID;
				else if(! catgenPageContext.OriginalReferralID.startsWith(networkMarketID))
				{
					int c = catgenPageContext.OriginalReferralID.indexOf(',');
					if(c > 0)
						catgenPageContext.ReferralID = catgenPageContext.OriginalReferralID.substring(0, c);
					else
						catgenPageContext.ReferralID = catgenPageContext.OriginalReferralID;
						
					catgenPageContext.ReferralID = catgenPageContext.NetworkMarket.NetworkMarketID + "," + catgenPageContext.ReferralID;
				}
				else
					catgenPageContext.ReferralID = catgenPageContext.OriginalReferralID;					

				if(pathNames.length > companyIndex)
				{
					if(pathNames[companyIndex] != null && pathNames[companyIndex].toLowerCase().startsWith("referralevent"))
					{
						String redirectURL = request.getParameter("url");
						if(redirectURL != null && redirectURL.trim().length() > 0)
						{
							if(catgenPageContext.ReferralID != null && catgenPageContext.ReferralID.trim().length() > 0)
							{
								String clientIP = request.getRemoteAddr();
								String forwarded = request.getHeader("x-forwarded-for");
								if( forwarded != null && forwarded.trim().length() > 0)
									clientIP += " XFF: " + forwarded;
								
								ReferralFactory.SaveReferral(conn, catgenPageContext.NetworkMarket.NetworkMarketID,  request.getParameter("companycode"), catgenPageContext.ReferralID, request.getParameter("type"), clientIP, request.getParameter("extra"));
								
								response.sendRedirect(redirectURL);
								return;
							}
						}
					}
					
					catgenPageContext.Company = CompanyFactory.getCompanyByCode(conn, catgenPageContext.NetworkMarket.NetworkMarketID, pathNames[companyIndex]);
					if(catgenPageContext.Company != null )
					{
						if( pathNames.length > companyIndex + 1)
						{
							String p = URLDecoder.decode( pathNames[companyIndex + 1], "UTF-8" );
							
							if(p.matches("^[Pp]roduct[Ll]ine.*"))
							{
								catgenPageContext.ProductLine = ProductLineFactory.getProductLineByCode(conn, catgenPageContext.NetworkMarket.NetworkMarketID, catgenPageContext.Company.Code, catgenPageContext.ProductLineID);
								if(catgenPageContext.ProductLine == null)
								{
									catgenPageContext.ProductLineID = null;
									catgenPageContext.ProductLine = ProductLineFactory.getProductLineByName(conn, catgenPageContext.NetworkMarket.NetworkMarketID, catgenPageContext.Company.Code, request.getParameter("productlinename"));
								}
								
								if(catgenPageContext.ProductLine != null)
								{
									servetURL = "ProductLine";
									catgenPageContext.PageName = catgenPageContext.ProductLine.Name;
								}
							}
							else if(p.matches("^[Pp]roducts.*"))
							{
								servetURL = "Products";
								catgenPageContext.PageName = "Products";
							}
							else if(p.matches("^[Pp]roduct.*"))
							{
								catgenPageContext.Product = ProductFactory.getProductByCode(conn, catgenPageContext.NetworkMarket.NetworkMarketID, catgenPageContext.Company.Code, catgenPageContext.ProductID);
								if(catgenPageContext.Product == null)
								{
									catgenPageContext.ProductID = null;
									catgenPageContext.Product = ProductFactory.getProductByName(conn, catgenPageContext.NetworkMarket.NetworkMarketID, catgenPageContext.Company.Code, request.getParameter("productname"));
								}
								
								if(catgenPageContext.Product != null)
								{
									servetURL = "Product";
									catgenPageContext.PageName = catgenPageContext.Product.Name;
								}
							}
							else if(p.matches("^[Rr]eport.*"))
								servetURL = "Report";
							else if(p.matches("^[Mm]ember.*"))
								servetURL = "Member";
							else
							{
								catgenPageContext.ProductLine = ProductLineFactory.getProductLineByName(conn, catgenPageContext.NetworkMarket.NetworkMarketID, catgenPageContext.Company.Code, p);
								if(catgenPageContext.ProductLine != null)
								{
									servetURL = "ProductLine";
									catgenPageContext.ProductLine = ProductLineFactory.getProductLineByName(conn, catgenPageContext.NetworkMarket.NetworkMarketID, catgenPageContext.Company.Code, p);
									catgenPageContext.PageName = p;
								}
								else 
								{
									catgenPageContext.Product = ProductFactory.getProductByName(conn, catgenPageContext.NetworkMarket.NetworkMarketID, catgenPageContext.Company.Code, p);
									if(catgenPageContext.Product != null)
									{
										servetURL = "Product";
										catgenPageContext.PageName = p;
									}
								}
							}
						}
						else
						{
							servetURL = "Member";
							catgenPageContext.PageName = "Member"; 
						}
					}
					else
					{
						String p = URLDecoder.decode( pathNames[companyIndex], "UTF-8" );
						if(p.matches("^[Cc]ompany.*"))
						{
							servetURL = "Company";
							catgenPageContext.PageName = "Company";
						}
						else if(p.matches("^[Hh]ome.*"))
						{
							servetURL = "Home";
							catgenPageContext.PageName = "Home";
						}
						else if(p.matches("^[Rr]eport.*"))
						{
							servetURL = "Report";
							catgenPageContext.PageName = "Report";
						}
						else if(p.matches("^[Mm]embers.*") || p.matches("^[Vv]endors.*") )
						{
							servetURL = "Members";
							catgenPageContext.PageName = "Vendors";
						}
						else if(p.matches("^[Ss]earch\\..*"))
						{
							servetURL = "Search";
							catgenPageContext.PageName = "Search";
						}
						else if(p.matches("^[Pp]age\\..*"))
						{
							if(catgenPageContext.PageID == null || catgenPageContext.PageID.trim().length() == 0)
								catgenPageContext.PageID = request.getParameter("page");
							
							catgenPageContext.Page = PageFactory.getPageByName(conn, catgenPageContext.NetworkMarket.NetworkMarketID, catgenPageContext.PageID);
							if(catgenPageContext.Page != null)
							{
								servetURL = "Page";
								catgenPageContext.PageName = catgenPageContext.Page.Name; 
							}
						}						
						else if(p.matches("^[Vv]iew[Xx]ml.*"))
						{
							ViewXml(catgenPageContext.NetworkMarket.NetworkMarketID, response);
							return;
						}
						else if(p.matches("^[Vv]iew[Hh]tml.*"))
						{
							String requestUrl = request.getRequestURL().toString();
							String servletPath = request.getServletPath();

							response.reset();
							response.setContentType("text/html");

							ViewHtml(catgenPageContext.NetworkMarket.NetworkMarketID, requestUrl.substring( 0, requestUrl.length() - servletPath.length()) + "/" + request.getParameter("template"), response);
							return;
						}
						else if(p.matches("^[Vv]iew[Pp][Dd][Ff].*"))
						{
							String requestUrl = request.getRequestURL().toString();
							String servletPath = request.getServletPath();

							response.reset();
							response.setContentType("application/pdf");

							ViewPDF(catgenPageContext.NetworkMarket.NetworkMarketID, requestUrl.substring( 0, requestUrl.length() - servletPath.length()) + "/" + request.getParameter("template"), response, false);

							return;
						}
						else if(p.matches("^[Vv]iew_[Pp][Dd][Ff].*"))
						{
							String requestUrl = request.getRequestURL().toString();
							String servletPath = request.getServletPath();

							response.reset();
							response.setContentType("text/xml");

							DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
							response.setHeader("Expires", df.format(Calendar.getInstance().getTime() ));

							ViewPDF(catgenPageContext.NetworkMarket.NetworkMarketID, requestUrl.substring( 0, requestUrl.length() - servletPath.length()) + "/" + request.getParameter("template"), response, true);

							return;
						}
						else
						{
							catgenPageContext.Page = PageFactory.getPageByName(conn, catgenPageContext.NetworkMarket.NetworkMarketID, p);
							if(catgenPageContext.Page != null)
							{
								servetURL = "Page";
								catgenPageContext.PageName = catgenPageContext.Page.Name;
							}
						}
					}
				}
				else
				{
					servetURL = "Home";
					catgenPageContext.PageName = "Home";
				}

				/*response.setContentType("text/html");
				PrintWriter out = response.getWriter();

				out.println( "sp: " + request.getServletPath() + "<bR>" );
				out.println( "mid: " + marketID + "<bR>" );
				out.println( "cc: " + companyCode + "<bR>" );
				out.println( "jsp: " + servetURL + "<bR>" );*/

				if((catgenPageContext.Template == null || catgenPageContext.Template.length() == 0) && catgenPageContext.Company != null)
					catgenPageContext.Template = catgenPageContext.Company.Template;

				if(catgenPageContext.Template == null || catgenPageContext.Template.length() == 0)
					catgenPageContext.Template = catgenPageContext.NetworkMarket.Template;

				if(catgenPageContext.Page == null && catgenPageContext.PageName != null && catgenPageContext.PageName.trim().length() > 0)
				{
					catgenPageContext.Page = PageFactory.getPageByName(conn, catgenPageContext.NetworkMarket.NetworkMarketID, catgenPageContext.PageName);
					if(catgenPageContext.Page == null)
						catgenPageContext.PageName = null;
				}
			}
			finally
			{
				conn.close();
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		if(catgenPageContext.Template != null)
		{
			catgenPageContext.Template = catgenPageContext.Template.replaceAll("[^a-zA-Z0-9_-]", "");
			if(catgenPageContext.Template.length() > 0 && servetURL != null)
				servetURL = servetURL + "_" + catgenPageContext.Template; 
		}

		request.setAttribute("catgenContext", catgenPageContext);
		
		if(servetURL == null)
			servetURL = "NotFound";

		javax.servlet.RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/" + servetURL + ".jsp"); 
		dispatcher.forward(request,response); 
	}

	private void ViewXml(String marketId, HttpServletResponse response) throws IOException
	{
		try
		{
			Connection conn = com.catgen.factories.MySqlDB.getDBConnection(getServletContext());

			try
			{
				// get DOM Implementation using DOM Registry
				System.setProperty(DOMImplementationRegistry.PROPERTY,"org.apache.xerces.dom.DOMXSImplementationSourceImpl");
				DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
				DOMImplementationLS impl = (DOMImplementationLS)registry.getDOMImplementation("LS");

				LSParser builder = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);

				DOMConfiguration config = builder.getDomConfig();
				DOMErrorHandler errorHandler = new DOMErrors();
				config.setParameter("error-handler", errorHandler);
				config.setParameter("validate",Boolean.FALSE);
				config.setParameter("schema-type", "http://www.w3.org/2001/XMLSchema");

				LSInput input = impl.createLSInput();

				StringReader stream = new StringReader("<Page/>"); 
				input.setCharacterStream(stream);

				Document document = builder.parse(input);

				NetworkMarketXml marketXml = new NetworkMarketXml();
				marketXml.AddToXml(conn, marketId, document.getDocumentElement());

				LSOutput destination = impl.createLSOutput();
				OutputStream os = response.getOutputStream();
				destination.setByteStream(os);
				destination.setEncoding("UTF-8");

				LSSerializer writer = impl.createLSSerializer();
				writer.write( document, destination);
			} 
			catch (ClassCastException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			finally
			{
				conn.close();
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	private void ViewHtml(String marketId, String templateURL, HttpServletResponse response) throws IOException
	{
		try
		{
			Connection conn = com.catgen.factories.MySqlDB.getDBConnection(getServletContext());

			try
			{
				// get DOM Implementation using DOM Registry
				System.setProperty(DOMImplementationRegistry.PROPERTY,"org.apache.xerces.dom.DOMXSImplementationSourceImpl");
				DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
				DOMImplementationLS impl = (DOMImplementationLS)registry.getDOMImplementation("LS");

				LSParser builder = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);

				DOMConfiguration config = builder.getDomConfig();
				DOMErrorHandler errorHandler = new DOMErrors();
				config.setParameter("error-handler", errorHandler);
				config.setParameter("validate",Boolean.FALSE);
				config.setParameter("schema-type", "http://www.w3.org/2001/XMLSchema");

				LSInput input = impl.createLSInput();

				StringReader stream = new StringReader("<Page/>"); 
				input.setCharacterStream(stream);

				Document document = builder.parse(input);

				NetworkMarketXml marketXml = new NetworkMarketXml();
				marketXml.AddToXml(conn, marketId, document.getDocumentElement());

				TransformerFactory transformerFactory = TransformerFactory.newInstance();

				if(transformerFactory.getFeature(DOMSource.FEATURE) && transformerFactory.getFeature(DOMResult.FEATURE))
				{
					Document xslDoc = builder.parseURI(templateURL);

					DOMSource xslDomSource = new DOMSource(xslDoc);
					xslDomSource.setSystemId(templateURL);

					DOMSource xmlDomSource = new DOMSource(document);
					xslDomSource.setSystemId(templateURL);

					Transformer transformer = transformerFactory.newTransformer(xslDomSource);

					SimpleDateFormat df;

					df = new SimpleDateFormat("dd");
					transformer.setParameter("day", "" + df.format( Calendar.getInstance().getTime() ));

					df = new SimpleDateFormat("MM");
					transformer.setParameter("month", "" + df.format( Calendar.getInstance().getTime() ));

					df = new SimpleDateFormat("MMMM");
					transformer.setParameter("monthname", "" + df.format( Calendar.getInstance().getTime() ));

					df = new SimpleDateFormat("yyyy");
					transformer.setParameter("year", "" + df.format( Calendar.getInstance().getTime() ));

					transformer.transform(xmlDomSource, new StreamResult(response.getOutputStream()));
				}
			} 
			catch (ClassCastException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
			finally
			{
				conn.close();
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	private void ViewPDF(String marketId, String templateURL, HttpServletResponse response, boolean showXML) throws IOException
	{
		try
		{
			Connection conn = com.catgen.factories.MySqlDB.getDBConnection(getServletContext());

			try
			{
				// get DOM Implementation using DOM Registry
				System.setProperty(DOMImplementationRegistry.PROPERTY,"org.apache.xerces.dom.DOMXSImplementationSourceImpl");
				DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
				DOMImplementationLS impl = (DOMImplementationLS)registry.getDOMImplementation("LS");

				LSParser builder = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);

				DOMConfiguration config = builder.getDomConfig();
				DOMErrorHandler errorHandler = new DOMErrors();
				config.setParameter("error-handler", errorHandler);
				config.setParameter("validate",Boolean.FALSE);
				config.setParameter("schema-type", "http://www.w3.org/2001/XMLSchema");

				LSInput input = impl.createLSInput();

				StringReader stream = new StringReader("<Page/>"); 
				input.setCharacterStream(stream);

				Document document = builder.parse(input);

				NetworkMarketXml marketXml = new NetworkMarketXml();
				marketXml.AddToXml(conn, marketId, document.getDocumentElement());

				TransformerFactory transformerFactory = TransformerFactory.newInstance();

				if(transformerFactory.getFeature(DOMSource.FEATURE) && transformerFactory.getFeature(DOMResult.FEATURE))
				{
					Document xslDoc = builder.parseURI(templateURL);

					DOMSource xslDomSource = new DOMSource(xslDoc);
					xslDomSource.setSystemId(templateURL);

					DOMSource xmlDomSource = new DOMSource(document);
					xslDomSource.setSystemId(templateURL);

					DOMResult domResult = new DOMResult();

					Transformer transformer = transformerFactory.newTransformer(xslDomSource);

					SimpleDateFormat df;

					df = new SimpleDateFormat("dd");
					transformer.setParameter("day", "" + df.format( Calendar.getInstance().getTime() ));

					df = new SimpleDateFormat("MM");
					transformer.setParameter("month", "" + df.format( Calendar.getInstance().getTime() ));

					df = new SimpleDateFormat("MMMM");
					transformer.setParameter("monthname", "" + df.format( Calendar.getInstance().getTime() ));

					df = new SimpleDateFormat("yyyy");
					transformer.setParameter("year", "" + df.format( Calendar.getInstance().getTime() ));

					transformer.transform(xmlDomSource, domResult);

					if(showXML)
					{
						LSOutput destination = impl.createLSOutput();
						OutputStream os = response.getOutputStream();
						destination.setByteStream(os);
						destination.setEncoding("UTF-8");

						LSSerializer writer = impl.createLSSerializer();
						writer.write( domResult.getNode(), destination);
					}
					else
					{
						FopFactory fopFactory = FopFactory.newInstance();

						FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

						// Construct fop with desired output format and output stream
						Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, response.getOutputStream());

						// Setup input for XSLT transformation
						DOMSource src = new DOMSource( domResult.getNode() );

						// Resulting SAX events (the generated FO) must be piped through to FOP
						SAXResult res = new SAXResult(fop.getDefaultHandler());

						Transformer fopTransformer = transformerFactory.newTransformer();

						// Start XSLT transformation and FOP processing
						fopTransformer.transform(src, res);
					}
				}

			} 
			catch (ClassCastException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			} catch (FOPException e) {
				e.printStackTrace();
			}
			finally
			{
				conn.close();
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
}



