import java.io.IOException;
import java.nio.ByteBuffer;

import java.nio.channels.FileChannel;
/**
 * The super block displays information about the Ext2File
 * @author Lewis
 *
 */
public class SuperBlock {
	private String magicNumber;
	private static int totalInodes; 
	private static int totalBlocks; 
	private static int blocksPerGroup; 
	private static int inodesPerGroup; 
	private static int inodeSize; 
	private String volumeLabel=""; 
	/**
	 * Constructor for the super block
	 * @param buffer - byte buffer
	 * @param chan - channel
	 */
	public SuperBlock(ByteBuffer buffer, FileChannel chan){ 
		
		try {
			chan.read(buffer,1024); //read 1024 bytes
		} catch (IOException e) {			
			System.out.println("Could not load Super Block");
		}
		//use getInt() if the size of an item is 4 bytes 
		//use getShort() if the size of an item is 2 bytes 
		//use get() if the size of an item is 1 byte
		magicNumber = Integer.toHexString(buffer.getInt(56)); 
		totalInodes = buffer.getInt(0); 
		totalBlocks = buffer.getInt(4); 
		blocksPerGroup = buffer.getInt(32); 
		inodesPerGroup = buffer.getInt(40);
		inodeSize = buffer.getInt(88);
		for (int i= 120; i < 136; i++) // read volume name
			volumeLabel+=((char)buffer.get(i));						
		buffer.clear(); 
	}	
	/**
	 * Prints superblock
	 */
	public void printInfo(){ 
		System.out.println("Magic Number: "+ magicNumber);	  
		System.out.println("Total number of inodes in filesystem: "+totalInodes);
		System.out.println("Total number of blocks in filesystem: "+totalBlocks);
		System.out.println("Number of blocks per Group: "+blocksPerGroup);	 	  
		System.out.println("Number of inodes per Group: "+inodesPerGroup);	  
		System.out.println("Size of each inode in bytes: "+inodeSize);	  
		System.out.print("Volume label: " + volumeLabel + "\n");
	}
	/**
	 * Gets the size of an inode
	 * @return inodeSize
	 */
	public int getInodeSize(){ 
		return inodeSize;
	}
	/**
	 * Gets number of inodes per group
	 * @return inodesPerGroup
	 */
	public int getInodesPerGroup(){ 
		return inodesPerGroup;
	}
	/**
	 * Get total number of inodes
	 * @return totalInodes
	 */
	public int getTotalInodes(){
		return totalInodes;
	}

}
