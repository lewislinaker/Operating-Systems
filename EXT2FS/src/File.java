import java.io.IOException;
import java.util.Date;

/**
 * This represents a simple file
 * @author Lewis
 *
 */
public class File extends Inode {
	private Inode inode;
	/**
	 * Constructor for File
	 * @param vol
	 * @param inodeNumber
	 */
	public File(Volume vol, int inodeNumber){ 
		super(vol,inodeNumber);
		vol.buffer.clear(); // always clear buffer so it won't read from wrong path
		inode = new Inode(vol,inodeNumber); 
		System.out.println(inode.showPermissions()+ "\t" + inode.amountHardLinks + "\t" + inode.userId + "\t" + inode.ownerGroupId + "\t" + inode.fileSize + "\t"+ new Date(inode.lastModifiedTime) +"\t" );
		//read data blocks
		for(int i =0; i<dataBlocks.length ; i++){ 
			try {
				vol.channel.read(vol.buffer,1024*dataBlocks[i]);	
				if(dataBlocks[i]==0) { // exit loop if data block is 0
					System.out.println("\n"+"End");
					break;
				}
				for(int j=0; j<1024; j++){ 
					System.out.print((char)vol.buffer.get(j)); 
				}				

			} catch (IOException e) {
				System.out.println("Could not read Data Block: " + dataBlocks[i]);
			}vol.buffer.clear();

		}
	}
}
