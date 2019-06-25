package hu.donat.mutcont;

import java.io.InputStreamReader;
import java.util.List;

import com.google.common.io.CharStreams;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public final class ClasspathContainerInitializer extends org.eclipse.jdt.core.ClasspathContainerInitializer {

    private static final IPath ENTRIES_XML_PATH = new Path("entries.xml");

    public ClasspathContainerInitializer() {
    }

    @Override
    public void initialize(final IPath containerPath, final IJavaProject javaProject) throws CoreException {
        final IProject project = updateContainer(containerPath, javaProject);

        ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {

            @Override
            public void resourceChanged(IResourceChangeEvent event) {
                try {
                    event.getDelta().accept(new IResourceDeltaVisitor() {

                        @Override
                        public boolean visit(IResourceDelta delta) throws CoreException {
                            IResource resource = delta.getResource();
                            if (resource != null && resource.getProject() != null && resource.getLocation() != null && resource.getProject().getLocation() != null
                                    && resource.getProject().equals(project) && resource.getFullPath().makeRelativeTo(project.getFullPath()).equals(ENTRIES_XML_PATH)) {
                                updateContainer(containerPath, javaProject);
                                return false;
                            }
                            return true;
                        }
                    });
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private IProject updateContainer(IPath containerPath, IJavaProject javaProject) {
        final IProject project = javaProject.getProject();
        IFile file = project.getFile(ENTRIES_XML_PATH);

        try {
            String content = CharStreams.toString(new InputStreamReader(file.getContents(), file.getCharset()));
            List<IClasspathEntry> entries = ClasspathConverter.toEntries(javaProject, content);
            MutableClasspathContainer container = new MutableClasspathContainer(entries.toArray(new IClasspathEntry[0]));
            JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { javaProject }, new IClasspathContainer[] { container }, null);

        } catch (Exception e) {
            Activator.log().log(new Status(IStatus.WARNING, Activator.ID, e.getMessage(), e));
        }
        return project;
    }

}
