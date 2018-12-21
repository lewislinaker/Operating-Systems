import java.util.Scanner;

public class Main {
	/**
	 * This is the main class that will load the file into the volume and provide user input to navigate in the file
	 * @param args
	 */
	public static void main(String[] args){ 
		Scanner in = new Scanner(System.in);
		String input=""; 
		Volume vol = new Volume("ext2fs"); // load the file into volume
		vol.printSuperBlock();  //print super block of file
		vol.showBlockGroups();  //shows block groups for the file
		vol.showRoot();		    //present root so the user knows what items does it have
		while(!input.equals("quit")){ 
			input = in.nextLine();
			vol.openItem(input);		//opens a file or directory														
		} 
		System.out.println("Program closed");
	}
}