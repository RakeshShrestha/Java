# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

package com.catgen.loader;

public class FeaturedProductsLoader extends DataLoader 
{
	public FeaturedProductsLoader()
	{
		super();
		
		tableName = "FeaturedProducts";
		IsRowData = true;
		IncludeRowID = true;

		required.add("companycode");
		required.add("productcode");
		
		dbColumn.put("company code", new DataDefinition( "companycode", DataDefinition.STRING, DataDefinition.LOADER_TRIM));
		dbColumn.put("vendor code", new DataDefinition( "companycode", DataDefinition.STRING, DataDefinition.LOADER_TRIM));

		dbColumn.put("product code", new DataDefinition( "productcode", DataDefinition.STRING, DataDefinition.LOADER_TRIM));
		dbColumn.put("product", new DataDefinition( "productcode", DataDefinition.STRING, DataDefinition.LOADER_TRIM));

		dbColumn.put("description", new DataDefinition( "description"));
	}	
}
