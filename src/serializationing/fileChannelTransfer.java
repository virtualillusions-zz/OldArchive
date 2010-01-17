package serializationing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

//optimal native operation transfers
//atempt to find best transfer size
public class fileChannelTransfer {
	//channel to channel transfer
	void test(String inputFilename, String outputFilename){
		try{
		File inFile = new File(inputFilename);
		File outFile = new File(outputFilename);
		FileInputStream fis = new FileInputStream(inFile);
		FileOutputStream fos = new FileOutputStream(outFile);
		FileChannel ifc = fis.getChannel();
		FileChannel ofc = fos.getChannel();
		
		ifc.transferTo(0, ifc.size(),ofc);
		}catch(IOException e){
			System.out.println("IOException: "+e);
		}
	}
}
