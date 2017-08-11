/**
 * @author shrut
 * This program implements the 2-3 tree.
 * Given an input file with integer keys, it constructs a 2-3 tree
 * and prints the tree key-val pairs by traversing the tree inorder
 * 
 * input parameters: filename with integer keys
 * value is considered equal to key
 * output: tree traversed inorder
 * 
 * to execute:
 	javac TwoThreeTree.java
 	java TwoThreeTree <filename>
 */
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TwoThreeTree {

	Node root;
	public static void main(String[] args) {

		String fileInput = "";
		List<Integer> inputList= new ArrayList<Integer>();
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
		
		//read the keys from the file and store into array
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

		//construct the 2-3 tree
		TwoThreeTree tree = new TwoThreeTree();
		for(int i=0; i<inputArray.length;i++)			
			tree.put(inputArray[i],inputArray[i]);

		//print the elements of the array
		tree.get(tree.root);
		System.out.println();		
	}

	/* This method prints all the elements of the tree
	 * by traversing inorder 
	 */
	private void get(Node x)
	{
		if(x==null)
			return;
		
		//check if node is two-node or three-node
		if(x instanceof ThreeNode)
		{
			ThreeNode x3 = (ThreeNode)x;
			get(x3.left);
			System.out.println(x3.leftKey + "," + x3.leftVal);
			get(x3.mid);
			System.out.println(x3.rightKey + "," + x3.rightVal);
			get(x3.right);
		}
		else
		{
			TwoNode x2 = (TwoNode)x;
			get(x2.left);
			System.out.println(x2.key + "," + x2.val);
			get(x2.right);
		}
	}

	//base put method 
	//calls the put(Node x, int key, int val) method with x=root
	//if root turns into 4-node, then split it to create new 2-node root 
	//and two 2-node children
	private void put(int key, int val)
	{
		if(root == null)
			root =  new TwoNode(key, val);
		else
		{
			//recursively call put function
			root = this.put(root, key, val);
			
			//after inserting key-val, if root is a four node, then split the root 
			//into one parent 2-node and two child 2-nodes
			if(root.isFourNode && root instanceof ThreeNode)
			{						
				ThreeNode oldroot = (ThreeNode)(root);			
				TwoNode newRoot = new TwoNode(oldroot.tempKey,oldroot.tempVal);
				
				//new left 2-node child
				TwoNode leftChild = new TwoNode(oldroot.leftKey,oldroot.leftVal);
				leftChild.left = oldroot.left;
				leftChild.right = oldroot.mid;

				//new right 2-node child
				TwoNode rightChild = new TwoNode(oldroot.rightKey,oldroot.rightVal);
				rightChild.left = oldroot.right;
				rightChild.right = oldroot.fourth;
				newRoot.left=leftChild;
				newRoot.right=rightChild;

				root = newRoot;
			}
		}
	}

	/* This method puts the key-val pair into the tree by recursively searching for the correct location to insert.
	 * If key already exists, then update value. Else insert into existing 2 or 3 node. 
	 * 2 node is made a 3 node. 3 node is converted to a temporary 4 node
	 * if child is a 4 node, then split into two 2-nodes
	 */
	private Node put(Node x, int key, int val)
	{		
		//check if node is 2-node or 3-node
		if(x instanceof ThreeNode)
		{
			ThreeNode x3 = (ThreeNode)x;
			//if input key lesser than left key of node x, insert node on left
			if (key < x3.leftKey)
			{	
				//check if left child exists
				if(x3.left == null)
				{
					//make temporary 4 node
					x3.isFourNode=true;
					x3.tempKey = x3.leftKey;
					x3.tempVal=x3.leftVal;

					x3.leftKey = key;
					x3.leftVal = val;
				}				
				else
				{
					//recursively call put method
					x3.left = put(x3.left, key, val);
					
					//if left child is 4-node, then split the child node into two child 2nodes and 
					//insert the middle key of the former 4-node into this node as leftmost key making this a 4-node
					if(x3.left.isFourNode && x3.left instanceof ThreeNode)
					{						
						ThreeNode leftchild = (ThreeNode)(x3.left);

						//new left child
						TwoNode t1 = new TwoNode(leftchild.leftKey,leftchild.leftVal);
						t1.left = leftchild.left;
						t1.right = leftchild.mid;

						//new second child
						TwoNode t2 = new TwoNode(leftchild.rightKey,leftchild.rightVal);
						t2.left = leftchild.right;
						t2.right = leftchild.fourth;

						x3.tempKey = x3.leftKey;
						x3.tempVal = x3.leftVal;

						x3.leftKey = leftchild.tempKey;
						x3.leftVal = leftchild.tempVal;

						x3.left=t1;
						x3.fourth=x3.right;
						x3.right=x3.mid;
						x3.mid=t2;

						//make this node a 4-node
						x3.isFourNode=true;
					}

				}
			}

			//if input key greater than right key of node x, insert node on right
			else if (key > x3.rightKey)
				//make temporary 4 node
				if(x3.right == null)
				{
					x3.isFourNode = true;
					x3.tempKey = x3.rightKey;
					x3.tempVal=x3.rightVal;

					x3.rightKey = key;
					x3.rightVal = val;
				}
				else
				{
					//recursively call put method
					x3.right = put(x3.right, key, val);
					
					//if right child is 4-node, then split the child node into two child 2nodes and 
					//insert the middle key of the former 4-node into this node as rightmost key making this a 4-node					
					if(x3.right.isFourNode && x3.right instanceof ThreeNode)
					{						
						ThreeNode rightchild = (ThreeNode)(x3.right);

						//new third child
						TwoNode t1 = new TwoNode(rightchild.leftKey,rightchild.leftVal);
						t1.left = rightchild.left;
						t1.right = rightchild.mid;

						//new fourth child
						TwoNode t2 = new TwoNode(rightchild.rightKey,rightchild.rightVal);
						t2.left = rightchild.right;
						t2.right = rightchild.fourth;

						x3.tempKey = x3.rightKey;
						x3.tempVal = x3.rightVal;

						x3.rightKey = rightchild.tempKey;
						x3.rightVal = rightchild.tempVal;					

						x3.fourth=t2;
						x3.right=t1;					

						//make this node a 4-node
						x3.isFourNode=true;
					}
				}

			//if input key greater than left key of node x and lesser than right key of node x,
			//insert node on right
			else if (key > x3.leftKey && key < x3.rightKey)
			{
				//if no mid child exists
				if(x3.mid == null)
				{
					//make this node a temporary 4 node
					x3.isFourNode = true;
					x3.tempKey = key;
					x3.tempVal  = val;
				}
				else
				{
					//recursively call put method
					x3.mid=put(x3.mid,key, val);
					
					//if mid child is 4-node, then split the child node into two child 2nodes and 
					//insert the middle key of the former 4-node into this node as mid key making this a 4-node					
					if(x3.mid.isFourNode && x3.mid instanceof ThreeNode)
					{						
						ThreeNode midchild = (ThreeNode)(x3.mid);

						//new second child
						TwoNode t1 = new TwoNode(midchild.leftKey,midchild.leftVal);
						t1.left = midchild.left;
						t1.right = midchild.mid;

						//new third child
						TwoNode t2 = new TwoNode(midchild.rightKey,midchild.rightVal);
						t2.left = midchild.right;
						t2.right = midchild.fourth;

						x3.tempKey = midchild.tempKey;
						x3.tempVal = midchild.tempVal;						

						x3.fourth=x3.right;
						x3.right=t2;
						x3.mid=t1;
						
						//make this node a 4-node
						x3.isFourNode=true;
					}
				}
			}
			//if equal, set value of node to input value
			else if (key == x3.leftKey)
				x3.leftVal = val;

			else
				x3.rightVal = val;

			return x3;
		}

		//if current node is a 2-node
		else
		{
			TwoNode x2 = (TwoNode)x;
			
			//if key to be inserted is less than current node
			if (key < x2.key)
			{
				//check if no children
				if(x2.left == null)
				{
					//make three node
					ThreeNode temp3node = new ThreeNode(key, val, x2.key, x2.val);
					return temp3node;
				}			
				else
				{
					//recursively call put method
					x2.left = put(x2.left, key, val);
					
					//if left child is 4-node, then split the child node into two child 2nodes and 
					//insert the middle key of the former 4-node into this node as left key, making it a 3-node
					if(x2.left.isFourNode && x2.left instanceof ThreeNode)
					{						
						ThreeNode leftchild = (ThreeNode)(x2.left);

						//make three node
						ThreeNode temp3node = new ThreeNode(leftchild.tempKey, leftchild.tempVal, x2.key, x2.val);
						TwoNode t1 = new TwoNode(leftchild.leftKey,leftchild.leftVal);
						t1.left = leftchild.left;
						t1.right = leftchild.mid;
						temp3node.left = t1;
						TwoNode t2 = new TwoNode(leftchild.rightKey,leftchild.rightVal);
						t2.left = leftchild.right;
						t2.right = leftchild.fourth;
						temp3node.mid =	t2;
						temp3node.right = x2.right;
						return temp3node;						
					}
				}
			}

			//if input key greater than key of node x, insert node on right
			else if (key > x2.key)
			{
				//check if no children
				if(x2.right == null)
				{
					//make three node
					ThreeNode temp3node = new ThreeNode(x2.key, x2.val, key, val);
					return temp3node;
				}			
				else
				{
					//recursively call put method
					x2.right = put(x2.right, key, val);
					
					//if right child is 4-node, then split the child node into two child 2nodes and 
					//insert the middle key of the former 4-node into this node as right key, making it a 3-node
					if(x2.right.isFourNode && x2.right instanceof ThreeNode)
					{						
						ThreeNode rightchild = (ThreeNode)(x2.right);

						//make three node
						ThreeNode temp3node = new ThreeNode(x2.key, x2.val, rightchild.tempKey, rightchild.tempVal);
						temp3node.left=x2.left;
						TwoNode t1 = new TwoNode(rightchild.leftKey,rightchild.leftVal);
						t1.left = rightchild.left;
						t1.right = rightchild.mid;
						temp3node.mid = t1;
						TwoNode t2 = new TwoNode(rightchild.rightKey,rightchild.rightVal);
						t2.left = rightchild.right;
						t2.right = rightchild.fourth;						
						temp3node.right = t2;				
						return temp3node;						
					}
				}
			}
			//if equal, set value of node to input value
			else 
				x2.val = val;
			return x2;
		}

	}

	//base class Node
	private class Node
	{
		boolean isFourNode;
		public Node()
		{
			this.isFourNode = false;
		}
	}

	//child class TwoNode
	public class TwoNode extends Node
	{	
		int key;
		int val;

		Node left;
		Node right;

		public TwoNode(int key, int val)
		{
			super();
			this.key=key;
			this.val=val;
		}
	}

	//child class 3-node
	private class ThreeNode extends Node
	{		
		public ThreeNode(int leftKey, int leftVal, int rightKey, int rightVal) {
			super();
			this.leftKey = leftKey;
			this.leftVal = leftVal;
			this.rightKey = rightKey;
			this.rightVal = rightVal;
			//this.isFourNode = false;
		}

		int leftKey;
		int leftVal;

		int rightKey;
		int rightVal;

		int tempKey;
		int tempVal;

		Node left;
		Node right;
		Node mid;
		Node fourth;
	}


}
