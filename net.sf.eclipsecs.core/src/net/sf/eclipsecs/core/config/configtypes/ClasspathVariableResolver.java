//============================================================================
//
// Copyright (C) 2003-2023  David Schneider, Lars Ködderitzsch
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
//
//============================================================================

package net.sf.eclipsecs.core.config.configtypes;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaCore;

import com.puppycrawl.tools.checkstyle.PropertyResolver;

/**
 * Property resolver that tries to resolve values from classpath variables.
 *
 */
public class ClasspathVariableResolver implements PropertyResolver {

  @Override
  public String resolve(String aName) {

    IPath var = JavaCore.getClasspathVariable(aName);
    return var != null ? var.toString() : null;
  }
}
