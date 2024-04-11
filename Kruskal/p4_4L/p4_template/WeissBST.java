// Binary Search Tree class From your textbook (Weiss)

//TODO:
//  (1) Update this code to meet the code style and JavaDoc style requirements.
//			Why? So that you get experience with the code for a binary search tree!
//			Also, this happens a lot in industry (updating old code
//			to meet your new standards). 
//  (2) Change remove() to be "predecessor replacement".
//  (3) Implement three more methods needed by other classes of this project:
//		size(),  toString(), and values(). Make sure to add JavaDoc for those. 


import java.util.LinkedList; //only for the return of values(), do not use it anywhere else

/**
 * Implements an unbalanced binary search tree.
 * Note that all "matching" is based on the compareTo method.
 * @param <T> the type of elements stored in the tree, must implement Comparable.
 * @author Mark Allen Weiss
 */
public class WeissBST<T extends Comparable<? super T>>
{

	//--------------------------------------------------------
	// CODE PROVIDED from WEISS
	//--------------------------------------------------------
	// Do NOT change the implementation - you should only edit 
	// in order to pass JavaDoc and style checking.
	//--------------------------------------------------------

	/**
	 * Represents a node in an unbalanced binary search tree.
	 * This class is private and only accessible within the {@code WeissBST} class.
	 *
	 * @param <T> the type of elements stored in the node, must implement Comparable.
	 */
	private class BinaryNode<T>
	{
		/**
		 * Constructs a new BinaryNode with the specified element.
		 *
		 * @param theElement the data to be stored in the node.
		 */
		BinaryNode( T theElement )
		{
	   	 	element = theElement;
			left = right = null;
		}

	       /**
	 * The data stored in the node.
	 */
   	    T			 element;  
	       /**
	 * Reference to the left child of the node.
	 */
   	    BinaryNode<T> left;
	       /**
	 * Reference to the right child of the node.
	 */
   	    BinaryNode<T> right;	// Right child
	}

	//NOTE: you may not have any other instance variables, only this one below.
	//if you make more instance variables your binary search tree class 
	//will receive a 0, no matter how well it works
	
	/** The tree root. */
	private BinaryNode<T> root;



	/**
	 * Construct the tree.
	 */
	public WeissBST( )
	{
		root = null;
	}

	/**
	 * Insert into the tree.
	 * @param x the item to insert.
	 * @throws Exception if x is already present.
	 */
	public void insert( T x )
	{
		root = insert( x, root );
	}


	

	/**
	 * Remove minimum item from the tree.
	 * @throws Exception if tree is empty.
	 */
	public void removeMin( )
	{
		root = removeMin( root );
	}

	/**
	 * Find the smallest item in the tree.
	 * @return smallest item or null if empty.
	 */
	public T findMin( )
	{
		return elementAt( findMin( root ) );
	}


	/**
	 * Find an item in the tree.
	 * @param x the item to search for.
	 * @return the matching item or null if not found.
	 */
	public T find( T x )
	{
		return elementAt( find( x, root ) );
	}

	   /**
	 * Make the tree logically empty.
	 */
    public void makeEmpty( )
	{
		root = null;
	}

	/**
	 * Test if the tree is logically empty.
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty( )
	{
		return root == null;
	}

	/**
	 * Internal method to get element field.
	 * @param t the node.
	 * @return the element field or null if t is null.
	 */
	private T elementAt( BinaryNode<T> t )
	{
		return t == null ? null : t.element;
	}

	/**
	 * Internal method to insert into a subtree.
	 * @param x the item to insert.
	 * @param t the node that roots the tree.
	 * @return the new root.
	 * @throws Exception if x is already present.
	 */
	private BinaryNode<T> insert( T x, BinaryNode<T> t )
	{
		if( t == null )
			t = new BinaryNode<T>( x );
		else if( x.compareTo( t.element ) < 0 )
			t.left = insert( x, t.left );
		else if( x.compareTo( t.element ) > 0 )
			t.right = insert( x, t.right );
		else
			throw new IllegalArgumentException( "Duplicate Item: " + x.toString( ) );  // Duplicate
		return t;
	}


	/**
	 * Internal method to remove minimum item from a subtree.
	 * @param t the node that roots the tree.
	 * @return the new root.
	 * @throws Exception if t is empty.
	 */
	private BinaryNode<T> removeMin( BinaryNode<T> t )
	{
		if( t == null )
			throw new IllegalArgumentException( "Min Item Not Found");
		else if( t.left != null )
		{
			t.left = removeMin( t.left );
			return t;
		}
		else
			return t.right;
	}	

	/**
	 * Internal method to find the smallest item in a subtree.
	 * @param t the node that roots the tree.
	 * @return node containing the smallest item.
	 */
	private BinaryNode<T> findMin( BinaryNode<T> t )
	{
		if( t != null )
			while( t.left != null )
				t = t.left;

		return t;
	}


	/**
	 * Internal method to find an item in a subtree.
	 * @param x is item to search for.
	 * @param t the node that roots the tree.
	 * @return node containing the matched item.
	 */
	private BinaryNode<T> find( T x, BinaryNode<T> t )
	{
		while( t != null )
		{
			if( x.compareTo( t.element ) < 0 )
				t = t.left;
			else if( x.compareTo( t.element ) > 0 )
				t = t.right;
			else
				return t;	// Match
		}
		
		return null;		 // Not found
	}

