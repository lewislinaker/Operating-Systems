import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * This is a directory and holds other directories and files
 * @author Robert
 *
 */
public class Directory extends Inode {
	private short length;
	private byte nameLength;
	private byte fileType;
	private String fileName = "";
	private ArrayList<Contents> contents = new ArrayList<Contents>();
	private Volume volume;
	private Inode inode;
	private static final int limit = 1024;
	/**
	 * Constructor for directory
	 * @param vol
	 * @param inodeNumber
	 */
	public Directory(Volume vol, int inodeNumber){ 		
		super(vol,inodeNumber);
		volume = vol;
		vol.buffer.clear();		
		int len = 0;
		int index = 0;	
		for(int j=0; j<dataBlocks.length && dataBlocks[j]!=0;j++){ 
			
			try {		
				vol.channel.read(vol.buffer,1024*dataBlocks[j]);	
				len=0; index=0;
				System.out.println("Data block: " + dataBlocks[j]);
				while (len < limit) {					// get files and folders until the length reaches 1024
					inodeNumber = vol.buffer.getInt(index);
					index += 4;
					inode = new Inode(vol,inodeNumber);
					vol.channel.read(vol.buffer,1024*dataBlocks[j]);
					length = vol.buffer.getShort(index);
					index += 2;
					nameLength = vol.buffer.get(index);
					index++;
					fileType = vol.buffer.get(index);
					index++;
					int ind = index; //this is used so that variable index won't alter 
					for (int i = index; i < ind + nameLength; i++) { 
						fileName += (char) vol.buffer.get(i);
						index++;
					}
					while (index % 4 != 0){ // check if number is multiple of 4
						index++;
					}									                  
					contents.add(new Contents(inode,length,nameLength,fileType,fileName)); // add items to array list
					System.out.println(inode.showPermissions()+ "\t" + inode.amountHardLinks + "\t" + inode.userId + "\t" + inode.ownerGroupId + "\t" + inode.fileSize + "\t"+ new Date(inode.lastModifiedTime) +"\t" + fileName);
					fileName = ""; // clear fileName 
					len += length; 								
				} 					
			}				
			catch (IOException e) {
				System.out.println("Could not read data block: " +dataBlocks[j]);
			}
		}
	}
	/**
	 * Retrieve array list of contents
	 * @return contents
	 */
	public ArrayList<Contents> getContents(){ 
		return contents;
	}
	/**
	 * Retrieves volume
	 * @return volume
	 */
	public Volume getVolume(){ 
		return volume;
	}

}




