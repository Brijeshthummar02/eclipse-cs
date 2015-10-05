//============================================================================
//
// Copyright (C) 2002-2014  David Schneider, Lars Ködderitzsch
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
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//============================================================================

package net.sf.eclipsecs.core.config.configtypes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;

import com.puppycrawl.tools.checkstyle.PropertyResolver;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

/**
 * This property resolver is able to aggregate a list of child property resolvers, where each child resolver looks for
 * different properties and may have different ways of finding properties. The child resolvers are asked to resolve the
 * properties in the order they are added. This PropertyResolver adds the property chaining feature, to allow properties
 * within properties.
 *
 * @author Lars Ködderitzsch
 */
public class MultiPropertyResolver implements PropertyResolver, IContextAware {

    /** The list of PropertyResolvers. */
    private List<PropertyResolver> mChildResolver = new ArrayList<PropertyResolver>();

    /**
     * Adds a PropertyResolver to this aggregation property resolver.
     *
     * @param resolver
     *            the PropertyResolver to add
     */
    public void addPropertyResolver(PropertyResolver resolver) {
        mChildResolver.add(resolver);
    }

    /**
     * {@inheritDoc}
     */
    public void setProjectContext(IProject project) {

        // propagate context to the childs
        for (int i = 0, size = mChildResolver.size(); i < size; i++) {
            PropertyResolver aChildResolver = mChildResolver.get(i);
            if (aChildResolver instanceof IContextAware) {
                ((IContextAware) aChildResolver).setProjectContext(project);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public String resolve(String property) {

        String value = null;

        for (int i = 0, size = mChildResolver.size(); i < size; i++) {

            PropertyResolver aChildResolver = mChildResolver.get(i);
            value = aChildResolver.resolve(property);

            if (value != null) {
                break;
            }
        }

        try {

            // property chaining - might recurse internally
            while (PropertyUtil.hasUnresolvedProperties(value)) {
                value = PropertyUtil.replaceProperties(value, this);
            }
        }
        catch (CheckstyleException e) {
            throw new RuntimeException(e);
        }

        return value;
    }

}
