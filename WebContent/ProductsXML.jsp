<%@ page
	import="java.util.*, org.apache.commons.lang.*,java.sql.*,com.catgen.*,com.catgen.factories.*, java.text.*,java.net.URLEncoder"%>
<%
	CatgenPageContext catgenContext = CatgenPageContext.getInstance(pageContext);

	NetworkMarket networkMarket = catgenContext.NetworkMarket;
	if (networkMarket == null) 
	{
		response.sendRedirect("CompanyNotFound.jsp");
		return;
	}

	Company company = catgenContext.Company;
	if (company == null) 
	{
		response.sendRedirect("MemberNotFound.jsp");
		return;
	}

	Connection conn = MySqlDB.getDBConnection(getServletContext());
	try {
		List<Product> products = null;
		if(catgenContext.Search == null)
			products = ProductFactory.getProducts( conn, networkMarket.NetworkMarketID, company.Code);
		else
			products = ProductFactory.getProducts( conn, networkMarket.NetworkMarketID, company.Code, catgenContext.Search);

		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		response.setContentType("text/xml");

		if(products != null && products.size() > 0)
		{
			int rowNumber = 0;
    		for( Product product : products ) 
    		{
    			String productURL;

    			if(product.URL != null && product.URL.length() > 0)
    				productURL = product.URL.replace( "$$productcode$$", product.Code );
    			else if(company.ProductURL != null && company.ProductURL.length() > 0)
    				productURL = company.ProductURL.replace( "$$productcode$$", product.Code );
    			else
    				productURL = URLEncoder.encode(product.CompanyCode, "UTF-8") + "/Product.html?productid=" + URLEncoder.encode(product.Code, "UTF-8");
    		}
		}
	} 
	finally 
	{
		conn.close();
	}
%>

