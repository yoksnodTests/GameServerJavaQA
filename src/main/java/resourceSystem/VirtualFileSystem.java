package resourceSystem;

import java.util.Iterator;

public interface VirtualFileSystem {
    Iterator<String> getIterator(String startDir);
}