	//--------------------------------------------------------
	// CODE THAT YOU MAY NEED TO CHANGE
	//--------------------------------------------------------
	// We need the BST removal to be "predecessor replacement".
	// Make necessary changes to match the expected behavior.
	// Feel free to add private helper methods as needed.
	//--------------------------------------------------------

	/**
	 * Remove from the tree..
	 * @param x the item to remove.
	 * @throws Exception if x is not found.
	 */
	public void remove( T x )
	{
		root = remove( x, root ); // calls remove function to remove that value from the tree
	}

	/**
	 * Internal method to remove from a subtree using predecessor replacement.
	 * @param x the item to remove.
	 * @param t root of that tree/subtree.
	 * @return the new root.
	 * @throws Exception if x is not found.
	 */
	private BinaryNode<T> remove(T x, BinaryNode<T> t) {
		if (t == null) { // if the node is null
			throw new IllegalArgumentException("Item Not Found: " + x.toString());
		}
		if (x.compareTo(t.element) < 0) {
			t.left = remove(x, t.left); // Recursively remove from the left subtree
		} else if (x.compareTo(t.element) > 0) {
			t.right = remove(x, t.right); // Recursively remove from the right subtree
		} else if (t.left != null && t.right != null) { // Node with two children
			// Replace the current node's element with the maximum from the left subtree
			t.element = findMax(t.left).element;
			// Remove the maximum from the left subtree
			t.left = removeMax(t.left);
		} else {  //handle predecessor replacement
			if (t.left != null) { // Node with one child
				t = t.left;
			} else { // node with no children
				t = t.right;
			}
		}
		return t;
	}

	/**
	 * Find the maximum item in a subtree.
	 * @param t  root of the tree.
	 * @return node containing the maximum value.
	 */
	private BinaryNode<T> findMax(BinaryNode<T> t) {
		if (t != null) {
			while (t.right != null) { // keeps traversing until the right most node is found
				t = t.right;
			}
		}
		return t;
	}

	/**
	 * Remove the maximum value from a subtree.
	 * @param t root of the tree.
	 * @return the new root.
	 */
	private BinaryNode<T> removeMax(BinaryNode<T> t) {
		if (t == null) { // current node is null/ empty subtree
			return null;
		} else if (t.right != null) { // traverses down right subtree until null is hit
			t.right = removeMax(t.right);
			return t;
		} else { //If right child is null,  current node is the max
			return t.left;
		}
	}


	//--------------------------------------------------------
	// CODE YOU MUST IMPLEMENT
	//--------------------------------------------------------
	// Feel free to define private helper methods.
	// Remember to add JavaDoc for your methods.
	//--------------------------------------------------------

	/**
	 * Returns the number of nodes in the tree.
	 * @return the number of nodes in the tree. Returns 0 for null trees.
	 */
	public int size(){
		return sizeCalc(this.root); //uses helper mehtod to count the nodes
	}

	/**
	 * Recursively calculates the number of nodes in the subtree rooted at the given node.
	 *
	 * @param node the root of the subtree.
	 * @return the number of nodes in the subtree. Returns 0 if the subtree is empty or if the node is null.
	 */
	private int sizeCalc(BinaryNode<T> node) {
		if (node == null) { // if tree is empty or null pointer is hit
			return 0;
		}
		//counts left subtree and right subtree recursivley and adds one for the root
		return 1 + sizeCalc(node.left) + sizeCalc(node.right);
	}
	

	  /**
	 * Returns a string representation of the tree following in-order traversal.
	 *
	 * @return a string representation of the tree following in-order traversal. Returns an empty string for null trees.
	 */
  	public String toString(){
		return inOrder(root); // calls helper function to traverse nodes
  	}

	/**
	 * Recursively performs in-order traversal on the subtree rooted at the given node
	 * and constructs a string representation of the values.
	 * @param node the root of the subtree.
	 * @return a string representation of the values in the subtree following in-order traversal.
	 */
	private String inOrder(BinaryNode<T> node) {
		if (node == null) { //return empty string if tree is empty or traversal hits null pointer
			return "";
		}
	
		String left = inOrder(node.left); // traverses left subtree
		String nodeString = node.element.toString() + " "; // converts element to a string and adds space
		String right = inOrder(node.right); // traverses right subtree
	
		return left + nodeString + right;
	}
  	
	/**
	 * Returns a linked list of all values in the tree following pre-order traversal.
	 * @return a linked list of all values in the tree following pre-order traversal. Returns an empty linked list for null trees.
	 */
	public LinkedList<T> values(){
		LinkedList<T> values = new LinkedList<>(); //linked list to store values
		preOrder(root, values); //calls helper method to do preorder traversel
		return values;
	}

	/**
	 * Recursively performs pre-order traversal on the subtree rooted at the given node
	 * and adds the values to a linked list.
	 *
	 * @param node the root of the subtree.
	 * @param list the linked list which the values are added to during traversal.
	 */
	private void preOrder(BinaryNode<T> node, LinkedList<T> list) {
		if (node != null) {
			list.add(node.element);
			preOrder(node.left, list);
			preOrder(node.right, list);
		}
	}


}