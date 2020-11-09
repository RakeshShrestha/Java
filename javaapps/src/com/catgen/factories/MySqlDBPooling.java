# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

package com.catgen.factories;

import javax.servlet.*;
import javax.sql.*;
import javax.naming.*;

public class MySqlDBPooling implements ServletContextListener
{
	public void contextInitialized(ServletContextEvent sce)
	{
		try {
			// Obtain our environment naming context
			Context envCtx = (Context) new InitialContext().lookup("java:comp/env");

			// Look up our data source
			DataSource  ds = (DataSource) envCtx.lookup("jdbc/NetmarketDB");

			sce.getServletContext().setAttribute("NetmarketPool", ds);
			
			System.out.println("MySqlDBPooling is set to " + ds.toString() );
		} 
		catch(NamingException e)
		{
			e.printStackTrace();
		}
	}
	
	public void contextDestroyed(ServletContextEvent sce)
	{
	}
}

