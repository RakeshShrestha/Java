# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

package com.catgen.factories;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.catgen.Company;
import com.catgen.MarketMember;
import com.catgen.loader.CompanyInfoLoader;
import com.catgen.loader.PagesLoader;
import com.catgen.loader.ProductsLoader;

public class CompanyFactory {

	public static void Save(Company company)
	{

	}

	public static void Load(Company company)
	{

	}
	
	private static Hashtable<String, Company> m_cachedCompanies = null;
	
	public static void ExpiresCompaniesCache()
	{
		m_cachedCompanies = null;
	}
	
	public static Company getCachedCompanyByCode(Connection conn, String marketId, String companyCode) throws SQLException
	{
		if(m_cachedCompanies == null)
			m_cachedCompanies = new Hashtable<String, Company>();
			
		Company company = m_cachedCompanies.get(marketId + "###" + companyCode);
		if(company == null)
		{
			company = getCompanyByCode(conn, marketId, companyCode);
			if(company != null)
				m_cachedCompanies.put(marketId + "###" + companyCode, company);
		}
		
		return company;
	}
	
	public static void UpdateCompany(Connection conn, String marketId, String companyCode) throws SQLException
	{
		MarketMember marketMember = MarketMemberFactory.getMarketMemberByCode(conn, marketId, companyCode);
		
		if(marketMember != null)
		{
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Companies WHERE MarketID = ? AND CompanyCode = ?");

			pstmt.setString(1, marketId);
			pstmt.setString(2, companyCode);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("DELETE FROM Products WHERE MarketID = ? AND CompanyCode = ?");

			pstmt.setString(1, marketId);
			pstmt.setString(2, companyCode);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("DELETE FROM Pages WHERE MarketID = ? AND CompanyCode = ?");

			pstmt.setString(1, marketId);
			pstmt.setString(2, companyCode);
			pstmt.executeUpdate();

			pstmt = conn.prepareStatement("DELETE FROM ProductLines WHERE MarketID = ? AND CompanyCode = ?");

			pstmt.setString(1, marketId);
			pstmt.setString(2, companyCode);
			pstmt.executeUpdate();
			
			CompanyInfoLoader companyInfoLoader = new CompanyInfoLoader();
			companyInfoLoader.ClearExtras();
			companyInfoLoader.AddExtras("MarketID", marketMember.NetworkMarketID);
			companyInfoLoader.AddExtras( "CompanyCode", companyCode );
			companyInfoLoader.LoadData(conn, marketMember.CompanyInfoURL);
			
			ProductsLoader productsLoader = new ProductsLoader();
			productsLoader.ClearExtras();
			productsLoader.AddExtras( "MarketID", marketMember.NetworkMarketID );
			productsLoader.AddExtras( "CompanyCode", companyCode );
			productsLoader.LoadData(conn, marketMember.ProductsURL);

			PagesLoader pagesLoader = new PagesLoader();
			pagesLoader.ClearExtras();
			pagesLoader.AddExtras( "MarketID", marketMember.NetworkMarketID );
			pagesLoader.AddExtras( "CompanyCode", companyCode );
			pagesLoader.LoadData(conn, marketMember.PagesURL);
		}
	}

	private static void LoadCompanyFromResultSet(ResultSet rs, Company company) throws SQLException
	{
		company.NetworkMarketID = rs.getString("MarketID");
		company.Code = rs.getString("CompanyCode");
		company.Name = rs.getString("Name");
		company.Description = rs.getString("Description");
		company.HomePageDescription = rs.getString("HomePageDescription");
		company.LogoImage = rs.getString("LogoImage");
		company.Country = rs.getString("Country");
		company.Contact = rs.getString("Contact");
		company.ContactEmail = rs.getString("ContactEmail");
		company.PayPalEmail = rs.getString("PayPalEmail");
		company.GoogleMerchantID = rs.getString("GoogleMerchantID");
		company.TwoCOSID = rs.getString("TwoCOSID");
		company.Header = rs.getString("Header");
		company.Footer = rs.getString("Footer");
		company.HeaderURL = rs.getString("HeaderURL");
		company.FooterURL = rs.getString("FooterURL");
		company.CSS = rs.getString("CSS");
		company.Currency = rs.getString("Currency");
		company.CurrencySymbol = rs.getString("CurrencySymbol");
		company.InquiryURL = rs.getString("InquiryURL");
		company.ThumbnailSize = rs.getString("ThumbnailSize");
		company.TemplateWidth = rs.getString("TemplateWidth");
		company.Template = rs.getString("Template");
		company.Font = rs.getString("Font");
		company.FontSize = rs.getString("FontSize");
		company.Color = rs.getString("Color");
		company.SecondaryColor = rs.getString("SecondaryColor");
		company.TetriaryColor = rs.getString("TetriaryColor");
		company.Background = rs.getString("Background");
		company.Background2 = rs.getString("Background2");
		company.Background3 = rs.getString("Background3");
		company.BackgroundImage = rs.getString("BackgroundImage");
		company.ProductImageSize = rs.getString("ProductImageSize");
		company.ProductURL = rs.getString("ProductURL");
		company.CompanyURL = rs.getString("CompanyURL");
	}

	public static Company getCompanyByCode(Connection conn, String marketId, String companyCode) throws SQLException
	{
		Company company = null;

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Companies WHERE MarketID = ? AND CompanyCode = ?" );
		try
		{
			pstmt.setString(1, marketId);
			pstmt.setString(2, companyCode);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				if(rs.next())
				{
					company = new Company();

					LoadCompanyFromResultSet(rs, company);
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

		return company;
	}

	public static List<Company> getNetmarketMembers(Connection conn, String marketId) throws SQLException
	{
		ArrayList<Company> companies = new ArrayList<Company>();

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Companies WHERE MarketID = ?" );
		try
		{
			pstmt.setString(1, marketId);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Company company = new Company();

					LoadCompanyFromResultSet(rs, company);
					
					companies.add(company);
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


		return companies;
	}
}
