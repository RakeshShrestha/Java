# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

package com.catgen.factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.catgen.MarketMember;;

public class MarketMemberFactory
{
	private static void LoadMarketMemberFromResultSet(ResultSet rs, MarketMember marketMember) throws SQLException
	{
		marketMember.NetworkMarketID = rs.getString("MarketID");
		marketMember.CompanyCode = rs.getString("CompanyCode");
		marketMember.ProductsURL = rs.getString("ProductsURL");
		marketMember.CompanyInfoURL = rs.getString("CompanyInfoURL");
		marketMember.PagesURL = rs.getString("PagesURL");
		marketMember.DomainName = rs.getString("DomainName");
	}
	
	public static MarketMember getMarketMemberByCode(Connection conn, String marketId, String companyCode) throws SQLException
	{
		MarketMember marketMember = null;

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM MarketMembers WHERE MarketID = ? AND CompanyCode = ?" );
		try
		{
			pstmt.setString(1, marketId);
			pstmt.setString(2, companyCode);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				if(rs.next())
				{
					marketMember = new MarketMember();

					LoadMarketMemberFromResultSet(rs, marketMember);
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

		return marketMember;
	}
}
