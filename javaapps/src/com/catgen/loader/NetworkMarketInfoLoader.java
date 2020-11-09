# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

package com.catgen.loader;

public class NetworkMarketInfoLoader extends DataLoader {
	
	public NetworkMarketInfoLoader()
	{
		super();
		
		tableName = "NetworkMarkets";
		IsRowData = false;

		required.add("name");
		
		dbColumn.put("name", new DataDefinition( "name"));
		dbColumn.put("company name", new DataDefinition( "name"));
		dbColumn.put("company", new DataDefinition( "name"));

		dbColumn.put("descr", new DataDefinition( "description"));
		dbColumn.put("description", new DataDefinition( "description"));
		dbColumn.put("company description", new DataDefinition( "description"));

		dbColumn.put("logo url", new DataDefinition( "logo" ));
		dbColumn.put("image", new DataDefinition( "logo"));
		dbColumn.put("image url", new DataDefinition( "logo"));
		dbColumn.put("imageurl", new DataDefinition( "logo"));
		dbColumn.put("company image", new DataDefinition( "logo"));

		dbColumn.put("url", new DataDefinition( "url"));
		dbColumn.put("company url", new DataDefinition( "url"));

		dbColumn.put("headerurl", new DataDefinition( "headerurl"));
		dbColumn.put("header url", new DataDefinition( "headerurl"));
		dbColumn.put("company header url", new DataDefinition( "headerurl"));

		dbColumn.put("footerurl", new DataDefinition( "footerurl"));
		dbColumn.put("footer url", new DataDefinition( "footerurl"));
		dbColumn.put("company footer url", new DataDefinition( "footerurl" ));

		dbColumn.put("address", new DataDefinition( "address"));
		dbColumn.put("company address", new DataDefinition( "address"));

		dbColumn.put("address2", new DataDefinition( "address2"));
		dbColumn.put("company address2", new DataDefinition( "address2"));

		dbColumn.put("city", new DataDefinition( "city"));
		dbColumn.put("company city", new DataDefinition( "city"));

		dbColumn.put("state", new DataDefinition( "state"));
		dbColumn.put("company state", new DataDefinition( "state"));

		dbColumn.put("country", new DataDefinition( "country"));
		dbColumn.put("company country", new DataDefinition( "country"));

		dbColumn.put("zip", new DataDefinition( "zip"));
		dbColumn.put("country zip", new DataDefinition( "zip"));

		dbColumn.put("header", new DataDefinition( "header"));
		dbColumn.put("company header", new DataDefinition( "header"));

		dbColumn.put("footer", new DataDefinition( "footer"));
		dbColumn.put("company footer", new DataDefinition( "footer"));

		dbColumn.put("rate", new DataDefinition( "rate"));
		dbColumn.put("order", new DataDefinition( "rate"));

		dbColumn.put("template", new DataDefinition( "template"));
	}

}
