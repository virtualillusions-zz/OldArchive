
import java.io.FileInputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kyle Williams
 */
public class test {
    public static void main (String argv []){
        test app = new test();
    }
    public test(){
           try{
            FileInputStream xmlInput = new FileInputStream("assets/Models/characterList.xml");
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();

            SaxHandler handler = new SaxHandler();
            saxParser.parse(xmlInput,handler);
            
        } catch(Throwable err){
            err.printStackTrace();
        }
    }

    public class SaxHandler extends DefaultHandler {
        @Override
    public void startDocument() throws SAXException {    }
        @Override
    public void endDocument() throws SAXException {    }
    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException {if(qName.equals("character"))System.out.println(attributes.getValue("name"));   }
        @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException {    }
        @Override
    public void characters(char ch[], int start, int length)
        throws SAXException {    }

        @Override
    public void ignorableWhitespace(char ch[], int start, int length)
        throws SAXException {    }
    }
}
