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
import java.util.*;
import com.catgen.Referral;

public class ReferralFactory 
{
	private static void LoadReferralFromResultSet(ResultSet rs, Referral referral) throws SQLException
	{
		referral.NetworkMarketID = rs.getString("NetworkMarketID");
		referral.CompanyCode = rs.getString("CompanyCode");

		java.sql.Timestamp t = rs.getTimestamp("ReferralDate");
		
		if(t != null)
			referral.ReferralDate = new java.util.Date(t.getTime());
				
		referral.Email = rs.getString("Email");
		referral.ClientIP = rs.getString("ClientIP");
		referral.ReferralEvent = rs.getString("ReferralEvent");
		referral.ExtraData = rs.getString("ExtraData");
		referral.Level = rs.getInt("Level"); 
	}
	
	public static void SaveReferral(Connection conn, String networkMarketID, String companyCode, String referralID, String referralEventType, String clientIP, String extraData) throws SQLException
	{
		int l = 0;
		for(String s: referralID.split(","))
		{
			String email = s.trim();
			if(email.length() > 0)
			{
				l++;
				if(l > 3)
					break;
				
				PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Referrals(Email, NetworkMarketID, CompanyCode, ClientIP, ReferralEvent, ExtraData, ReferralDate, Level) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

				pstmt.setString(1, email);
				pstmt.setString(2, networkMarketID);
				pstmt.setString(3, companyCode);
				pstmt.setString(4, clientIP);
				pstmt.setString(5, referralEventType);
				pstmt.setString(6, extraData);

				pstmt.setTimestamp(7, new java.sql.Timestamp( Calendar.getInstance().getTime().getTime()));
				pstmt.setInt(8, l);

				pstmt.executeUpdate();
			}		
		}
	}

	public static List<Referral> getReferralInfoByEmail(Connection conn, String email, Date startDate, Date endDate) throws SQLException
	{
		ArrayList<Referral> referrals = new ArrayList<Referral>();
		
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Referrals WHERE Email = ? AND ReferralDate > ? AND ReferralDate < ? ORDER BY ReferralDate DESC");
		try
		{
			pstmt.setString(1, email);
			pstmt.setTimestamp(2, new java.sql.Timestamp( startDate.getTime() ));
			pstmt.setTimestamp(3, new java.sql.Timestamp( endDate.getTime() ));

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Referral referral = new Referral();

					LoadReferralFromResultSet(rs, referral);
					
					referrals.add(referral);
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
		
		return referrals;
	}

	public static List<Referral> getReferralInfoByCompany(Connection conn, String companyCode, Date startDate, Date endDate) throws SQLException
	{
		ArrayList<Referral> referrals = new ArrayList<Referral>();
		
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Referrals WHERE CompanyCode = ? AND ReferralDate > ? AND ReferralDate < ? ORDER BY ReferralDate DESC");
		try
		{
			pstmt.setString(1, companyCode);
			pstmt.setTimestamp(2, new java.sql.Timestamp( startDate.getTime() ));
			pstmt.setTimestamp(3, new java.sql.Timestamp( endDate.getTime() ));

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Referral referral = new Referral();

					LoadReferralFromResultSet(rs, referral);
					
					referrals.add(referral);
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
		
		return referrals;
	}

	public static List<Referral> getReferralInfoByMarketID(Connection conn, String marketID, Date startDate, Date endDate) throws SQLException
	{
		ArrayList<Referral> referrals = new ArrayList<Referral>();
		
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Referrals WHERE NetworkMarketID = ? AND ReferralDate > ? AND ReferralDate < ? ORDER BY ReferralDate DESC");
		try
		{
			pstmt.setString(1, marketID);
			pstmt.setTimestamp(2, new java.sql.Timestamp( startDate.getTime() ));
			pstmt.setTimestamp(3, new java.sql.Timestamp( endDate.getTime() ));

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Referral referral = new Referral();

					LoadReferralFromResultSet(rs, referral);
					
					referrals.add(referral);
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
		
		return referrals;
	}
}
