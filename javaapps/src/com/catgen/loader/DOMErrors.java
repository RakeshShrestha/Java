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

import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSParserFilter;

class DOMErrors
  implements DOMErrorHandler, LSParserFilter
{
  public boolean handleError(DOMError error)
  {
    return true;
  }

  public short acceptNode(Node enode)
  {
    return 1;
  }

  public int getWhatToShow()
  {
    return 1;
  }

  public short startElement(Element elt)
  {
    return 1;
  }
}