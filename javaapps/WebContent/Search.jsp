<%@ page
	import="java.util.*, org.apache.commons.lang.*,java.sql.*,com.catgen.*,com.catgen.factories.*,java.net.URLEncoder"%>
<%
	CatgenPageContext catgenContext = CatgenPageContext.getInstance(pageContext);
	catgenContext.DefaultPageName = "Search";

	NetworkMarket networkMarket = catgenContext.NetworkMarket;
	if (networkMarket == null) 
	{
		response.sendRedirect("CompanyNotFound.jsp");
		return;
	}

	Connection conn = MySqlDB.getDBConnection(getServletContext());
	try {
		String keyword1 = request.getParameter("search");
		String keyword2 = request.getParameter("search2"); 
		String country = request.getParameter("searchCountry");
		String region = request.getParameter("searchRegion");
		
		ArrayList<KeyValue> parameters = new ArrayList<KeyValue>();
		
		if(keyword1 != null && keyword1.trim().length() > 0)
			parameters.add( new KeyValue("keyword", keyword1) );

		if(keyword2 != null && keyword2.trim().length() > 0)
			parameters.add( new KeyValue("keyword", keyword2) );

		if(country != null && country.trim().length() > 0)
			parameters.add( new KeyValue("country", country) );

		if(region != null && region.trim().length() > 0)
			parameters.add( new KeyValue("region", region) );
		
		List<Product> products = null;
		if(parameters.size() > 0)
			products = ProductFactory.SortEqualyByMarkets( ProductFactory.getProductsByParameters( conn, networkMarket.NetworkMarketID, parameters ));
		else if(catgenContext.Search != null) 
			products = ProductFactory.SortEqualyByMarkets( ProductFactory.getMarketProducts( conn, networkMarket.NetworkMarketID, catgenContext.Search) );

		List<Product> allProducts = products;
		
		if(catgenContext.PageSize > 0)
			products = ProductFactory.getPagedProducts(products, catgenContext.PageSize, catgenContext.PageNumber);
		
		List<Product> networkMarketFeaturedProducts = ProductFactory.getNetworkMarketOwnFeaturedProducts(conn, networkMarket.NetworkMarketID);

%>
<%@ include file="MasterTop.jsp" %>

<P class="bigestFont">&nbsp;&nbsp;Searched for "<%= StringEscapeUtils.escapeHtml( Utils.getSafeString( request.getParameter("search" ))) %>"</p>

<P>
Sample of complex search:
</P>
<P>
<form action="Search.html">
<form method=get action="<%= catgenContext.RelativePath %>Search.html">
<% if(catgenContext.Company != null) { %>
<input type="hidden" name="company" value="<%= StringEscapeUtils.escapeHtml( Utils.getSafeString( catgenContext.Company.Code )) %>"/>
<% } %>
Keyword1: <input type="text" class="smallestFont" name="search" value="<%= StringEscapeUtils.escapeHtml( Utils.getSafeString( request.getParameter("search" ))) %>" size="20"><br/>
Keyword2: <input type="text" class="smallestFont" name="search2" value="<%= StringEscapeUtils.escapeHtml( Utils.getSafeString( request.getParameter("search2" ))) %>" size="20"><br/>
Country: <input type="text" class="smallestFont" name="searchCountry" value="<%= StringEscapeUtils.escapeHtml( Utils.getSafeString( request.getParameter("searchCountry" ))) %>" size="20"><br/><br/>
Region: <select name="searchRegion">
<option value="">[select one]</option>
<option value="AF" <% if( "AF".equalsIgnoreCase( region ) ) { %> selected <% } %>>Africa</option>
<option value="AS" <% if( "AS".equalsIgnoreCase( region ) ) { %> selected <% } %>>Asia</option>
<option value="EU" <% if( "EU".equalsIgnoreCase( region ) ) { %> selected <% } %>>Europe</option>
<option value="NA" <% if( "NA".equalsIgnoreCase( region ) ) { %> selected <% } %>>North America</option>
<option value="SA" <% if( "SA".equalsIgnoreCase( region ) ) { %> selected <% } %>>South America</option> 
<option value="OC" <% if( "OC".equalsIgnoreCase( region ) ) { %> selected <% } %>>Oceania</option>
</select>
<input type=submit value="Go!" class="smallestFont textColor" style="BACKGROUND-COLOR:  {{company.Background}};"><br/>
</form>
</P>

<%
		if(products != null && products.size() > 0)
		{
%>
<table width="90%">

<% 
int rowNumber = 0;

for( Product product : products ) { 

	Company company = CompanyFactory.getCachedCompanyByCode(conn, product.NetworkMarketID, product.CompanyCode);
	
	String productURL = Utils.getReferralURL( catgenContext.RelativePath, catgenContext.ReferralID, company.Code, "product,search", Utils.getProductURL(company, product, catgenContext.RelativePath, catgenContext.ReferralID), product.Code );


    		if(rowNumber % 4 == 0) {
    	%>
<tr>
<% 			} %>
    <td valign="bottom" align="center"><% if( product.ImageURL != null && product.ImageURL.length() > 0 ) { %><a href="<%= productURL %>"><img style="max-width: 120px; max-height: 120px;" src="<%=product.ImageURL%>" border="0"></a><br><% } %>
    <a href="<%= productURL %>"><%= StringEscapeUtils.escapeHtml(product.Name) %></a><% if( product.Price != null && product.Price.length() > 0 ) { %><br><% if( company.Currency != null && company.Currency.length() > 0 ) { %><%= StringEscapeUtils.escapeHtml( Utils.getSafeString( company.Currency )) %><% } %> <%= StringEscapeUtils.escapeHtml(product.Price) %><% } %><br></td>
   		<%	if(rowNumber % 4 == 3) {
    	%>
</tr>
    <%
   			}

			rowNumber++;
    	} %>

</table>

<% } else { %>
<center><h2>No Products Found</h2></center>
<% } 

if(catgenContext.PageSize > 0 && allProducts != null && allProducts.size() > catgenContext.PageSize)
{
	String pagerURL = "Search.html?" + request.getQueryString();
%>
<br><br><center><%@ include file="PagerControl.jsp" %></center>
<% } %>

<%@ include file="MasterBottom.jsp" %>
<%
	} finally {
		conn.close();
	}
%>
