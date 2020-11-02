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
<%@ include file="MasterTop_pigeon.jsp" %>

  <!--topbar-->
    <div id="content_top">
    <div class="leftc">
    <div class="rightc">
    </div>
    </div>
    </div>
    <!--topbar-->
     <!--midcontent-->
    <div id="content_mid">
      <div class="maincontent">
        <h2>About <%= networkMarket.Name %></h2>
        <p><%= networkMarket.Description %></p>
      </div>
    </div>
    <!--midcontent-->
    <!--bottom-->
    <div id="feature_btm">
    <div class="leftcb">
    <div class="rightcb">
    </div>
    </div>
    </div>
    <!--bottom end-->

<%
	if (networkMarket.LogoImage != null && networkMarket.LogoImage.length() > 0) {
%>
<img src="<%=StringEscapeUtils.escapeHtml(networkMarket.LogoImage)%>" />
<%
	}
%>
<%@ include file="MasterBottom_pigeon.jsp" %>
<%
	} finally {
		conn.close();
	}
%>