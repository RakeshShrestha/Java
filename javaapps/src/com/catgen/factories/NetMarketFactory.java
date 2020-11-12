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

import com.catgen.MasterMarket;
import com.catgen.NetworkMarket;
import com.catgen.loader.FeaturedProductsLoader;
import com.catgen.loader.MembersLoader;
import com.catgen.loader.NetworkMarketInfoLoader;
import com.catgen.loader.PagesLoader;

public class NetMarketFactory 
{
	public static void Save(NetworkMarket NetworkMarket)
	{

	}

	public static void Load(NetworkMarket NetworkMarket)
	{

	}	
	
	public static void UpdateNetworkMarket(Connection conn, String marketId) throws SQLException
	{
		MasterMarket masterMarket = MasterMarketFactory.getMasterMarketByCode(conn, marketId);  
		
		if(masterMarket != null)
		{
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM NetworkMarkets WHERE MarketID = ?");

			pstmt.setString(1, marketId);
			pstmt.executeUpdate();

			pstmt = conn.prepareStatement("DELETE FROM Companies WHERE MarketID = ?");

			pstmt.setString(1, marketId);
			pstmt.executeUpdate();

			pstmt = conn.prepareStatement("DELETE FROM Products WHERE MarketID = ?");

			pstmt.setString(1, marketId);
			pstmt.executeUpdate();

			pstmt = conn.prepareStatement("DELETE FROM Pages WHERE MarketID = ?");

			pstmt.setString(1, marketId);
			pstmt.executeUpdate();

			pstmt = conn.prepareStatement("DELETE FROM ProductLines WHERE MarketID = ?");

			pstmt.setString(1, marketId);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("DELETE FROM FeaturedProducts WHERE MarketID = ?");

			pstmt.setString(1, marketId);
			pstmt.executeUpdate();

			NetworkMarketInfoLoader networkMarketInfoLoader = new NetworkMarketInfoLoader();
			networkMarketInfoLoader.ClearExtras();
			networkMarketInfoLoader.AddExtras("MarketID", masterMarket.NetworkMarketID);
			networkMarketInfoLoader.LoadData(conn, masterMarket.MarketInfoURL);
			
			MembersLoader membersLoader = new MembersLoader();
			membersLoader.MarketID = masterMarket.NetworkMarketID;
			membersLoader.ClearExtras();
			membersLoader.AddExtras("MarketID", masterMarket.NetworkMarketID);
			membersLoader.LoadData(conn, masterMarket.MarketsURL);

			if(masterMarket.PagesURL != null && masterMarket.PagesURL.trim().length() > 0)
			{
				PagesLoader pagesLoader = new PagesLoader();
				pagesLoader.ClearExtras();
				pagesLoader.AddExtras("MarketID", masterMarket.NetworkMarketID);
				pagesLoader.LoadData(conn, masterMarket.PagesURL);
			}
			
			if(masterMarket.FeaturedProductsURL != null && masterMarket.FeaturedProductsURL.trim().length() > 0)
			{
				FeaturedProductsLoader featuredProductsLoader = new FeaturedProductsLoader();
				featuredProductsLoader.ClearExtras();
				featuredProductsLoader.AddExtras("MarketID", masterMarket.NetworkMarketID);
				featuredProductsLoader.LoadData(conn, masterMarket.FeaturedProductsURL);
			}
		}
	}

	private static void LoadNetworkMarketFromResultSet(ResultSet rs, NetworkMarket NetworkMarket) throws SQLException
	{
		NetworkMarket.NetworkMarketID = rs.getString("MarketID");
		NetworkMarket.Name = rs.getString("Name");
		NetworkMarket.Description = rs.getString("Description");
		NetworkMarket.HomePageDescription = rs.getString("HomePageDescription");
		NetworkMarket.LogoImage = rs.getString("LogoImage");
		NetworkMarket.Address = rs.getString("Address");
		NetworkMarket.City = rs.getString("City");
		NetworkMarket.State = rs.getString("State");
		NetworkMarket.Zip = rs.getString("Zip");
		NetworkMarket.Contact = rs.getString("Contact");
		NetworkMarket.ContactEmail = rs.getString("ContactEmail");
		NetworkMarket.PayPalEmail = rs.getString("PayPalEmail");
		NetworkMarket.GoogleMerchantID = rs.getString("GoogleMerchantID");
		NetworkMarket.TwoCOSID = rs.getString("TwoCOSID");
		NetworkMarket.Header = rs.getString("Header");
		NetworkMarket.Footer = rs.getString("Footer");
		NetworkMarket.HeaderURL = rs.getString("HeaderURL");
		NetworkMarket.FooterURL = rs.getString("FooterURL");
		NetworkMarket.CSS = rs.getString("CSS");
		NetworkMarket.Currency = rs.getString("Currency");
		NetworkMarket.InquiryURL = rs.getString("InquiryURL");
		NetworkMarket.ThumbnailSize = rs.getString("ThumbnailSize");
		NetworkMarket.TemplateWidth = rs.getString("TemplateWidth");
		NetworkMarket.Template = rs.getString("Template");
		NetworkMarket.Font = rs.getString("Font");
		NetworkMarket.FontSize = rs.getString("FontSize");
		NetworkMarket.Color = rs.getString("Color");
		NetworkMarket.SecondaryColor = rs.getString("SecondaryColor");
		NetworkMarket.TetriaryColor = rs.getString("TetriaryColor");
		NetworkMarket.Background = rs.getString("Background");
		NetworkMarket.Background2 = rs.getString("Background2");
		NetworkMarket.Background3 = rs.getString("Background3");
		NetworkMarket.BackgroundImage = rs.getString("BackgroundImage");
		NetworkMarket.ProductImageSize = rs.getString("ProductImageSize");
	}

	public static NetworkMarket getNetworkMarketByCode(Connection conn, String marketId) throws SQLException
	{
		NetworkMarket networkMarket = null;

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM NetworkMarkets WHERE MarketID = ?" );
		try
		{
			pstmt.setString(1, marketId);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				if(rs.next())
				{
					networkMarket = new NetworkMarket();

					LoadNetworkMarketFromResultSet(rs, networkMarket);
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

		return networkMarket;
	}

	public static List<NetworkMarket> getNetmarketMembers(Connection conn) throws SQLException
	{
		ArrayList<NetworkMarket> networkMarkets = new ArrayList<NetworkMarket>();

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM NetworkMarkets ORDER BY MarketID" );
		try
		{
			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					NetworkMarket NetworkMarket = new NetworkMarket();

					LoadNetworkMarketFromResultSet(rs, NetworkMarket);
					
					networkMarkets.add(NetworkMarket);
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


		return networkMarkets;
	}
}
