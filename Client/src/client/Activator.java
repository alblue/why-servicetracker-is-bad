package client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	ServiceTracker<Runnable, ?> serviceTracker;

	public void start(BundleContext bundleContext) throws Exception {
		serviceTracker = new ServiceTracker<>(bundleContext, Runnable.class, null);
		if (!Boolean.getBoolean("disableOpen")) {
			serviceTracker.open(); // This will cause a DS component to be instantiated even though we don't use it
		}
	}

	public void stop(BundleContext bundleContext) throws Exception {
	}

}
