<%@ page
	import="org.apache.commons.lang.*,java.sql.*,com.catgen.*,com.catgen.factories.*"%>
<%
	CatgenPageContext catgenContext = CatgenPageContext.getInstance(pageContext);

	NetworkMarket networkMarket = catgenContext.NetworkMarket;
	if (networkMarket == null) 
	{
		response.sendRedirect("CompanyNotFound.jsp");
		return;
	}

	Company company = catgenContext.Company;
	if (company == null) {
		response.sendRedirect("MemberNotFound.jsp");
		return;
	}
	
	Connection conn = MySqlDB.getDBConnection(getServletContext());
	try {
%>
<%@ include file="MasterTop.jsp" %>
<h2>Company <%=StringEscapeUtils.escapeHtml(company.Name)%></h2>

<br>
<br>
<%=StringEscapeUtils.escapeHtml(company.Description)%><br>
<br>
<%
	if (company.LogoImage != null && company.LogoImage.length() > 0) {
%>
<img src="<%=StringEscapeUtils.escapeHtml(company.LogoImage)%>" />
<%
	}
%>
<%@ include file="MasterBottom.jsp" %>
<%
	} finally {
		conn.close();
	}
%>