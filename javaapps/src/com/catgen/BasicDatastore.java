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

package com.catgen;

import org.w3c.dom.Element;

public abstract class BasicDatastore {

	public abstract void Save();
	public abstract void Load();
	public abstract void AddToXml(Element element);
}
