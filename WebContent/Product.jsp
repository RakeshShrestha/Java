<%@ page
	import="org.apache.commons.lang.*,java.sql.*,com.catgen.*,com.catgen.factories.*, java.text.*, java.util.*"%>
<%
	CatgenPageContext catgenContext = CatgenPageContext.getInstance(pageContext);
	
	NetworkMarket networkMarket = catgenContext.NetworkMarket;
	if (networkMarket == null) {
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
		Product product = ProductFactory.getProductByCode(conn, networkMarket.NetworkMarketID, company.Code, catgenContext.ProductID);
		if (product == null) {
			response.sendRedirect("ProductNotFound.jsp");
			return;
		}

		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		response.setHeader("Expires", df.format(Calendar.getInstance().getTime() ));
%>
<%@ include file="MasterTop.jsp" %>
<br>
<br>

<table>
<tr><td>Code: </td><td><%=StringEscapeUtils.escapeHtml(product.Code)%></td></tr>
<tr><td>Name: </td><td><%=StringEscapeUtils.escapeHtml(product.Name)%></td></tr>
<tr><td>Company: </td><td><a href="/<%= StringEscapeUtils.escapeHtml(networkMarket.NetworkMarketID) %>/Member.html?companycode=<%=StringEscapeUtils.escapeHtml(company.Code)%>"><%=StringEscapeUtils.escapeHtml(company.Name)%></a></td></tr>
<%  if( product.Price != null && product.Price.length() > 0 ) { %>
<tr><td>Price: </td><td>$<%=StringEscapeUtils.escapeHtml(product.Price)%></td></tr>
<%  } %>
<% if( product.Description != null && product.Description.length() > 0 )  { %>
<tr><td colspan="2">Description:<br><%=StringEscapeUtils.escapeHtml(product.Description)%></td></tr>
<% } %>

<% if( product.ImageURL != null && product.ImageURL.length() > 0 ) { %>
<tr><td valign="top">Image: </td><td><img src="<%=StringEscapeUtils.escapeHtml(product.ImageURL)%>"></td></tr>
<% } %>

<% if( product.URL != null && product.URL.length() > 0 ) { %>
<tr><td colspan="2"><a href="<%=StringEscapeUtils.escapeHtml(product.URL)%>">More info</a></td></tr>  
<% } %>

<% if( company.PayPalEmail != null && company.PayPalEmail.length() > 0 ) { %>

<tr><td colspan="2">
<form target="paypal" action="https://www.paypal.com/cgi-bin/webscr"
method="post">
<!-- Identify your business so that you can collect the payments. -->
<input type="hidden" name="business" value="<%=StringEscapeUtils.escapeHtml(company.PayPalEmail)%>">
<!-- Specify a PayPal Shopping Cart Add to Cart button. -->
<input type="hidden" name="cmd" value="_cart">
<input type="text" name="add" value="1" size="5">
<!-- Specify details about the item that buyers will purchase. -->
<input type="hidden" name="item_name"
value="Birthday - Cake and Candle">
<input type="hidden" name="amount" value="3.95">
<input type="hidden" name="currency_code" value="<%=StringEscapeUtils.escapeHtml(product.Currency)%>">
<!-- Display the payment button. -->
<input type="image" name="submit" border="0"
src="https://www.paypal.com/en_US/i/btn/btn_cart_LG.gif"
alt="PayPal - The safer, easier way to pay online">
<img alt="" border="0" width="1" height="1"
src="https://www.paypal.com/en_US/i/scr/pixel.gif" >
</form>

</td></tr>  

<% } %>

<% if( company.GoogleMerchantID != null && company.GoogleMerchantID.length() > 0 ) { %>

<tr><td colspan="2">
<form method="POST"
  action="https://checkout.google.com/api/checkout/v2/checkoutForm/Merchant/<%=StringEscapeUtils.escapeHtml(company.GoogleMerchantID)%>"
      accept-charset="utf-8">

  <input type="hidden" name="item_name_1" value="<%=StringEscapeUtils.escapeHtml(product.Name)%>"/>
  <input type="hidden" name="item_description_1" value="<%=StringEscapeUtils.escapeHtml(product.Description)%>"/>
  <input type="text" name="item_quantity_1" value="1" size="5"/>
  <input type="hidden" name="item_price_1" value="<%=StringEscapeUtils.escapeHtml(product.Price)%>"/>
  <input type="hidden" name="item_currency_1" value="<%=StringEscapeUtils.escapeHtml(product.Currency)%>"/>


  <input type="hidden" name="_charset_"/>

  <input type="image" name="Google Checkout" alt="Fast checkout through Google"
src="http://checkout.google.com/buttons/checkout.gif?merchant_id=<%=StringEscapeUtils.escapeHtml(company.GoogleMerchantID)%>&w=180&h=46&style=white&variant=text&loc=en_US"
height="46" width="180"/>

</form>
 
</td></tr>  

<% } %>

</table>
<%@ include file="MasterBottom.jsp" %>
<%
	} finally {
		conn.close();
	}
%>
