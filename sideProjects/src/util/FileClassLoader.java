package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jme.scene.Node;
 
public class FileClassLoader extends ClassLoader
{	/**loads all Classes of type Node from a given directory to a Map*/
    public FileClassLoader() { }
    
    private <A> Map<String, Node> loadClassesToMap(File searchDirectory, Class<A> searchKlass)
        throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException
    {
        Map<String, Node> nodeMap = new HashMap<String, Node>();
 
        for (File file : searchDirectory.listFiles()) {
            if (file.isFile()) {
                // get the name
                String nodeName = file.getName();
                int dotLocation = nodeName.lastIndexOf(".");
                nodeName = nodeName.substring(0, dotLocation);
                // get the class
                Class<A> nodeClass = loadFileClass(file, searchKlass);
 
                // add to the map
                nodeMap.put( nodeName, (Node)nodeClass.newInstance() );
            }
        }
 
        return nodeMap;
    }

    @SuppressWarnings("unchecked")
    private <A> Class<A> loadFileClass(File file, Class<A> loadClass)
            throws FileNotFoundException, IOException
    {
        FileInputStream input = new FileInputStream(file);
        byte[] bytes = new byte[input.available()];
        input.read(bytes, 0, bytes.length);
        Class<?> loadedKlass = defineClass(null, bytes, 0, bytes.length);
        
        if (loadClass.isAssignableFrom(loadedKlass)) {
            resolveClass(loadedKlass);
            return (Class<A>) loadedKlass;
        } else {
            return null;
        }
    }
     
    /**
     * returns a list of all Node extended classes in the directory
     * @param searchDirectory
     * @return loader 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Map<String, Node> loadNodeClasses(File searchDirectory)
    throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException
    {
    	FileClassLoader loader = new FileClassLoader();
    	return loader.loadClassesToMap(searchDirectory, Node.class);
    }
    
    
}