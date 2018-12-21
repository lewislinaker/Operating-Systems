import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * This class loads the ext2file and it has instance variables and methods that can access 
 * files and folders
 * @author Lewis
 *
 */
public class Volume {
	protected RandomAccessFile ext2File;
	protected ByteBuffer buffer;
	protected FileChannel channel;
	protected String fileName;
	protected SuperBlock superBlock; //displays information about the ext2 file
	protected ArrayList<BlockGroup> blockGroups = new ArrayList<BlockGroup>(); // an ext2 file has multiple blockGroups
	protected Directory dir; // this represents a directory and has folders and files
	protected File file; // this represents a simple file that can be read
	protected static final int blockSize = 1024; // size of a block
	protected static final int rootInode = 2;
	protected int blockGroupAmount;
	/**
	 * Constructor for Volume, here the ext2 file is loaded
	 * @param fileName
	 */
	public Volume(String fileName){ 
		try {
			ext2File = new RandomAccessFile(fileName,"r"); // access file
		} catch (FileNotFoundException e) {
			System.out.println("Could not read file");
		}
		buffer = ByteBuffer.allocate(blockSize);
		buffer.order(ByteOrder.LITTLE_ENDIAN); 
		channel = ext2File.getChannel();
		superBlock= new SuperBlock(buffer,channel); // creates the super block
	    blockGroupAmount = 0;  
	    //create block groups by getting the number of inodes per group 
	    // and incrementing it with itself until it reaches the the total size of inodes
		for(int i=superBlock.getInodesPerGroup(); i<superBlock.getTotalInodes()+1; i+=superBlock.getInodesPerGroup()){ 			
			blockGroups.add(new BlockGroup(this,blockGroupAmount));
			blockGroupAmount++;
		}				
	}
	/**
	 * Prints super block
	 */
	public void printSuperBlock(){ 
		superBlock.printInfo();
	}
	/**
	 * Print block groups
	 */
	public void showBlockGroups(){ 
		for(BlockGroup b: blockGroups){
			System.out.println("Block group "+b.getNumber() + "\n" + "InodeTablePointer: " + b.getInodeTablePointer() );
		}
	}
	/**
	 * Display root directory
	 */
	public void showRoot(){ 
		dir = new Directory(this,rootInode); // root is always at inode 2

	}
	/**
	 * This method opens a file or directory depending on the parameter
	 * @param name
	 */
	public void openItem(String name){
		ArrayList<Contents>contents = dir.getContents(); // retrieve contents of directory
		for(Contents c: contents){ 
			//look for the file or directory by checking their name  and file type
			if(name.equals(c.getFileName())){	
				if(c.getFileType()==1){ 
					file = new File(this,c.getInode().getInodeNumber());					
				}
				else if(c.getFileType()==2){				
					dir = new Directory(this,c.getInode().getInodeNumber());					
				}
				else{ 
					System.out.println("This is a directory, not a file");
				}
			}
		}
	}
		
}

