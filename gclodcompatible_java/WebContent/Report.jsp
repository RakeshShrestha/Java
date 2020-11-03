<%@ page
	import="org.apache.commons.lang.*,java.sql.*,com.catgen.*,com.catgen.factories.*, java.text.*, java.util.*"%>
<%
	CatgenPageContext catgenContext = CatgenPageContext.getInstance(pageContext);
	
	NetworkMarket networkMarket = catgenContext.NetworkMarket;
	if (networkMarket == null) {
		response.sendRedirect("CompanyNotFound.jsp");
		return;
	}

	Connection conn = MySqlDB.getDBConnection(getServletContext());
	try 
	{
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		response.setHeader("Expires", df.format(Calendar.getInstance().getTime() ));
%>
<%@ include file="MasterTop.jsp" %>
<br>
<br>

<form method="get" action="Report.html">
<input type="hidden" name="action" value="1"/>
<select name="template">
<%
	for(com.catgen.reports.Report report: ReportFactory.getReports(conn))
	{
%>
<option value="<%= StringEscapeUtils.escapeHtml(report.TemplateURL ) %>"><%= StringEscapeUtils.escapeHtml( report.ReportName ) %></option>
<%		
	}
%>
</select>
<br/>
<input type="submit" value="Continue"/> <input type="submit" name="cancel" value="Cancel"/>
</form>

<%@ include file="MasterBottom.jsp" %>
<%
	} finally {
		conn.close();
	}
%>
