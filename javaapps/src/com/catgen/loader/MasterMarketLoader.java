# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

package com.catgen.loader;

public class MasterMarketLoader extends DataLoader
{
	public MasterMarketLoader()
	{
		super();

		tableName = "MasterMarkets";

		required.add("marketid");
		required.add("marketsurl");
		required.add("marketinfourl");
		required.add("pagesurl");

		dbColumn.put("network market code", new DataDefinition( "marketid"));
		dbColumn.put("network market members url", new DataDefinition( "marketsurl"));
		dbColumn.put("network market info url", new DataDefinition( "marketinfourl"));
		dbColumn.put("network market pages url", new DataDefinition( "pagesurl"));
		dbColumn.put("network market featured products url", new DataDefinition( "featuredproductsurl"));
		dbColumn.put("description", new DataDefinition( "description"));
		dbColumn.put("network market domain name", new DataDefinition( "domainname"));
	}
}
