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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.catgen.Page;

public class PageFactory {
	public static void Save(Page page)
	{
		
	}
	
	public static void Load(Page page)
	{
		
	}

	private static void LoadPageFromResultSet(ResultSet rs, Page page) throws SQLException
	{
		page.NetworkMarketID = rs.getString("MarketID");
		page.Name = rs.getString("Name");
		page.Code = rs.getString("Code");
		page.Description = rs.getString("Description");
		page.HeaderURL = rs.getString("HeaderURL");
		page.HeaderData = rs.getString("HeaderData");
		page.FooterURL = rs.getString("FooterURL");
		page.FooterData = rs.getString("FooterData");
		page.Type = rs.getString("Type");
		page.Header = rs.getString("Header");
		page.Footer = rs.getString("Footer");
		page.CSS = rs.getString("CSS");
		page.JavaScript = rs.getString("JavaScript");
		page.Template = rs.getString("Template");
	}
	
	public static Page getPageByName(Connection conn, String marketId, String name) throws SQLException
	{
		Page page = null;

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Pages WHERE MarketID = ? AND Name = ?" );
		try
		{
			pstmt.setString(1, marketId);
			pstmt.setString(2, name);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				if(rs.next())
				{
					page = new Page();

					LoadPageFromResultSet(rs, page);
				}
			}
			finally
			{
				rs.close();
			}
		}
		finally
		{
			pstmt.close();
		}			
		
		return page;		
	}
	
	public static List<Page> getPages(Connection conn, String marketId) throws SQLException
	{
		ArrayList<Page> pages = new ArrayList<Page>();

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Pages WHERE MarketID = ? ORDER BY Name" );
		try
		{
			pstmt.setString(1, marketId);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Page page = new Page();

					LoadPageFromResultSet(rs, page);
					
					pages.add(page);
				}
			}
			finally
			{
				rs.close();
			}
		}
		finally
		{
			pstmt.close();
		}			
		
		return pages;		
	}
	
	public static List<Page> getPagesOrderByRow(Connection conn, String marketId) throws SQLException
	{
		ArrayList<Page> pages = new ArrayList<Page>();

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Pages WHERE MarketID = ? ORDER BY RowID, Name" );
		try
		{
			pstmt.setString(1, marketId);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Page page = new Page();

					LoadPageFromResultSet(rs, page);
					
					pages.add(page);
				}
			}
			finally
			{
				rs.close();
			}
		}
		finally
		{
			pstmt.close();
		}			
		
		return pages;		
	}
}
