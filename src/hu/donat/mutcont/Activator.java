package hu.donat.mutcont;

import org.osgi.framework.BundleContext;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Plugin;

public class Activator extends Plugin {

    public static final String ID = "hu.donat.mutcont";

    private static ILog log;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        this.log = log();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        log = null;
        super.stop(context);
    }

    public static ILog log() {
        return log;
    }
}
