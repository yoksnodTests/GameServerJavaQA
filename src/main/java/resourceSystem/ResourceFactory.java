package resourceSystem;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import utils.ReflectionHelper;

public class ResourceFactory {

    private static ResourceFactory resourceFactory;
    private VirtualFileSystem vfs;
    private Iterator<String> fileIter;
    private Map<String, Resource> resources;

    private Resource resource;


    private ResourceFactory() throws ParserConfigurationException, SAXException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        this.vfs = VirtualFileSystemImpl.getInstance();
        resources = new HashMap<>();
        loadResources();
    }

    public static ResourceFactory getInstance() throws ParserConfigurationException, SAXException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        if (resourceFactory == null) {
            resourceFactory = new ResourceFactory();
        }
        return resourceFactory;
    }


    public Resource get(String filename) {
        return resources.get(filename);
    }

    public void loadResources() throws ParserConfigurationException, SAXException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        fileIter = vfs.getIterator(File.separator);
        while (fileIter.hasNext()) {
            File file = new File(fileIter.next());
            if (file.isFile()) {
                resources.put(file.getName(), getResourceFromFile(file));
            }
        }
    }

    private Resource getResourceFromFile(File xmlFile) throws ParserConfigurationException, SAXException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        visit(doc, 0);
        return this.resource;
    }

    private void visit(Node node, int level) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            DOMException, NoSuchFieldException, SecurityException,
            IllegalArgumentException {

        NodeList nl = node.getChildNodes();
        String parent = "";
        for (int i = 0, cnt = nl.getLength(); i < cnt; i++) {
            if (nl.item(i).getNodeType() == Node.TEXT_NODE) { // if1
                parent = nl.item(i).getParentNode().getNodeName();
                if (!nl.item(i).getNodeValue().contains(" ") && !nl.item(i).getNodeValue().contains("\t")
                        && !nl.item(i).getNodeValue().contains("\n")) {
                    if (parent == "class")
                        this.resource = (Resource) ReflectionHelper.createInstance(nl.item(i).getNodeValue());
                    else {
                        ReflectionHelper.setFieldValue(resource, parent, nl.item(i).getNodeValue());
                    }
                }
            }
            visit(nl.item(i), level + 1);
        }
    }
}
