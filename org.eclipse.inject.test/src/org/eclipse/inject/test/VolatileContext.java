package org.eclipse.inject.test;

import java.util.Collections;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.internal.contexts.ContextChangeEvent;
import org.eclipse.e4.core.internal.contexts.EclipseContext;

@SuppressWarnings("restriction")
public class VolatileContext extends EclipseContext {

	public VolatileContext(IEclipseContext parent) {
		super(parent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object internalGet(EclipseContext originatingContext, String name,
			boolean local) {
		Object o = super.internalGet(originatingContext, name, local);
		originatingContext.invalidate(name, ContextChangeEvent.REMOVED, null, null, Collections.EMPTY_SET);
		return o;
	}

}

