import java.io.IOException;


/**
 * Block group holds block despriptor that has the inode table pointer
 * @author Lewis Linaker
 *
 */
public class BlockGroup  {
	private int blockGroupNumber;
	private int inodeTablePointer;
	private static final int offset = 2048;
	public BlockGroup(Volume vol, int n) {		
		blockGroupNumber = n; 	
		try {
			vol.channel.read(vol.buffer,offset);
		} catch (IOException e) {
			System.out.println("Could not read Group Descriptor");			
		}
		inodeTablePointer= vol.buffer.getShort(8 + (32 * n)); 
		
	}
	/**
	 * Get group number
	 * @return blockGroupNumber
	 */
	public int getNumber(){ 
		return  blockGroupNumber;
	}
	/**
	 * Gets inode table pointer
	 * @return inodeTablePointer
	 */
	public int getInodeTablePointer(){ 
		return inodeTablePointer;
	}
}
