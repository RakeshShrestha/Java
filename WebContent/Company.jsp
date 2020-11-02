<%@ page
	import="org.apache.commons.lang.*,java.sql.*,com.catgen.*,com.catgen.factories.*, java.text.*, java.util.*"%>
<%
	CatgenPageContext catgenContext = CatgenPageContext.getInstance(pageContext);

	Connection conn = MySqlDB.getDBConnection(getServletContext());
	try {
		NetworkMarket networkMarket = catgenContext.NetworkMarket;
		if (networkMarket == null) {
			response.sendRedirect("CompanyNotFound.jsp");
			return;
		}
		
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		response.setHeader("Expires", df.format(Calendar.getInstance().getTime() ));
		
%>
<%@ include file="MasterTop.jsp" %>
<h2>Company <%=StringEscapeUtils.escapeHtml(networkMarket.Name)%></h2>

<br>
<br>
<%=StringEscapeUtils.escapeHtml(networkMarket.Description)%><br>
<br>
<%
	if (networkMarket.LogoImage != null && networkMarket.LogoImage.length() > 0) {
%>
<img src="<%=StringEscapeUtils.escapeHtml(networkMarket.LogoImage)%>" />
<%
	}
%>
<%@ include file="MasterBottom.jsp" %>
<%
	} finally {
		conn.close();
	}
%>