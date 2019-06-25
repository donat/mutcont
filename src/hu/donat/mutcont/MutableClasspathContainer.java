package hu.donat.mutcont;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;

public final class MutableClasspathContainer implements IClasspathContainer {

    public static final Path PATH = new Path("hu.donat.mutcont.container");

    private final IClasspathEntry[] entries;

    public MutableClasspathContainer(IClasspathEntry[] entries) {
        this.entries = entries;
    }

    @Override
    public IClasspathEntry[] getClasspathEntries() {
        return this.entries;
    }

    @Override
    public String getDescription() {
        return "Container populated from entries.xml";
    }

    @Override
    public int getKind() {
        return IClasspathContainer.K_APPLICATION;
    }

    @Override
    public IPath getPath() {
        return PATH;
    }

}