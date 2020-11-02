package com.catgen.factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import com.catgen.Product;
import com.catgen.KeyValue;

public class ProductFactory 
{
	public static void Save(Product product)
	{

	}

	public static void Load(Product product)
	{

	}

	private static void LoadProductFromResultSet(ResultSet rs, Product product) throws SQLException
	{
		product.NetworkMarketID = rs.getString("MarketID");
		product.CompanyCode = rs.getString("CompanyCode");
		product.Code = rs.getString("Code");
		product.Name = rs.getString("Name");
		product.Price = rs.getString("Price");
		product.Description = rs.getString("Description");
		product.ImageURL = rs.getString("ImageURL");
		product.URL = rs.getString("URL");
		product.Header = rs.getString("Header");
		product.Footer = rs.getString("Footer");
		product.HeaderURL = rs.getString("HeaderURL");
		product.FooterURL = rs.getString("FooterURL");
		product.Currency = rs.getString("Currency");
		product.ProductLine = rs.getString("ProductLine");
		product.Keywords = rs.getString("Keywords");
		product.Quantity = rs.getString("Quantity");
		product.Featured = rs.getString("Featured") == "1";
	}
	
	public static boolean ExistsByName(Connection conn, String marketId, String companyCode, String name) throws SQLException
	{
		boolean result = false;
		
		PreparedStatement pstmt = conn.prepareStatement( "SELECT COUNT(*) FROM Products WHERE MarketID = ? AND CompanyCode = ? AND Name = ?" );
		try
		{
			pstmt.setString(1, marketId);
			pstmt.setString(2, companyCode);
			pstmt.setString(3, name);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				if(rs.next())
					result = rs.getInt(1) > 0;
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
		
		return result;
	}
	
	
	public static Product getProductByCode(Connection conn, String marketId, String companyCode, String productCode) throws SQLException
	{
		Product product = null;
		
		if(productCode == null || productCode.trim().length() == 0)
			return product;		

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Products WHERE MarketID = ? AND CompanyCode = ? AND Code = ?" );
		try
		{
			pstmt.setString(1, marketId);
			pstmt.setString(2, companyCode);
			pstmt.setString(3, productCode);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				if(rs.next())
				{
					product = new Product();

					LoadProductFromResultSet(rs, product);
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

		return product;
	}

	public static Product getProductByName(Connection conn, String marketId, String companyCode, String productName) throws SQLException
	{
		Product product = null;
		
		if(productName == null || productName.trim().length() == 0)
			return product;

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Products WHERE MarketID = ? AND CompanyCode = ? AND Name = ?" );
		try
		{
			pstmt.setString(1, marketId);
			pstmt.setString(2, companyCode);
			pstmt.setString(3, productName);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				if(rs.next())
				{
					product = new Product();

					LoadProductFromResultSet(rs, product);
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

		return product;
	}
	
	public static List<Product> getProducts(Connection conn, String marketId, String companyCode) throws SQLException
	{
		ArrayList<Product> products = new ArrayList<Product>();

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Products WHERE MarketID = ? AND CompanyCode = ? ORDER BY Name" );
		try
		{
			pstmt.setString(1, marketId);
			pstmt.setString(2, companyCode);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Product product = new Product();

					LoadProductFromResultSet(rs, product);
					
					products.add(product);
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

		return products;
	}

	public static List<Product> getFeaturedProducts(Connection conn, String marketId, String companyCode) throws SQLException
	{
		ArrayList<Product> products = new ArrayList<Product>();

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Products WHERE MarketID = ? AND CompanyCode = ? AND Featured = 'y' ORDER BY Name" );
		try
		{
			pstmt.setString(1, marketId);
			pstmt.setString(2, companyCode);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Product product = new Product();

					LoadProductFromResultSet(rs, product);
					
					products.add(product);
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

		return products;
	}
	
	public static List<Product> getNetworkMarketOwnFeaturedProducts(Connection conn, String marketId) throws SQLException
	{
		ArrayList<Product> products = new ArrayList<Product>();

		PreparedStatement pstmt = conn.prepareStatement( "SELECT Products.* FROM Products INNER JOIN FeaturedProducts ON Products.CompanyCode = FeaturedProducts.CompanyCode AND Products.Code = FeaturedProducts.ProductCode WHERE FeaturedProducts.MarketID = ? AND Products.MarketID = ? ORDER BY Products.Name" );
		try
		{
			pstmt.setString(1, marketId);
			pstmt.setString(2, marketId);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Product product = new Product();

					LoadProductFromResultSet(rs, product);
					
					products.add(product);
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

		return products;
	}
	
	public static List<Product> getProducts(Connection conn, String marketId, String companyCode, String search) throws SQLException
	{
		ArrayList<Product> products = new ArrayList<Product>();

		if(search.indexOf("%") <0)
			search = "%" + search + "%";
			
		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Products WHERE MarketID = ? AND CompanyCode = ? AND (Name LIKE ? OR Description LIKE ? OR Keywords LIKE ?) ORDER BY Name" );
		try
		{
			pstmt.setString(1, marketId);
			pstmt.setString(2, companyCode);
			pstmt.setString(3, search);
			pstmt.setString(4, search);
			pstmt.setString(5, search);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Product product = new Product();

					LoadProductFromResultSet(rs, product);
					
					products.add(product);
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

		return products;
	}

	public static List<Product> getProductsForProductLine(Connection conn, String marketId, String companyCode, String productLine) throws SQLException
	{
		ArrayList<Product> products = new ArrayList<Product>();

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Products WHERE MarketID = ? AND CompanyCode = ? AND ProductLine = ? ORDER BY Name" );
		try
		{
			pstmt.setString(1, marketId);
			pstmt.setString(2, companyCode);
			pstmt.setString(3, productLine);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Product product = new Product();

					LoadProductFromResultSet(rs, product);
					
					products.add(product);
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

		return products;
	}
	
	public static List<Product> getProductsByParameters(Connection conn, String marketId, List<KeyValue> parameters) throws SQLException
	{
		ArrayList<Product> products = new ArrayList<Product>();
		
		String sqlWhere = "";
		
		for(KeyValue parameter: parameters)
		{
			if("keyword".equalsIgnoreCase(parameter.Key))
				sqlWhere += " AND (Name LIKE ? OR Description LIKE ? OR Keywords LIKE ?)";
			else if("country".equalsIgnoreCase(parameter.Key))
				sqlWhere += " AND CompanyCode IN (SELECT CompanyCode FROM Companies WHERE Companies.MarketID = Products.MarketID AND Companies.Country = ?)";
			else if("region".equalsIgnoreCase(parameter.Key))
				sqlWhere += " AND CompanyCode IN (SELECT CompanyCode FROM Companies WHERE Companies.MarketID = Products.MarketID AND Companies.Country IN (SELECT Keyword FROM Keywords WHERE Keywords.KeywordType = ? AND Keywords.Value = ?))";
			else
				sqlWhere += " AND " + parameter.Key + " = ?";
		}

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Products WHERE MarketID = ? " + sqlWhere + " ORDER BY Name" );
		try
		{
			pstmt.setString(1, marketId);
			
			int i = 2; 
			for(KeyValue parameter: parameters)
			{
				if("keyword".equalsIgnoreCase(parameter.Key))
				{
					pstmt.setString(i++, "%" + parameter.Value + "%");
					pstmt.setString(i++, "%" + parameter.Value + "%");
					pstmt.setString(i++, "%" + parameter.Value + "%");
				}
				else if("region".equalsIgnoreCase(parameter.Key))
				{
					pstmt.setString(i++, "cntr2rgn");
					pstmt.setString(i++, parameter.Value);
				}
				else
					pstmt.setString(i++, parameter.Value);
			}

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Product product = new Product();

					LoadProductFromResultSet(rs, product);
					
					products.add(product);
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

		return products;
	}
	
	public static List<Product> getMarketProducts(Connection conn, String marketId) throws SQLException
	{
		ArrayList<Product> products = new ArrayList<Product>();

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Products WHERE MarketID = ? ORDER BY Name" );
		try
		{
			pstmt.setString(1, marketId);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Product product = new Product();

					LoadProductFromResultSet(rs, product);
					
					products.add(product);
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

		return products;
	}
	
	public static List<Product> getMarketFeaturedProducts(Connection conn, String marketId) throws SQLException
	{
		ArrayList<Product> products = new ArrayList<Product>();

		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Products WHERE MarketID = ? AND Featured = 'y' ORDER BY Name" );
		try
		{
			pstmt.setString(1, marketId);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Product product = new Product();

					LoadProductFromResultSet(rs, product);
					
					products.add(product);
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

		return products;
	}
	

	public static List<Product> getMarketProducts(Connection conn, String marketId, String search) throws SQLException
	{
		ArrayList<Product> products = new ArrayList<Product>();

		if(search.indexOf("%") <0)
			search = "%" + search + "%";
			
		PreparedStatement pstmt = conn.prepareStatement( "SELECT * FROM Products WHERE MarketID = ? AND (Name LIKE ? OR Description LIKE ? OR Keywords LIKE ?) ORDER BY Name" );
		try
		{
			pstmt.setString(1, marketId);
			pstmt.setString(2, search);
			pstmt.setString(3, search);
			pstmt.setString(4, search);

			ResultSet rs = pstmt.executeQuery();
			try
			{
				while(rs.next())
				{
					Product product = new Product();

					LoadProductFromResultSet(rs, product);
					
					products.add(product);
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

		return products;
	}

	public static List<Product> RanodmizeProducts(List<Product> products)
	{
		return RanodmizeProducts(products, products.size());
	}

	public static List<Product> RanodmizeProducts(List<Product> products, int maxInclude)
	{
		if(products != null)
		{
			Collections.shuffle(products);

			if(maxInclude == 0 || maxInclude == products.size())
				return products;
			else
			{
				ArrayList<Product> randomProducts = new ArrayList<Product>();
				for(int i = 0; i < maxInclude; i++)
				{
					randomProducts.add( products.get(i) );					
				}
				
				return randomProducts;
			}
		}
		else
			return null;
	}
	
	public static List<Product> SortEqualyByMarkets(List<Product> products)
	{
		return SortEqualyByMarkets(products, products.size());
	}

	public static List<Product> SortEqualyByMarkets(List<Product> products, int maxInclude)
	{
		ArrayList<Product> sortedProducts = new ArrayList<Product>();
		
		if(products != null)
		{
			ArrayList<Integer> usedProducts = new ArrayList<Integer>(products.size());
			for(int j = 0; j < products.size(); j++)
			{
				usedProducts.add(new Integer(0));
			}

			boolean found = true;

			while(found && (maxInclude == 0 || sortedProducts.size() < maxInclude))
			{
				Hashtable<String, String> companies = new Hashtable<String, String>();

				found = false;
				for(int i = 0; i < products.size(); i++)
				{
					if(usedProducts.get(i) == null || usedProducts.get(i) == 0)
					{
						Product product = products.get(i);

						if(companies.get(product.CompanyCode) != null)
							continue;

						sortedProducts.add(product);
						usedProducts.set(i, new Integer(1));
						companies.put(product.CompanyCode, "1");
						found = true;

						if(maxInclude != 0 && sortedProducts.size() >= maxInclude)
							break;
					}
				}
			}
		}

		return sortedProducts;
	}

	public static List<Product> getPagedProducts(List<Product> products, int pageSize, int pageNumber)
	{
		List<Product> resultProducts = products; 
		
		ArrayList<Product> pagedProducts = null;
		
		if(products != null && pageSize > 0)
		{
			if(pageNumber < 1)
				pageNumber = 1;
			
			int l = products.size();
			
			if(pageSize * (pageNumber - 1) < l )
			{
				pagedProducts = new ArrayList<Product>();
				
				int startIndex = pageSize * (pageNumber - 1);
				
				for(int i = 0; i < pageSize; i++ )
				{
					if(l <= startIndex + i)
						break;
					
					pagedProducts.add( products.get(startIndex + i) );
				}
				
				resultProducts = pagedProducts;				
			}
		}

		return resultProducts;
	}

}
