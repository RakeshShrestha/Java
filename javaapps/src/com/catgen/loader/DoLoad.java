# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

package com.catgen.loader;

public class DoLoad {

	public static void main( String[] argv) {

		MasterMarketLoader masterMarket = new MasterMarketLoader();
		masterMarket.LoadData(null, "http://spreadsheets.google.com/feeds/cells/................./od6/public/basic");
		
		/*MembersLoader membersLoader = new MembersLoader();
		membersLoader.ClearExtras();
		membersLoader.AddExtras("MarketID", "market2");
		membersLoader.LoadData("http://spreadsheets.google.com/feeds/cells/.............../od6/public/basic");
		
		NetworkMarketInfoLoader networkMarketInfoLoader = new NetworkMarketInfoLoader();
		networkMarketInfoLoader.ClearExtras();
		networkMarketInfoLoader.AddExtras("MarketID", "market2");
		networkMarketInfoLoader.LoadData("http://spreadsheets.google.com/feeds/cells/.............../od7/public/basic");*/
	}
}

