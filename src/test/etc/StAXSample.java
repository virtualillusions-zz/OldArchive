package test.etc;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

/** @author Mark A. Ziesemer */
public class StAXSample{

  public static void main(String[] args) throws Exception{
    XMLStreamWriter xsw = XMLOutputFactory.newInstance()
      .createXMLStreamWriter(System.out);
    xsw = new IndentingXMLStreamWriter(xsw);
    xsw.writeStartDocument();
        xsw.writeStartElement("Root");
        xsw.writeAttribute("Name", "Value");
            xsw.writeEmptyElement("Child");
            xsw.writeAttribute("Name", "Value");     
        xsw.writeEndElement();
    xsw.writeEndDocument();
    xsw.close();   
  }
}