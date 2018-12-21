/**
 * This holds the values of a file or directory
 * @author Lewis
 *
 */
public class Contents  {
	private int inodeNumber; 
	private short length; 
	private byte nameLength;
	private byte fileType;
	private String fileName;
	private Inode inode;
	/**
	 * Constructor for contents
	 * @param inode
	 * @param length
	 * @param nameLength
	 * @param fileType
	 * @param fileName
	 */
	public Contents(Inode inode, short length, byte nameLength, byte fileType, String fileName){   
		this.inode = inode;
		this.length = length; 
		this.nameLength = nameLength; 
		this.fileType =fileType; 
		this.fileName = fileName; 		
	}
	/**
	 * Retrieve file/directory inode
	 * @return inode
	 */
	public Inode getInode(){
		return inode;
	}
	/**
	 * Prints the inodes details
	 */
	public void printInode(){
		inode.printDetails();
	}
	/**
	 * Gets inode number
	 * @return inodeNumber
	 */
	public int getInodeNumber(){
		return inodeNumber;
	}
	/**
	 * Gets file lenght
	 * @return length
	 */
	public short getLength(){ 
		return length;
	}
	/**
	 * Gets size of name
	 * @return nameLength
	 */
	public byte getNameLength(){ 
		return nameLength;
	}
	/**
	 * Get file type
	 * @return fileType
	 */
	public byte getFileType(){
		return fileType;
	}
	/**
	 * Get the name of the file
	 * @return fileName
	 */
	public String getFileName(){ 
		return fileName;
	}
}
