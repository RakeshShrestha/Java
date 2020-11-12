/**
# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.
*/

package com.catgen.factories;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.sql.DataSource;


public class MySqlDB {
	private static DataSource datasource = null;

	public static synchronized Connection getDBConnection(ServletContext servletContext) throws SQLException 
	{
		if(datasource == null)
			datasource = (DataSource) servletContext.getAttribute("NetmarketPool");

		return datasource.getConnection();
	}
}