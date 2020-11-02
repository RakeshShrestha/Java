<%@ page
	import="java.util.*, org.apache.commons.lang.*,java.sql.*,com.catgen.*,com.catgen.factories.*,java.net.URLEncoder"%>
<%
	CatgenPageContext catgenContext = CatgenPageContext.getInstance(pageContext);

	NetworkMarket networkMarket = catgenContext.NetworkMarket;
	if (networkMarket == null) 
	{
		response.sendRedirect("CompanyNotFound.jsp");
		return;
	}
	
	Company member = catgenContext.Company;
	if (member == null) {
		response.sendRedirect("MemberNotFound.jsp");
		return;
	}
	
	catgenContext.DefaultPageName = "Products of \"" + member.Name + "\"";

	Connection conn = MySqlDB.getDBConnection(getServletContext());
	try {
		List<Product> products = ProductFactory.getProducts( conn, networkMarket.NetworkMarketID, member.Code);

%>
<%@ include file="MasterTop.jsp" %>

<%
		if(products != null && products.size() > 0)
		{
%>
<table width="90%">

<% 
int rowNumber = 0;

for( Product product : products ) { 

	String productURL = Utils.getReferralURL( catgenContext.RelativePath, catgenContext.ReferralID, member.Code, "product", Utils.getProductURL(member, product, catgenContext.RelativePath, catgenContext.ReferralID), product.Code );
	String currency = Utils.getCurrencySymbol(member, product);


    		if(rowNumber % 4 == 0) {
    	%>
<tr> 
<% 			} %>
    <td valign="bottom" align="center"><% if( product.ImageURL != null && product.ImageURL.length() > 0 ) { %><a href="<%= productURL %>"><img style="max-width: 120px; max-height: 120px;" src="<%=product.ImageURL%>" border="0"></a><br><% } %>
    <a href="<%= productURL %>"><%= StringEscapeUtils.escapeHtml(product.Name) %></a><% if( product.Price != null && product.Price.length() > 0 ) { %><br><%= currency %> <%= StringEscapeUtils.escapeHtml(product.Price) %><% } %><br></td>
   		<%	if(rowNumber % 4 == 3) {
    	%>
</tr>
    <%
   			}

			rowNumber++;
    	} %>

</table>

<% } else { %><br/><br/>
<center><h2>Products Not Found</h2></center>
<% } %>


<%@ include file="MasterBottom.jsp" %>
<%
	} finally {
		conn.close();
	}
%>
