/*
 * Copyright 2022 Allette Systems (Australia)
 * http://www.allette.com.au
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pageseeder.schematron.svrl;

import javax.xml.namespace.NamespaceContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for SVRL
 *
 * @author Christophe Lauret
 *
 * @version 2.0
 * @since 2.0
 */
public final class SVRL {

  public final static String NAMESPACE_URI = "http://purl.oclc.org/dsdl/svrl";

  /**
   * Replace the namespace URI in the location attribute by the prefix used for them if any.
   *
   * <p>If there is no available prefix, the namespace URI is left in place</p>
   *
   * @param location The location attribute
   * @param context  The namespace context
   *
   * @return The updated location
   */
  public static String toLocationPrefix(String location, NamespaceContext context) {
    Pattern p = Pattern.compile("Q\\{([^}]*)}");
    Matcher m = p.matcher(location);
    StringBuffer update = new StringBuffer();
    while (m.find()) {
      String namespaceURI = m.group(1);
      String prefix = context.getPrefix(namespaceURI);
      if (prefix == null) {
        if (namespaceURI.isEmpty() && context.getNamespaceURI("") == null) {
          m.appendReplacement(update, "");
        } else {
          m.appendReplacement(update, m.group());
        }
      } else {
        m.appendReplacement(update, prefix+":");
      }
    }
    m.appendTail(update);
    return update.toString();
  }
}
