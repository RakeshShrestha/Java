/**
# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.
*/

package com.catgen.loader;

import java.sql.Connection;
import java.util.ArrayList;

public interface IExtaRowLoader 
{
	public void Load(Connection conn, DataLoader dataLoader,  ArrayList<SpreadsheetData> sheetData);
}
