package resourceSystem;

/**
 * Created by d.donskoy
 */

import com.google.inject.AbstractModule;

public class VFSInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(VirtualFileSystem.class).to(VirtualFileSystemImpl.class);
    }

}
