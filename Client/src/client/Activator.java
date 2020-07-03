package client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activator implements BundleActivator {

	ServiceTracker<Runnable, ?> serviceTracker;

	@Override
	public void start(BundleContext bundleContext) throws Exception {

		serviceTracker = new ServiceTracker<>(bundleContext, Runnable.class, new ServiceTrackerCustomizer<Runnable, LazyRunnable>() {

			@Override
			public LazyRunnable addingService(ServiceReference<Runnable> reference) {

				System.out.println("Adding service lazy...");
				return new LazyRunnable(reference, bundleContext);
			}

			@Override
			public void modifiedService(ServiceReference<Runnable> reference, LazyRunnable service) {

				// we don't mind
			}

			@Override
			public void removedService(ServiceReference<Runnable> reference, LazyRunnable service) {

				service.dispose();
			}
		});
		if(!Boolean.getBoolean("disableOpen")) {
			serviceTracker.open(); // This will NOT cause a DS component to be instantiated
		}
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {

	}

	private static class LazyRunnable implements Runnable {

		private ServiceReference<Runnable> reference;
		private BundleContext bundleContext;

		public LazyRunnable(ServiceReference<Runnable> reference, BundleContext bundleContext) {

			this.reference = reference;
			this.bundleContext = bundleContext;
		}

		public void dispose() {

			// in a real world example one might want to do something here to cleanup/shutdown things
		}

		@Override
		public void run() {

			Runnable runnable = bundleContext.getService(reference);
			if(runnable == null) {
				throw new IllegalStateException("underlying service has vanished");
			}
			try {
				runnable.run();
			} finally {
				bundleContext.ungetService(reference);
			}
		}
	}
}
