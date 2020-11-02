package com.catgen.factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.catgen.MasterMarket;
import com.catgen.loader.MasterMarketLoader;

public class MasterMarketFactory 
{
	public static void UpdateMasterMarket(Connection conn) throws SQLException
	{
		PreparedStatement pstmt = conn.prepareStatement("DELETE FROM MasterMarkets");
		pstmt.executeUpdate();
		
		MasterMarketLoader masterMarketLoader = new MasterMarketLoader();
		masterMarketLoader.LoadData(conn, "http://spreadsheets.google.com/feeds/cells/p2DIlEfXXvRGM1iKwllxSaw/od6/public/basic");
	}
	
	private static void LoadMasterMarketFromResultSet(ResultSet rs, MasterMarket MasterMarket) throws SQLException
	{
		MasterMarket.NetworkMarketID = rs.getString("MarketID");
		MasterMarket.Description = rs.getString("Description");
		MasterMarket.DomainName = rs.getString("DomainName");
		MasterMarket.MarketsURL = rs.getString("MarketsURL");
		MasterMarket.MarketInfoURL = rs.getString("MarketInfoURL");
		MasterMarket.PagesURL = rs.getString("PagesURL");
		MasterMarket.FeaturedProductsURL = rs.getString("FeaturedProductsURL");
	}

	public static MasterMarket getMasterMarketByDomainName(Connection conn, String domainName) throws SQLException
	{
		MasterMarket MasterMarket = null;

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM MasterMarkets WHERE DomainName = ?" );
		try
		{
			pstmt.setString(1, domainName);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				if(rs.next())
				{
					MasterMarket = new MasterMarket();

					LoadMasterMarketFromResultSet(rs, MasterMarket);
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

		return MasterMarket;
	}

	public static MasterMarket getMasterMarketByCode(Connection conn, String marketId) throws SQLException
	{
		MasterMarket MasterMarket = null;

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM MasterMarkets WHERE MarketID = ?" );
		try
		{
			pstmt.setString(1, marketId);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				if(rs.next())
				{
					MasterMarket = new MasterMarket();

					LoadMasterMarketFromResultSet(rs, MasterMarket);
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

		return MasterMarket;
	}
	
	public static List<MasterMarket> getMasterMembers(Connection conn) throws SQLException
	{
		ArrayList<MasterMarket> MasterMarkets = new ArrayList<MasterMarket>();

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM MasterMarkets ORDER BY MarketID" );
		try
		{
			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					MasterMarket MasterMarket = new MasterMarket();

					LoadMasterMarketFromResultSet(rs, MasterMarket);

					MasterMarkets.add(MasterMarket);
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


		return MasterMarkets;
	}
}
