package com.salah.jsubstool;

import java.io.File;
import java.util.Arrays;
/**
 * 
 * @author salah
 * @version v1.0
 */
public class MyClass {
	/**
	 * The path of the selected folder
	 */
	private final String path;
	/**
	 * The format of videos
	 */
	private final String vidForm;
	/**
	 * The format of the subs files
	 */
	private final String subsForm;
	/**
	 * The episodes special format (the character/string before the episode's number)
	 */
	private final String epForm;
	/**
	 * The number of episodes
	 */
	private final int numEp;
	/**
	 * Choice for creating the subs folder or not
	 */
	private final boolean mkdirSubs;
	/**
	 * Choice for advanced output
	 */
	private final boolean advOutput;
	/**
	 * A list of all the files(episodes/subs) in the selected folder 
	 */
	private File[] listOfVids,listOfSubs;
	
	/**
	 * The constructor of this class
	 * 
	 * This is the only constructor allowed in this class,
	 * witch means you have to initialize your parameters here
	 * 
	 * @param path The path
	 * @param vidForm The form of videos
	 * @param subsForm The form of subtitles
	 * @param epForm The episode special format
	 * @param numEp The number of episodes
	 * @param mkdir The choice of moving files
	 * @param output The choice of advanced output
	 */
	public MyClass(String path,String vidForm, String subsForm,String epForm,int numEp,boolean mkdir, boolean output){
		this.path=path;
		this.subsForm=subsForm;
		this.vidForm=vidForm;
		this.epForm=epForm;
		this.numEp=numEp;
		this.mkdirSubs=mkdir;
		this.advOutput=output;
		
	}
	/**
	 * This method open the selected folder
	 * and make a list of the videos and subtitles inside
	 * @return True if every thing is OK and false if not
	 */
	private boolean open(){
		File folder = new File(this.path);
		if(!folder.exists())
			return false;
		File[] listOfFiles = folder.listFiles();
		this.listOfSubs=new File[0];
		this.listOfVids=new File[0];
		for(int i=1; i<=this.numEp; i++){
			for (File file : listOfFiles) {
				if (file.isFile() && file.getName().contains(this.vidForm) && file.getName().contains(this.curEp(i)) ) {
					this.listOfVids=Arrays.copyOf(this.listOfVids, this.listOfVids.length+1);
					this.listOfVids[this.listOfVids.length-1]=file;
				}
				if (file.isFile() && file.getName().contains(this.subsForm) && file.getName().contains(this.curEp(i)) ) {
					this.listOfSubs=Arrays.copyOf(this.listOfSubs, this.listOfSubs.length+1);
					this.listOfSubs[this.listOfSubs.length-1]=file;
				}
			}
		}
		return true;
	}
	/**
	 * Given a number, this method return a string for the episodes format(eg:E02)
	 * @param The number of episode
	 * @return String of the episode special format
	 */
	private String curEp(int ep){
		if(ep<10)
			return this.epForm+"0"+ep;
		return this.epForm+ep;
	}
	/**
	 * This is the rename method
	 * 
	 * All the work is in this method,
	 * it start by opening the folder(call open() ) and search for the videos and
	 * subs files to find a match then renaming the subs file to the appropriate episode's name,
	 * and then calling the create() method
	 */
	public void rename(){
		if(!this.open()){
			System.out.println("Directory not found.");//add swing
			MainGUI.jTextArea1.append("Directory not found.\n");
			return;
		}
		System.out.println("Starting...\n\n*************************");//add swing
		MainGUI.jTextArea1.append("Starting...\n\n*************************\n");
		boolean exist;
		if(this.advOutput){
			this.affiche();
		}
		for(int i=1;i<=this.numEp; i++){
			exist=false;
			for(int j=1; j<=this.listOfVids.length; j++){
				for(int k=1; k<=this.listOfSubs.length; k++){
					if(this.listOfVids[j-1].getName().contains(curEp(i)) && this.listOfSubs[k-1].getName().contains(curEp(i))){
						// this is the rename code(thats what this is all about)
						if(this.listOfSubs[k-1].renameTo(new File(this.listOfSubs[k-1].getParentFile(),this.listOfVids[j-1].getName().replaceAll(this.vidForm, this.subsForm)))){
							System.out.println("[Episode "+curEp(i)+" found] - rename "+this.listOfSubs[k-1].getName()+" to "+this.listOfVids[j-1].getName().replaceAll(this.vidForm, this.subsForm)); //add swing
							MainGUI.jTextArea1.append("[Episode "+curEp(i)+" found] - rename "+this.listOfSubs[k-1].getName()+" to "+this.listOfVids[j-1].getName().replaceAll(this.vidForm, this.subsForm)+"\n");
							exist=true;
						}
					}
				}
			}
			if(!exist){
				System.out.println("Episode "+curEp(i)+" has no subs.");//add swing
				MainGUI.jTextArea1.append("Episode "+curEp(i)+" has no subs.\n");
			}
		}
		if(this.mkdirSubs && this.listOfSubs.length!=0)
			this.create();
		System.out.println("\n*****************************\n\nDone.");//add swing
		MainGUI.jTextArea1.append("\n*****************************\n\nDone.\n");
		
	}
	/**
	 * This function print a list of the videos and 
	 * subs in the selected folder
	 */
	public void affiche(){
		this.open();
		System.out.println("**Episodes and subtitles found:");//add swing
		MainGUI.jTextArea1.append("**Episodes and subtitles found:\n");
		for(File file: this.listOfVids)
		{
			System.out.println(file.getName());//add swing
			MainGUI.jTextArea1.append(file.getName());
		}
		for(File file: this.listOfSubs)
		{
			System.out.println(file.getName());//add swing
			MainGUI.jTextArea1.append(file.getName());
		}
	}
	/**
	 * This method create a folder named 'subs' inside the selected folder
	 * and if every goes OK it calls the move() method to move
	 * the subs to the 'subs' folder
	 */
	private void create(){
		String subs=this.path;
		if(subs.charAt(subs.length()-1)=='/')
			subs+="subs";
		else
			subs+="/subs";
		File folder = new File(subs);
		if(this.advOutput)
		{
			System.out.println("\n*Creating a sub folder (subs)...");//add swing
			MainGUI.jTextArea1.append("\n*Creating a sub folder (subs)...\n");
		}
		if(!folder.mkdir()){
			if(folder.isDirectory()){
				if(this.advOutput)
				{
					System.out.println("*Folder already exist.");//add swing
					MainGUI.jTextArea1.append("*Folder already exist.\n");
				}
				this.move();
			}
			else{
				if(this.advOutput)
				{
					System.out.println("*Folder cannot be created.");//add swing
					MainGUI.jTextArea1.append("*Folder cannot be created.\n");
				}
				return;
			}	
		}else{
			if(this.advOutput)
			{
				System.out.println("*Folder  created.");//add swing
				MainGUI.jTextArea1.append("*Folder  created.\n");
			}
			this.move();
		}
	}
	/**
	 * This method moves the subs files inside the 'subs' folder
	 */
	private void move(){
		for(int i=0; i<this.listOfSubs.length;i++){
			System.out.println("\n*Moving subtitles to subs folder.");//add swing
			MainGUI.jTextArea1.append("\n*Moving subtitles to subs folder.\n");
			if(this.advOutput)
			{
				System.out.println("*Moving "+this.listOfSubs[i].getName()+" to subs...");//add swing
				MainGUI.jTextArea1.append("*Moving "+this.listOfSubs[i].getName()+" to subs...\n");
			}
			try {
				String target=this.listOfSubs[i].getParent()+"/subs/"+this.listOfSubs[i].getName();
				this.listOfSubs[i].renameTo(new File(target));
				if(this.advOutput)
				{
					System.out.println("*File moved.");//add swing
					MainGUI.jTextArea1.append("*File moved.\n");
				}
			} catch (Exception e){ 
				if(this.advOutput)
				{
					System.out.println("*Failed to move the file");//add swing
					MainGUI.jTextArea1.append("*Failed to move the file\n");
				}
			}
		
		
		}
	}
	
}
