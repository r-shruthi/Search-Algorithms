/*
 * BST.java
 * This program implements Binary Search Tree
 * Given an input file, it builds a BST and 
 * can calculate and RANK and SELECT
 *
 * To Execute:
 javac BST.java
 java BST <filename>
 
 * Input Arguments: filename
 *
 * Output: Select(9) and Rank(15)
 */
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Scanner;

public class BST {

	private Node root;
		
	public static void main(String[] args) {
		String fileInput = "";
		
		//Accept filename from user
		if(args.length > 0)
		{
			fileInput = args[0];
		}
		else
		{
			System.out.println("Please input the file name");
			System.exit(-1);		
		}
		Scanner inFile;		 
		Path filePath;		
		
		//read input elements into List 
		List<Integer> inputList= new ArrayList<Integer>();
		BST bst = new BST();
		
		try {
			filePath = Paths.get(fileInput);	
			inFile = new Scanner(filePath);		
			
			while(inFile.hasNext())
			{
				inputList.add(inFile.nextInt());
			}
			inFile.close();			
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Convert array to list
		Integer[] inputArray = inputList.toArray(new Integer[0]);
		
		//Build BST
		for(int i=0;i<inputList.size();i++)
		{
			bst.put(inputArray[i],inputArray[i]);
		}
		
		System.out.println("Select(9): " + bst.select(9));
		System.out.println("Rank(15): " + bst.rank(15));
	}
	
	private class Node
	{
		private int key;
		private int val;
		private Node left;
		private Node right;
		private int count;
		
		private Node(int Key, int Value, int Count)
		{
			this.key = Key;
			this.val = Value;
			this.count = Count;
		}
	}
	
	//This method calculates the size of a node
	public int size()
	{ return size(root); }
	
	private int size(Node x)
	{
		if (x == null) return 0;
		return x.count;
	}
	
	//This method builds the BST by inserting a node
	public void put(int key, int val)
	{ 
		root = put(root, key, val);		
	}
	
	private Node put(Node x, int key, int val)
	{
		if (x == null) 
			return new Node(key, val,1);
		//if input key lesser than key of node x, insert node on left
		if (key < x.key)
			x.left = put(x.left, key, val);
		
		//if input key greater than key of node x, insert node on right
		else if (key > x.key)
			x.right = put(x.right, key, val);
		
		//if equal, set value of node to input value
		else 
			x.val = val;
		
		//update size 
		x.count = size(x.left) + size(x.right) + 1;
		return x;		
	}
	
	//This method returns Node containing key of rank k.
	public int select(int k)
	{
		return select(root, k).key;
	}
	
	private Node select(Node x, int k)
	{ 
		//get node of rank k
		if (x == null) return null;
		int t = size(x.left);
		if (t > k) return select(x.left, k);
		else if (t < k) return select(x.right, k-t-1);
		else return x;
	}
	
	//This method returns the rank of node with input key
	public int rank(int key)
	{ 
		return rank(key, root); 
	}
	
	private int rank(int key, Node x)
	{
		if (x == null) return 0;	
		//calculate rank of node recursively
		if (key < x.key) return rank(key, x.left);
		else if (key > x.key) return 1 + size(x.left) + rank(key, x.right);
		else if (key == x.key) return size(x.left);
		return 0;
	}

	
}