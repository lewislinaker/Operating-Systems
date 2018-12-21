import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

/**
 * Inode displays information about a file or directory
 * @author Lewis
 *
 */
public class Inode {
	protected static final int blockSize = 1024;
	protected int inodeNumber;
	protected int pointer; 
	protected short fileMode; 
	protected short userId; 
	protected int fileSizeLower; 
	protected int lastAccessTime; 
	protected int creationTime; 
	protected int lastModifiedTime; 
	protected int deletedTime; 
	protected short ownerGroupId; 
	protected short amountHardLinks;
	protected int[] dataBlocks = new int[12]; 
	protected int indirectPointer; 
	protected int doubleIndirectPointer; 
	protected int tripleIndirectPointer;
	protected int fileSizeUpper;
	protected long fileSize;
	/**
	 * Constructor for Inode class
	 * @param vol
	 * @param inodeNumber
	 */
	public Inode(Volume vol, int inodeNumber){ 
		this.inodeNumber=inodeNumber;
		vol.buffer.clear(); //always get a clear buffer so it won't search at the wrong position
		int boundary = vol.superBlock.getInodesPerGroup(); 
		//check if the inode number is greater than boundaries, if they are greater than look in the next block group
		if(inodeNumber<boundary){
			pointer = blockSize*vol.blockGroups.get(0).getInodeTablePointer() + (inodeNumber-1)*vol.superBlock.getInodeSize();
		}
		else if(boundary < inodeNumber && inodeNumber<boundary*2){		
			pointer = blockSize*vol.blockGroups.get(1).getInodeTablePointer() + (inodeNumber-boundary-1)*vol.superBlock.getInodeSize();
		}
		else if(boundary*2<inodeNumber && inodeNumber < boundary*3){

			pointer = blockSize*vol.blockGroups.get(2).getInodeTablePointer() + (inodeNumber-boundary*2-1)*vol.superBlock.getInodeSize();
		}
		try {
			vol.channel.read(vol.buffer,pointer);
		} catch (IOException e) {			
			System.out.println("Could not load Group Descriptor");
		}
		//get inode details
		fileMode = vol.buffer.getShort(0);
		userId = vol.buffer.getShort(2); 
		fileSizeLower=vol.buffer.getInt(4); 
		lastAccessTime = vol.buffer.getInt(8); 
		creationTime = vol.buffer .getInt(12); 
		lastModifiedTime =vol.buffer.getInt(16);
		deletedTime = vol.buffer.getInt(20);
		ownerGroupId = vol.buffer.getShort(24);
		amountHardLinks = vol.buffer.getShort(26); 
		int index=0;
		//read data blocks, these are at a distance of 4 bytes
		for(int i = 40; i<=84; i+=4){  
			dataBlocks[index] = vol.buffer.getInt(i);
			index++;
		}
		indirectPointer = vol.buffer.getInt(88);
		doubleIndirectPointer = vol.buffer.getInt(92); 
		tripleIndirectPointer = vol.buffer.getInt(96);
		fileSizeUpper = vol.buffer.getInt(108);
		fileSize=((long)(vol.buffer.getInt(108) << 32) + vol.buffer.getInt(4));
		vol.buffer.clear(); // clear buffer at the end

	}
	/**
	 * Print inode details
	 */
	public void printDetails(){ 
		System.out.println("File mode: " + fileMode);
		System.out.println("User ID of owner (lower 16 bits): " + userId);
		System.out.println("File size in bytes (lower 32 bits): "+ fileSizeLower);
		System.out.println("File size in bytes (upper 32 bits): "+ fileSizeUpper);
		System.out.println("Last Access time: "+ new Date(lastAccessTime));
		System.out.println("Creation time: "+ new Date(creationTime));
		System.out.println("Last modified time: "+ new Date(lastModifiedTime));
		System.out.println("Deleted time: "+ new Date(deletedTime));
		System.out.println("Group ID of owner (lower 16 bits): "+ownerGroupId);
		System.out.println("Number of hard links referencing file: "+amountHardLinks);
		//if data data blocks are 0 do not print them, same for indirect pointers
		for(int i=0; i<dataBlocks.length;i++){ 
			if(dataBlocks[i] != 0)
				System.out.println("Data block " + i + ":" + dataBlocks[i]);
		}
		if(indirectPointer != 0)
			System.out.println("Indirect pointer: " + indirectPointer);
		if(doubleIndirectPointer != 0)
			System.out.println("Double indirect pointer: " + doubleIndirectPointer);
		if(tripleIndirectPointer != 0)
			System.out.println("Triple indirect pointer: " + tripleIndirectPointer);
	}
	/**
	 * Check file/directory permissions
	 * @return
	 */
	public String showPermissions(){ 
		   char[] permission ={'-','-','-','-','-','-','-','-','-','-'};
		   int IFREG = 0x8000;      // Regular File
		   int IFDIR = 0x4000;      // Directory
		   int IRUSR = 0x0100;      // User read
		   int IWUSR = 0x0080;      // User write
		   int IXUSR = 0x0040;      // User execute
		   int IRGRP = 0x0020;      // Group read
		   int IWGRP = 0x0010;      // Group write
		   int IXGRP = 0x0008;      // Group execute
		   int IROTH = 0x0004;      // Others read
		   int IWOTH = 0x0002;      // Others write
		   int IXOTH = 0x0001;      // Others execute
		    
		    if((fileMode & IFREG) == IFREG)
		    	permission[0]='-';
		    if((fileMode & IFDIR) == IFDIR)
		    	permission[0]='d';
		    if((fileMode & IRUSR) == IRUSR)
		    	permission[1]='r';
		    if((fileMode & IWUSR) == IWUSR)
		    	permission[2]='w';
		    if((fileMode & IXUSR) == IXUSR)
		    	permission[3]='x'; 
		    if((fileMode & IRGRP) == IRGRP)
		    	permission[4]='r';
		    if((fileMode & IXGRP) == IXGRP)
		    	permission[5]='w';
		    if((fileMode & IWGRP) == IWGRP)
		    	permission[6]='x';
		    if((fileMode & IROTH) == IROTH)
		    	permission[7]='r';
		    if((fileMode & IWOTH) == IWOTH)
		    	permission[8]='w';
		    if((fileMode & IXOTH) == IXOTH)
		    	permission[9]='x';
		    String p = new String(permission);
		    return p;		    	
	}
	/**
	 * Get array of data blocks
	 * @return dataBlocks
	 */
	public int[] getDataBlocks(){ 
		return dataBlocks;
	}
	/**
	 * Retrieve pointer 
	 * @return pointer
	 */
	public int getPointer(){ 
		return pointer;
	}
	/**
	 * Retrieve inode number
	 * @return inode number
	 */
	public int getInodeNumber(){
		return inodeNumber;
	}
}
