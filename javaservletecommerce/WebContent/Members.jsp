<%@ page
	import="java.util.*, org.apache.commons.lang.*,java.sql.*,com.catgen.*,com.catgen.factories.*, java.text.*"%>
<%
	CatgenPageContext catgenContext = CatgenPageContext.getInstance(pageContext);

	NetworkMarket networkMarket = catgenContext.NetworkMarket;
	if (networkMarket == null) {
		response.sendRedirect("CompanyNotFound.jsp");
		return;
	}

	Connection conn = MySqlDB.getDBConnection(getServletContext());
	try {
		List<Company> companies = CompanyFactory.getNetmarketMembers(conn, networkMarket.NetworkMarketID);
		if (companies == null || companies.size() == 0) {
			response.sendRedirect("MembersNotFound.jsp");
			return;
		}
		
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		response.setHeader("Expires", df.format(Calendar.getInstance().getTime() ));
%>
<%@ include file="MasterTop.jsp" %>

<%@page import="java.net.URLEncoder"%><h2>Members</h2> 
    <% for( Company company : companies ) {  %>
        <a href="<%= URLEncoder.encode(company.Code, "UTF-8" ) %>/Member.html"><%= StringEscapeUtils.escapeHtml(company.Name) %></a> <%= StringEscapeUtils.escapeHtml(company.Country) %> <a href="<%= URLEncoder.encode(company.Code, "UTF-8" ) %>/ProductLine.html">Products of <%= StringEscapeUtils.escapeHtml(company.Name) %></a><hr size="1">
 	<br>
    <% } %>

<br><br>
<%@ include file="MasterBottom.jsp" %>
 <%
	} finally {
		conn.close();
	}
%>