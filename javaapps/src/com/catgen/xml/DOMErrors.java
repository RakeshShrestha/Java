# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

package com.catgen.xml;

import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSParserFilter;
import org.w3c.dom.traversal.NodeFilter;

public class DOMErrors implements DOMErrorHandler, LSParserFilter
{
	public boolean handleError(DOMError error){
		short severity = error.getSeverity();

		if (severity == DOMError.SEVERITY_ERROR) {
			System.out.println("[dom3-error]: "+error.getMessage());
		}

		if (severity == DOMError.SEVERITY_WARNING) {
			System.out.println("[dom3-warning]: "+error.getMessage());
		}

		return true;

	}
	/**
	 * @see org.w3c.dom.ls.LSParserFilter#acceptNode(Node)
	 */
	public short acceptNode(Node enode) {
		return NodeFilter.FILTER_ACCEPT;
	}

	/**
	 * @see org.w3c.dom.ls.LSParserFilter#getWhatToShow()
	 */
	public int getWhatToShow() {
		return NodeFilter.SHOW_ELEMENT;
	}

	/**
	 * @see org.w3c.dom.ls.LSParserFilter#startElement(Element)
	 */
	public short startElement(Element elt) {
		return LSParserFilter.FILTER_ACCEPT;
	}
}
