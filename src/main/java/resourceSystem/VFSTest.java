package resourceSystem;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by d.donskoy
 */
public class VFSTest {

    private static final String DB_RES = "DatabaseResourсe.xml";

    @Test
    public void injectVFS(){
        Injector injector = Guice.createInjector(new VFSInjector());
        ResourceFactory resFactory = injector.getInstance(ResourceFactory.class);
        Resource resource = resFactory.get(DB_RES);
        Assert.assertNotNull(resource);
    }
}
