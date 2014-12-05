package org.eclipse.inject.test;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProviderTest {

	public static class MyDependency{
		Object firstDependency;
		Object secondDependency;

		@Inject Provider<Object> objectProvider;
		
		public Object createDependency(){
			return objectProvider.get();
		}
		
		@Inject
		public MyDependency(Object a, Object b){
			firstDependency = a;
			secondDependency = b;
		}
	
	}
	
	
	private IEclipseContext context;
	
	@Before
	public void init(){
		context = new VolatileContext(null);
		context.set(Object.class.getName(), new ContextFunction() {
			@Override
			public Object compute(IEclipseContext context, String contextKey) {
				return new Object();
			}
		});
	}
	
	@Test
	public void fieldInjectionsShouldGetSeparateInstances(){
		MyDependency dependency = ContextInjectionFactory.make(MyDependency.class, context);

		Object first = dependency.firstDependency;
		Object second = dependency.secondDependency;
		Assert.assertNotSame(first, second);
	}
	
	@Test
	public void providerInjectionShouldAlwaysCallTheFunction(){
		MyDependency dependency = ContextInjectionFactory.make(MyDependency.class, context);

		Object first = dependency.createDependency();
		Object second = dependency.createDependency();
		Assert.assertNotSame(first, second);
	}
	
	@Test
	public void instanceInContextShouldBeSingleton(){
		context.set(Object.class, new Object()); //overwrite the contextFunction
		MyDependency dependency = ContextInjectionFactory.make(MyDependency.class, context);

		Object first = dependency.firstDependency;
		Object second = dependency.secondDependency;
		Assert.assertSame(first, second);
	}
	
	@Test
	public void instanceInContextShouldBeSingletonForProvider(){
		context.set(Object.class, new Object()); //overwrite the contextFunction
		MyDependency dependency = ContextInjectionFactory.make(MyDependency.class, context);

		Object first = dependency.createDependency();
		Object second = dependency.createDependency();
		Assert.assertSame(first, second);
	}
	
}
