<%@ page
	import="java.util.*, org.apache.commons.lang.*,java.sql.*,com.catgen.*,com.catgen.factories.*, java.text.*,java.net.URLEncoder"%>
<%
	CatgenPageContext catgenContext = CatgenPageContext.getInstance(pageContext);

	NetworkMarket networkMarket = catgenContext.NetworkMarket;
	if (networkMarket == null) 
	{
		response.sendRedirect("MemberNotFound.jsp");
		return;
	}
	
	Connection conn = MySqlDB.getDBConnection(getServletContext());
	try {
		List<Product> products = null;
		if(catgenContext.Search == null) 
			products = ProductFactory.getMarketProducts( conn, networkMarket.NetworkMarketID);
		else
			products = ProductFactory.getMarketProducts( conn, networkMarket.NetworkMarketID, catgenContext.Search);

		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		response.setHeader("Expires", df.format(Calendar.getInstance().getTime() ));
%>
<%@ include file="MasterTop.jsp" %>
    
<h2><a href="Members.html"><font size="4">Members</font></a></h2>

    <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1">
      <tr>
        <td width="29%"><font size="4">Products</font></td>
        <td width="71%" align="right"><form method=get action="Home.html">
<input type="text" name="search" value="<%if(catgenContext.Search != null) out.write( StringEscapeUtils.escapeHtml(catgenContext.Search) ); %>" size="20">
<input type=submit value="Search">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
</form>
</td>
      </tr>
    </table>

    <p>&nbsp;</p>

<%
		if(products == null ||products.size() == 0)
		{
%>
<h2>products not found</h2>
<%		} else { %>


<table>

    <%  int rowNumber = 0;
    	for( Product product : products ) {
    		String productURL;

    		Company company = CompanyFactory.getCachedCompanyByCode(conn, networkMarket.NetworkMarketID, product.CompanyCode);
    		
    		if(product.URL != null && product.URL.length() > 0)
    			productURL = product.URL.replace( "$$productcode$$", product.Code );
    		else if(company.ProductURL != null && company.ProductURL.length() > 0)
    			productURL = company.ProductURL.replace( "$$productcode$$", product.Code );
    		else
    			productURL = URLEncoder.encode(product.CompanyCode, "UTF-8") + "/Product.html?productid=" + URLEncoder.encode(product.Code, "UTF-8");
    		
    		if(rowNumber % 4 == 0) {
    	%>
<tr>
<% 			} %>
    <td align="center"><% if( product.ImageURL != null && product.ImageURL.length() > 0 ) { %><a href="<%= productURL %>"><img style="max-width: 120px; max-height: 120px;" src="<%=product.ImageURL%>" border="0"></a><br><% } %>
        <a href="<%= productURL %>"><%=StringEscapeUtils.escapeHtml(product.Name)%></a>
        <% if( product.Price != null && product.Price.length() > 0 ) { %>
         <br><center>$<%=StringEscapeUtils.escapeHtml(product.Price)%></center>
         <% } %>
    </td>
<%
   			if(rowNumber % 4 == 3) {
    	%>
</tr>
    <%
   			}

			rowNumber++;
    	} %>
</table>
<% } %>
<%@ include file="MasterBottom.jsp" %>
<%
	} finally {
		conn.close();
	}
%>

