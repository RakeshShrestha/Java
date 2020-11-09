# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

package com.catgen.loader;

public class SpreadsheetData {
	public String Column;
	public int Row;

	public String value;

	public void print()
	{
		System.out.println( "column: " + Column + " row " + Row + " value " + value);	
	}
}
