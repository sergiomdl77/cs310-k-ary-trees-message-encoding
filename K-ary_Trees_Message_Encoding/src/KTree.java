import java.util.Iterator;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;

/**
 * This class will provide all the basic methods to handle operations on a K-ary tree linked
 * structure which will utilize an array of links to children nodes.
 * 
 * @author Sergio Delgado
 *
 * @param <E> Generic type that will be used to define the type of value the tree nodes will 
 * hold. This class implements all the abstract methods of TreeIterable. 
 */
public class KTree<E> implements TreeIterable<E>
{
	int kFactor;			// k factor of the tree (max number of children per node).
	int size;				// size of the tree (number of elements).
	int height;             // height of the tree.
	int currentHeight;      // Auxiliar variable used when calculating height of tree.
	int tracer;				// Auxiliar variable used when traversing the tree (keeps track current index)
	TreeNode<E> root;		// Reference to the root node of the tree.

	/**
	 * This is a nested class that will provide the definition of the tree nodes.
	 *  
	 * @author Sergio Delgado
	 *
	 * @param <E> Generic type that will be used to define the type of value the tree nodes will hold. 
	 */
	private class TreeNode<E>
	{
		E value;				// data stored in the tree node.
		TreeNode<E>[] children;	// array of references to the children nodes of this node.

		/**
		 * Constructor that will initialize the value of the data stored in the node and the array
		 * of references to its children nodes.
		 */		
		@SuppressWarnings("unchecked")  // Tag meant to skip giving warnings due to unsafe (E) casting on Objects
		private TreeNode(E v)
		{
			value = v;	
			children = new TreeNode[kFactor];
		}
	};
	
	/**
	 * Constructor that will initialize the pointer to root to null if the array tree received as parameter
	 * has no elements (length = 0), otherwise it will initialize it by creating a new node that will be the
	 * root of the tree and then build the tree with the help of the method called build and the arrayTree as 
	 * parameter along with the root of the tree. It also initializes the kFactor of the tree to the parameter
	 * received (k).
	 */	
	public KTree (E[] arrayTree, int k)
	{
		kFactor = k;
		height = 0;
		size = 0;
		currentHeight = 0;
		
		if (arrayTree.length == 0)
			root = null;
		else
		{	
			size = 1;
			root = new TreeNode<E>(arrayTree[0]);
			build(root, 0, arrayTree);				// building the tree with with the new root.
		}
	}
	
	/**
	* This method builds the tree as a linked structure, from the tree stored in the form of
	* an array, which is received as a parameter. This method is recursive.
	* @param current Reference to a node that is the root of the subtree being built in this call to the method.
	* @param curIndex int that represents the index that the root to this subtree would have in an array representation.
	* @param arrayTree Generic array of elements of type (E) that is temporarily storing the tree representation of 
	* the k-ary tree. Where there is no elements in the tree the position in the array will be of value (null). The
	* array is as long as if the tree was a full complete tree.
	*/
	private void build(TreeNode<E> current, int curIndex, E[] arrayTree)
	{
		int[] chIndexArray = getChildren(curIndex, arrayTree);  // getting list of indexes to children of this node in
																// the array representation of tree.
		TreeNode<E> newChild;
		
		for(int i=0; i<kFactor; i++)     //  traversing list of children
		{
			if(chIndexArray[i] == -1)		// if this position on children-indexes array is -1, the link to child is null.
				current.children[i] = null;
			else							// if this position is one of a child to this node...
			{
				size++;						// increase size of the tree.
				newChild = new TreeNode<E>( arrayTree[chIndexArray[i]] );  // create a new node for the child
				current.children[i] = newChild; 						   // and link it to the list of children.
				
				currentHeight++;             // height of tree grew at this point
				if (currentHeight > height)		//  if current height is greater than official height
					height++;					//  update the official height of the tree
				
				build(newChild, chIndexArray[i], arrayTree);  // recursive call to the method (build), with the 
												// current child as the root of the new subtree cuil by t
				
				currentHeight--;			//  decrease the current height of tree before leaving this
											//  iteration of method (build) since at this point the 
											//  program will be moving back up one level in the tree.
			}
		}
	}
	
	/**
	* Method that receives an index and the array representation of a tree and returns an array of indexes
	* of the children of that node represented by the index received as parameter.
	* @param curIndex integer value that represents the position on the arrayTree of a node of the tree.
	* @param arrayTree Array of generic elements that represents the tree. 
	* @return int array that holds the list of children of a node in the array Tree.
	*/
	private int[] getChildren(int curIndex, E[] arrayTree)
	{
		int[] chList = new int[kFactor];
		int position;
		
		for (int i=0; i<kFactor; i++)
		{
			position = (curIndex * kFactor) + i + 1;
			if (position >= arrayTree.length || arrayTree[position] == null)
				chList[i] = -1;
			else
				chList[i] = position;
		}
				
		return chList;
	}
	
	
//*********************************** PART 1 METHODS **********************************************
	
	/**
	 * This method returns the k factor of the tree.
	 * @return int value with the max number of children per node of the tree.
	 */
	public int getK()
	{
		return kFactor;
	}
	
	/**
	 * Method that returns the number of elements of the tree.
	 * @return int value with number of elements of the tree.
	 */
	public int size()
	{
		return size;
	}
	
	/**
	 * Method that returns the height of the tree.
	 * @return int value with the height of the tree.
	 */
	public int height()
	{
		return height;
	}
	

	/**
	 * Method that receives a node that is the root of a subtree in the link-structure tree. Then it prints
	 * all of its elements in the Pre-Order format using recursivity.
	 * @param current Generic type tree node which is the root a subtree to be printed.
	 */
	private void preOrderPrint(TreeNode<E> current)
	{
		if (current != null)
		{
			System.out.println(current.value.toString() + " ");
			for (int i=0; i<kFactor; i++)
			{
				if (current.children[i] != null)
					preOrderPrint(current.children[i]);
			}
		}
	}

	
	/**
	 * Method that returns the value of a number (k) to the power (p).
	 * @param k int Number to be elevated to a power.
	 * @param p int Exponent to elevate the k number to.
	 * @return int value with the number (k) to the power (p).
	 */
	private int pow(int k, int p)
	{
		int result = 1;
		for (int i=0; i<p; i++)
			result = result * k;
		return result;
	}	
	
	
	/**
	 * This is a recursive method that builds the array representation of a tree from the link-structure tree.
	 * This method traverses the tree and while it is visiting every node it keeps track of which index that
	 * node would be in if it was in the array representation of the tree and keeping that index in the variable
	 * named (tracer).
	 * @param current Generic tree node that represents is considered the root of a subtree which is currently
	 * visited.
	 * @param array Object type array of elements that is holding the elements of the tree in the array representation.
	 */
	private void buildArray(TreeNode<E> current, Object[] array)
	{
		array[tracer] = current.value;   // inserting the current element of the tree in the array repre. of the tree.
		
		for (int i=0; i<kFactor; i++)	// visiting the children of the node
		{
			if (current.children[i] != null)   // if there is a child node
			{
				tracer = (tracer * kFactor) + i+1;     // update the tracer through math formula to position child in the array
				buildArray( current.children[i], array);   // recursively calling the function, now with a child node as the root.
			}
		}
		
		tracer = (tracer-1)/kFactor;	// updating tracer again to represent once again the current node visited.
	}
	
		
	/**
	 * Method that returns the array representation of the tree from traversing the link-structure tree. This method
	 * makes the first call to the recursive method buildArray to create the array tree.
	 * @return Object type array that holds array representation of the tree.
	 */
	public Object[] toArray()
	{
		int completeSize = (pow(kFactor,height+1)-1) / (kFactor-1);
		
		Object[] array = new Object[completeSize];
		tracer = 0;         //  initializing the tracer of index of each visited node in link-structure tree.
		
		buildArray(root,array);   // first call to the recursive method that builds the tree.

		return array;
	}

	/**
	 * Method that returns a reference to the tree node that is located in the a specific position (index) in the tree.
	 * The method receives the reference to a node in the tree (current) and the index of the node targeted. This is
	 * a recursive method that uses the variable (tracer) to keep track of the index of the node currently visited.
	 * @param current Generic TreeNode that is the root of a subtree 
	 * @param index int value thar represents the position of a node in the tree (if tree was in array representation).
	 * @return Generic TreeNode with that is the target of our search.
	 */
	private TreeNode<E> getTreeNode(TreeNode<E> current, int index)
	{
		TreeNode<E> target = null;
		TreeNode<E> found = null;
		
		if (tracer == index)
			found = current;
		else
		{
			for (int i=0; i<kFactor; i++)
			{
				if (current.children[i] != null)
				{
					tracer = (tracer * kFactor) + i+1;
					target = getTreeNode( current.children[i], index );
					if (target != null)
						found = target;
				}
			}
			
			tracer = (tracer-1)/kFactor;
			
		}
			
		return found;
	}
	
	
	/**
	 * Method that returns the data of the node located in the index (i) in the tree.
	 * @param i int Represents the index of a node in the tree.
	 * @return E value with the data of the node of the tree at index (i).
	 */
	public E get(int i)
	{
		tracer = 0;			// initializing tracer to position 0 (root of the tree)
		E element = null;
		
		TreeNode<E> target = getTreeNode(root, i);  // get the tree node at index i from the link-structure tree.
		if (target != null)
			element = target.value;
		
		return element;
	}

	
	/**
	 * Method that deletes a node located in the index (i) which must be the leaf of the tree in order to execute deletion.
	 * This method obtains an array representation of the tree by invoking the method toArray() and then performs the
	 * deletion of the tree node (if possible). Then it rebuilds the link-representation of the tree, just like it is
	 * done in the constructor of the KTree class. Then the root, height and size of the tree are updated.
	 * @param i int Represents a position in the tree.
	 * @return boolean value which indicates whether or not the deletion was successful. 
	 */
	@SuppressWarnings("unchecked")
	private boolean deleteLeafByIndex(int i)
	{
		boolean success = true;
		TreeNode<E> target = getTreeNode(root, i);

		boolean hasChildren = false;          //
		for (int j=0; j<kFactor; j++)         //  Routine to check if Node has children
			if (target.children[j] != null)   //
				hasChildren = true;           //
		
		if (hasChildren || target == null) // if Node to delete is not a leaf or the index (i) is not element of the tree.
			success = false;
		
		
		else  // if the Node has no children and (i) is index to an element of the tree
		{
			if(i != 0)  // if element to delete is not the root
			{
				E[] newArrayTree = (E[])toArray();   // turn the tree into an array (temporarily)
				newArrayTree[i] = null;              // turn that desired index of the tree into a null (delete element)
				
				height = 0;									//
				size = 1;                               	//
				root = new TreeNode<E>(newArrayTree[0]);	//  re-build the tree.
				build(root, 0, newArrayTree);				//
			}
			else   //  if we are deleting the root
			{
				height = size = 0;
				root = null;
			}
		}
		
		return success;
	}
	
	/**
	 * Method that expands the size of the array representing the tree if an insertion is made that requires the 
	 * creation of a whole new level of the tree.
	 * @param arrayTree Generic type array with that holds the array representation of the tree before expansion.
	 * @return returns the new expanded array.
	 */
	@SuppressWarnings("unchecked")
	private E[] expandArrayTree(E[] arrayTree)
	{
		height++;     // increasing height of the tree.
		
		int completeSize = (pow(kFactor,height+1)-1) / (kFactor-1);	// calculates the new size of the tree with one extra level.
				
		E[] expandedArrayTree = (E[]) new Object[completeSize];

		for (int i=0; i<arrayTree.length; i++)		// copying every element of arrayTree onto the expanded array.
			expandedArrayTree[i] = arrayTree[i];
		
		return expandedArrayTree;
	}


	/**
	 * Method that adds a node located in the index (i) which must be the leaf of the tree in order to execute insertion.
	 * This method obtains an array representation of the tree by invoking the method toArray() and then performs the
	 * insertion of the new node (if possible). Then it rebuilds the link-representation of the tree, just like it is
	 * done in the constructor of the KTree class. Then the root, height and size of the tree are updated.
	 * @param i int Represents a position in the tree.
	 * @param v Generic type value, which will be stored in the new element to be inserted in the tree.
	 * @return boolean value which indicates whether or not the insertion was successful. 
	 */
	@SuppressWarnings("unchecked")
	private boolean addLeafByIndex(int i, E v)
	{
		boolean success = true;
		
		if ((root == null) && (i == 0))   // if the tree is empty at the moment and we are trying to insert at root
			root = new TreeNode<E>(v);
		
		else
		{	
			E[] newArrayTree = (E[])toArray();   // turn the tree into an array (temporarily)
			int possibleParentPos = i/kFactor;
			int completeSize = (pow(kFactor,height+1)-1) / (kFactor-1);
		
			if (i < completeSize) 
			{	
				if (newArrayTree[possibleParentPos] != null)
				{
					newArrayTree[i] = v;						//  add the new element and
					height = 0;									//
					size = 1;                               	//
					root = new TreeNode<E>(newArrayTree[0]);	//  re-build the tree.
					build(root, 0, newArrayTree);				//
				}

				else
					throw new InvalidTreeException();
				
			}
			else // (if i >= completeSize)  meaning we are trying to add element at any level below the height of the tree
			{
				if (possibleParentPos < completeSize)      //  if possible parent position is within height of tree
				{	
					if (newArrayTree[possibleParentPos] != null)  // and if possible parent of new element is not null
					{
						newArrayTree = expandArrayTree(newArrayTree); 	//  expand size of the array of the tree
						
						newArrayTree[i] = v;							//  add the new element and
						height = 0;										//
						size = 1;                               		//
						root = new TreeNode<E>(newArrayTree[0]);		//  re-build the tree.
						build(root, 0, newArrayTree);					//
						
					}
					else
						throw new InvalidTreeException();
				}	
			}
		}			
				
		return success;
	}

	/**
	 * Method that sets a value v into a node located at index (i).  If there is no existing node at index (i) then
	 * create a new node as long as it will be a leaf node that will not make the tree invalid.   If the value we 
	 * are trying to set a node to is null, then delete such element from the tree as long as the element at index (i)
	 * is a leaf, and that such deletion will not make the tree invalid.
	 * @param i int Index of the node in the tree.
	 * @param v Generic type value to set into the tree node.
	 * @return boolean value that represents whether or not the setting of a value into a tree node was successful.
	 */
	public boolean set(int i, E v)
	{
		tracer = 0;
		boolean success = true;
		TreeNode<E> target = getTreeNode(root, i);
		
		if ((i < 0))			   // if it is a negative index (invalid index).
			success = false;
		
		else if (v == null)        // if value to set is a null, try to delete the element from tree. 
			success = deleteLeafByIndex(i);

		else if (target != null)   // if element at index (i) is part of the tree
			target.value = v;
	
		else    				   // if (i) is a position on the tree that does not exist yet, try to add as a leaf.
			success = addLeafByIndex(i,v);
		
		return success;
	}
	
	
	
//***************************************** PART 2 METHODS ***********************************************
	
	/**
	 * Method that returns a string with the array representation of the tree (level by level) having each
	 * level of the tree in a separate line.
	 * @return String with the array representation of the tree.
	 */
	public String toString()
	{
		String output = "";
		int exponent = 0;
		int levelElementsAccum = 0;			// total number of elements of a tree with a certain height (exponent).
		Object[] arrayTree = toArray();		// getting the array representation of the tree.
		
		for (int i=0; i<arrayTree.length; i++)  // building the string (output)
		{
			output = output + arrayTree[i] + " ";

			if (i == levelElementsAccum)    // if elements read so far are same as elements of a tree of height equal to exponent
			{
				output = output + "\n";		// then add a change of line.
				exponent++;					// increase the height (exponent)
				levelElementsAccum = levelElementsAccum + pow(kFactor,exponent);	// increase elements to fill tree of new height.
			}
		}		

		return output;
	}
	
	
	/**
	 * Method that returns a string (output) with the representation of the tree in Level-Order
	 * @return String with the representation of the tree in Level-order.
	 */
	public String toStringLevelOrder()
	{
		String output = "";
		Object[] arrayTree = toArray();		// obtaining the array representation of the tree
		
		for (int i=0; i<arrayTree.length; i++)	// copying the data of each node into the string (output).
		{
			if (arrayTree[i] != null)
				output = output + arrayTree[i] + " ";
		}
		
		return output;
	}

	
	/**
	 * Helper recursive method that builds the string representation of the tree while traversing it in Pre-Order fashion.
	 * @param subTreeRoot TreeNode Current tree node visited in the Pre-Order fashion, which is considered the root of 
	 * a new subtree to be also traversed (recursively).
	 * @return String with the values of each tree node for which subTreeRoot is the root
	 */
	private String buildStringPreOrder(TreeNode<E> subTreeRoot)
	{
		String output = "";
		
		if (subTreeRoot != null)
		{	
			output = output + subTreeRoot.value + " ";	// adding the current node to the string (output).
			
			for (int i=0; i<kFactor; i++)				// visiting every child of the current node to traverse in pre-order.
				output = output + buildStringPreOrder(subTreeRoot.children[i]);
		}

		return output;
	}
	
	
	/**
	 * Method that builds the String with the values of each node of the tree by traversing it in Pre-order fashion.
	 * This is done by making the first call to the recursive method buildStringPreOrder().
	 * @return String with the representation of the tree when traversing it in the Pre-Order fashion.
	 */
	public String toStringPreOrder()
	{
		String output = "";
		
		output = buildStringPreOrder(root);
		
		return output;
	}

	
	/**
	 * Helper recursive method that builds the string representation of the tree while traversing it in Post-Order fashion.
	 * @param subTreeRoot TreeNode Current tree node visited in the Post-Order fashion, which is considered the root of 
	 * a new subtree to be also traversed (recursively).
	 * @return String with the values of each tree node for which subTreeRoot is the root
	 */
	private String buildStringPostOrder(TreeNode<E> subTreeRoot)
	{
		String output = "";
		
		if (subTreeRoot != null)
		{	
			for (int i=0; i<kFactor; i++)		// visiting every child of the current node to traverse in Post-Order.
				output = output + buildStringPostOrder(subTreeRoot.children[i]);

			output = output + subTreeRoot.value + " ";		// adding the current node to the string (output).
		}

		return output;
	}

	
	/**
	 * Method that builds the String with the values of each node of the tree by traversing it in Post-Order fashion.
	 * This is done by making the first call to the recursive method buildStringPreOrder().
	 * @return String with the representation of the tree when traversing it in the Post-Order fashion.
	 */
	public String toStringPostOrder()
	{
		String output = "";
		
		output = buildStringPostOrder(root);
		
		return output;
	}


	/**
	 * Method that takes a tree in an array representation (arrayTree) and returns a new array with the same tree array
	 * representation without the null nodes.
	 * @param arrayTree Object Array with the original representation of the tree.
	 * @return Object array type that holds the new array without the null values.
	 */
	public Object[] trim( Object[] arrayTree)
	{
		int next = 0;
		Object[] trimmedArrayTree = new Object[size];	// creates a new array
		
		for (int i=0; i<arrayTree.length; i++)			// copies only elements of the tree that are not null.
		{
			if (arrayTree[i] != null)
				trimmedArrayTree[next++] = arrayTree[i];
		}
		
		return trimmedArrayTree;
	}
	
	
	/**
	 * This overrides the getLevelOrderIterator of the interface (TreeIterable) that will traverse the tree in Level 
	 * Order fashion.  This method will utilize the array Level Order array representation of the tree created by the 
	 * constructor of the iterator and holds it as an attribute to further obtain the next element visited.
	 */
	@SuppressWarnings("unchecked")  // Tag meant to skip giving warnings due to unsafe (E) casting on Objects
	public Iterator<E> getLevelOrderIterator()
	{
		/**
		 * Nested declaration of the Iterator which provides the methods to traverse the tree in Level-Order fashion.
		 * @param <T> Generic type that specifies the type of value held in every node of tree. 
		 */
		return new Iterator<E>()
		{
			int current = 0;   // index to first element in Level Order Traversal.
			Object[] levelOrderArray = trim(toArray());
			/**
			 * Returns true if the tree has a next element.
			 * @return Boolean value which indicates if there is a next element in tree.
			 */
			public boolean hasNext()
			{
				return (current < levelOrderArray.length);
			}
	
			/**
			 * Returns value held by the next node on the tree.
			 * @return T Generic type value held by the next node on the tree.	
			 */
			public E next()
			{
				if (current >= levelOrderArray.length)
					throw new NullPointerException("There was no next item on tree");
				
				E value =  (E)levelOrderArray[current++];

				return value;
			}
		};
	}
	
	
	/**
	 * Helper recursive method that builds the array representation of the tree when traversed in Pre-Order fashion.
	 * @param subTreeRoot Generic TreeNode Current tree node visited which in turn will be traversed in Pre-Order by
	 * calling itself recursively while building the array representation or the tree.
	 * @param array Object array type that holds the array that is being built by while traversing the tree.
	 */
	private void buildPreOrderArray(TreeNode<E> subTreeRoot, Object[] array)
	{
		if (subTreeRoot != null)
		{	
			array[tracer++] = subTreeRoot.value;
			
			for (int i=0; i<kFactor; i++)
				buildPreOrderArray(subTreeRoot.children[i], array);
		}

	}
	
	
	/**
	 * Method that returns an array representation of the tree when traversed in Pre-Order fashion.
	 * This method makes the first call to the recursive helper method buildPreOrderArray.
	 * @return
	 */
	private Object[] toPreOrderArray()
	{
		Object[] array = new Object[size];
		tracer = 0;			// initializing the tracer variable for the building of the array.
		buildPreOrderArray(root, array);
		return array;
	}

	
	/**
	 * This overrides the getPreOrderIterator of the interface (TreeIterable) that will traverse the tree in Pre 
	 * Order fashion.  This method will utilize the array Pre-Order array representation of the tree created by the 
	 * constructor of the iterator and holds it as an attribute to further obtain the next element visited.
	 */
	@SuppressWarnings("unchecked")  // Tag meant to skip giving warnings due to unsafe (E) casting on Objects
	public Iterator<E> getPreOrderIterator()
	{
		/**
		 * Nested declaration of the Iterator which provides the methods to traverse the tree in Level-Order fashion.
		 * @param <T> Generic type that specifies the type of value held in every node of tree. 
		 */
		return new Iterator<E>()
		{
			int current = 0;   // index to first element in Pre-Order Traversal.
			Object[] preOrderArray = toPreOrderArray();

			/**
			 * Returns true if the tree has a next element.
			 * @return Boolean value which indicates if there is a next element in tree.
			 */
 			public boolean hasNext()
			{
				return (current < preOrderArray.length);
			}
	
			/**
			 * Returns value held by the next node on the tree.
			 * @return T Generic type value held by the next node on the tree.	
			 */
			public E next()
			{
				if (current >= preOrderArray.length)
					throw new NullPointerException("There was no next item on tree");
				
				E value =  (E)preOrderArray[current++];

				return value;
			}
		};
	}	


	/**
	 * Helper recursive method that builds the array representation of the tree when traversed in Post-Order fashion.
	 * @param subTreeRoot Generic TreeNode Current tree node visited which in turn will be traversed in Post-Order by
	 * calling itself recursively while building the array representation or the tree.
	 * @param array Object array type that holds the array that is being built by while traversing the tree.
	 */
	private void buildPostOrderArray(TreeNode<E> subTreeRoot, Object[] array)
	{
		if (subTreeRoot != null)
		{	
			for (int i=0; i<kFactor; i++)
				buildPostOrderArray(subTreeRoot.children[i], array);

			array[tracer++] = subTreeRoot.value;
		}

	}
	

	/**
	 * Method that returns an array representation of the tree when traversed in Post-Order fashion.
	 * This method makes the first call to the recursive helper method buildPreOrderArray.
	 * @return
	 */
	private Object[] toPostOrderArray()
	{
		Object[] array = new Object[size];
		tracer = 0;				// initializing the tracer variable for the building of the array.
		buildPostOrderArray(root, array);
		return array;
	}
	

	/**
	 * This overrides the getPostOrderIterator of the interface (TreeIterable) that will traverse the tree in Post 
	 * Order fashion.  This method will utilize the array Post-Order array representation of the tree created by the 
	 * constructor of the iterator and holds it as an attribute to further obtain the next element visited.
	 */
	@SuppressWarnings("unchecked")  // Tag meant to skip giving warnings due to unsafe (E) casting on Objects
	public Iterator<E> getPostOrderIterator()
	{
		/**
		 * Nested declaration of the Iterator which provides the methods to traverse the tree in Post-Order fashion.
		 * @param <T> Generic type that specifies the type of value held in every node of tree. 
		 */
		return new Iterator<E>()
		{
			int current = 0;   // index to first element in Post-Order Traversal.
			Object[] postOrderArray = toPostOrderArray();

			/**
			 * Returns true if the tree has a next element.
			 * @return Boolean value which indicates if there is a next element in tree.
			 */
 
			
			public boolean hasNext()
			{
				return (current < postOrderArray.length);
			}
	
			/**
			 * Returns value held by the next node on the tree.
			 * @return T Generic type value held by the next node on the tree.	
			 */
			public E next()
			{
				if (current >= postOrderArray.length)
					throw new NullPointerException("There was no next item on tree");
				
				E value =  (E)postOrderArray[current++];

				return value;
			}
		};
	}	

//***************************************** PART 3 METHODS ***********************************************
	
	/**
	 * Helper method that returns true or false value depending on whether or not a certain node of the tree (current)
	 * has any children.
	 * @param current TreeNode<String> Node being examined for children.
	 * @param kFactor int K factor of the tree.
	 * @return boolean value that represents whether or not this node has any children.
	 */
	private static boolean hasChildren(KTree<String>.TreeNode<String> current, int kFactor)
	{
		boolean hasCh = false;
		for (int j=0; j<kFactor; j++)       	//  Routine to check if Node has children
			if (current.children[j] != null)  	//
				hasCh = true;       		  	//
		
		return hasCh;
		
	}
	
	
	/**
	 * Method that decoded the message received in a string (codedMessage) along with the tree that holds the decoding
	 * structure.  This method will return the decode message.
	 * @param tree KTree<String> Holds the decoding structure.
	 * @param codedMessage String Coded message about to be decoded.
	 * @return String with the message decoded with the help of the tree.
	 */
	public static String decode(KTree<String> tree, String codedMessage)
	{
		String message = "";
		int move = Character.getNumericValue(codedMessage.charAt(0)); // gets numeric value represented by a char of codedMessage. 
		KTree<String>.TreeNode<String> current = tree.root;
		
		for (int i=1; i<codedMessage.length(); i++)  // walks through every character of the codedMessage
		{
			current = current.children[move];    // moves down the tree depending on the current character of codedMessage.
			
			if (!hasChildren(current, tree.kFactor))	// if current node visited is a leaf
			{
				message = message + current.value;		// add leaf value to the decoded message
				current = tree.root;					// goes back to the root of the tree.
			}
			move = Character.getNumericValue(codedMessage.charAt(i)); // gets numeric value represented by a char of codedMessage.
		}
		
		current = current.children[move];		// moves to the last leaf pointed by the codedMessage
		message = message + current.value;		// adds the value of the last leaf visited by this method.
		return message;
	}
	

//*************************************** EXTRA CREDIT METHODS ***********************************************

	/**
	 * Method that takes an array representation of the tree and returns one without the null elements that come
	 * after the last non-null element of the tree.
	 * @param array Object array Representation of the tree.
	 * @return Object array type with trimmed representation of the tree.
	 */
	private Object[] trimEnd(Object[] array)
	{
		int newSize = 0;
		
		for (int i=0; i<array.length; i++)	//
			if (array[i] != null)			//	getting the new size that will be used to create the 
				newSize = i;				//	new array that won't include any nulls after the last element
		newSize++;							//
		
		Object[] trimmedArray = new Object[newSize];	// creates the new array.
		
		for (int j=0; j<newSize; j++)		//	copies all elements (except for null elements) of the original  
			trimmedArray[j] = array[j];		//	array into the new array (trimmedArray)

		return trimmedArray;
	}
	
	
	/**
	 * Method that returns an array with the representation of the subtree which has the element at location (i) of the
	 * tree as the root.
	 * @param i int Index of the node which will be the root of the subtree to be returned in an array representation.
	 * @return Generic type array representation of the subtree.
	 */
	@SuppressWarnings("unchecked")
	public E[] subtree(int i)	
	{
		int completeSize = (pow(kFactor,height+1)-1) / (kFactor-1); // calculates the number or elements of the subtree to create.
		
		Object[] subTreeArray = new Object[completeSize];	

		tracer = 0;			// initializing helping variable (tracer) to keep track of index of currently visited node
		TreeNode<E> newRoot = getTreeNode(root,i); 		// getting the node at index (i), root of the target subtree.
		
		tracer = 0;			// re-initializing helping variable (tracer).
		buildArray(newRoot, subTreeArray);				// building the array representation of the subtree.
		
		return (E[]) trimEnd(subTreeArray);  			// trimming the null elements at end of array repr. of the tree.
	}
	
	
	/**
	 * Helper method that obtains one level of the tree in the array representation (arrayTree) and reversed and inserts
	 * it at the end of the new array (mirrorAr). 
	 * @param arrayTree Object array Holds the original array representation of the tree.
	 * @param mirrorAr Object array Holds the mirror array representation of the tree.
	 * @param beginIndex int Index where a level of the tree begins.
	 * @param endIndex int Index where a level of the tree ends.
	 */
	void appendReversedLevel(Object[] arrayTree, Object[] mirrorAr, int beginIndex, int endIndex)
	{
		int levelLength = endIndex - beginIndex;
		for (int i=0; i<=levelLength; i++)
		{
			mirrorAr[endIndex - i] = arrayTree[beginIndex + i];
		}
	}

	/**
	 * Method that returns an array representation of the tree (with the elements of each level reversed).
	 * @return Generic type array with the mirror image of the tree.
	 */
	@SuppressWarnings("unchecked")
	public E[] mirror()
	{
		Object[] arrayTree = toArray();
		Object[] mirrorAr = new Object[arrayTree.length];

		int exponent = 0;
		int levelElementsAccum = 0;

		int beginIndex = 0;
		int endIndex = 0;
		
		for (int i=0; i<arrayTree.length; i++)	// traversing every element of the original array repr. of the tree.
		{
			if (i == levelElementsAccum) 		// if we are at the last element of a level of the tree.
			{
				endIndex = i;					// updates variables that keep track of indexes of a level of the tree.
				exponent++;						//
				levelElementsAccum = levelElementsAccum + pow(kFactor,exponent);

				appendReversedLevel(arrayTree, mirrorAr, beginIndex, endIndex); // reversing the tree elements of a level
				
				beginIndex = i+1;
			}
		}		

		return (E[])mirrorAr;
	}
	
	
//*************************************** MAIN METHOD *****************************************************
	
//	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		//change this method around to test!
		methodSigCheck();

		String[] string1 = { "A", "B", "C", "D", "E", null, null };
		KTree<String> tree1 = new KTree<String>(string1, 2);
		String[] strings2 = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
		KTree<String> tree2 = new KTree<>(strings2, 3);
		
		System.out.println("Testing getK()");
		System.out.println("K Factor of tree1 is: " + tree1.getK());	
		System.out.println("K Factor of tree2 is: " + tree2.getK());	
		System.out.println(" ");
		
		System.out.println("Testing size()");
		System.out.println("Size of tree1 is: " + tree1.size());	
		System.out.println("Size of tree2 is: " + tree2.size());	
		System.out.println(" ");
		
		System.out.println("Testing height()");
		System.out.println("Height of tree1 is: " + tree1.height());	
		System.out.println("Height of tree2 is: " + tree2.height());	
		System.out.println(" ");
		
		System.out.println("Testing get()");
		System.out.println("Node at index 3 in tree1 is: " + tree1.get(3));	
		System.out.println("Node at index 5 in tree2 is: " + tree2.get(5));	
		System.out.println(" ");

		System.out.println("Testing set(i,v)");
		tree2.set(5,"yay");
		System.out.println("Node at index 5 in tree1 is: " + tree2.get(5));	
		tree2.set(13,"yay");
		System.out.println("Node at index 13 in tree1 is: " + tree2.get(13));	
		tree2.set(13,null);
		System.out.println("Node at index 13 in tree1 is: " + tree2.get(13));	
		System.out.println(" ");
		
		System.out.println("Testing toArray()");
		Object[] treeArray1 = tree1.toArray();
		Object[] treeArray2 = tree2.toArray();

		for (int i=0; i<treeArray1.length; i++)
			System.out.print(treeArray1[i] + " ");
		System.out.println(" ");

		for (int i=0; i<treeArray2.length; i++)
			System.out.print(treeArray2[i] + " ");
		System.out.println(" ");
		System.out.println(" ");

		System.out.println("Testing toString()");
		System.out.println(tree1.toString());
		System.out.println(tree2.toString());
		System.out.println(" ");

		System.out.println("Testing toStringLevelOrder()");
		System.out.println(tree1.toStringLevelOrder());
		System.out.println(tree2.toStringLevelOrder());
		System.out.println(" ");

		System.out.println("Testing getLevelOrderIterator()"); 
		Iterator<String> it1 = tree1.getLevelOrderIterator();
		for(int i=0; i<tree1.size(); i++)
			System.out.print( it1.next() + " ");
		System.out.println(" ");

		Iterator<String> it2 = tree2.getLevelOrderIterator();
		for(int i=0; i<tree2.size(); i++)
			System.out.print( it2.next() + " ");
		System.out.println(" ");
		System.out.println(" ");

		System.out.println("Testing toStringPreOrder()");
		System.out.println(tree1.toStringPreOrder());
		System.out.println(tree2.toStringPreOrder());
		System.out.println(" ");

		System.out.println("Testing getPreOrderIterator()"); 
		it1 = tree1.getPreOrderIterator();
		for(int i=0; i<tree1.size(); i++)
			System.out.print( it1.next() + " ");
		System.out.println(" ");

		it2 = tree2.getPreOrderIterator();
		for(int i=0; i<tree2.size(); i++)
			System.out.print( it2.next() + " ");
		System.out.println(" ");
		System.out.println(" ");
		
		System.out.println("Testing toStringPostOrder()");
		System.out.println(tree1.toStringPostOrder());
		System.out.println(tree2.toStringPostOrder());
		System.out.println(" ");
		
		System.out.println("Testing getPostOrderIterator()"); 
		it1 = tree1.getPostOrderIterator();
		for(int i=0; i<tree1.size(); i++)
			System.out.print( it1.next() + " ");
		System.out.println(" ");

		it2 = tree2.getPostOrderIterator();
		for(int i=0; i<tree2.size(); i++)
			System.out.print( it2.next() + " ");
		System.out.println(" ");
		System.out.println(" ");
		
		System.out.println("Testing decode(tree, codedMessage)");
		KTree<String> codeTree1 = new KTree<>(new String[] {"_","_","_","_","_","_","U","I","H","G",null,"M","-",null,null}, 2);
		System.out.println( decode(	codeTree1, "00100010101010011"));
		
		KTree<String> codeTree2 = new KTree<>(new String[] {"_","_","A","_","E","_","B",null,null,null,"R",null,null}, 3);
		System.out.println( decode(	codeTree2, "02120020020"));
		System.out.println(" ");
		
		System.out.println("Testing mirror()");
		Object[] tempAr1 = tree1.mirror();

		for (int i=0; i<tempAr1.length; i++)
			System.out.print(tempAr1[i] + " ");				
		System.out.println(" ");

		Object[] tempAr2 = tree2.mirror();

		for (int i=0; i<tempAr2.length; i++)
			System.out.print(tempAr2[i] + " ");			

	}
	
	
	/****************************************/
	/* DO NOT EDIT ANYTHING BELOW THIS LINE */
	/****************************************/
	
	public static void methodSigCheck() {
		//This ensures that you've written your method signatures correctly
		//and understand how to call the various methods from the assignment
		//description.
		
		String[] strings = { "_", "_", "A", "B", "N", null, null };
		
		KTree<String> tree = new KTree<>(strings, 2);
		int x = tree.getK(); //should return 2
		int y = tree.size(); //should return 5
		int z = tree.height(); //should return 2
		
		String v = tree.get(0); //should be "_"
		boolean b = tree.set(0, "x"); //should set the root to "x"
		Object[] o = tree.toArray(); //should return [ "x", "_", "A", "B", "N", null, null ]
		
		String s = tree.toString(); //should be "x\n_ A\nB N null null"
		String s2 = "" + tree; //should also be "x\n_ A\nB N null null"
		
		Iterator<String> it1 = tree.getLevelOrderIterator(); //gets an iterator
		Iterator<String> it2 = tree.getPreOrderIterator(); //gets an iterator
		Iterator<String> it3 = tree.getPostOrderIterator(); //gets an iterator
		
		String s3 = tree.toStringLevelOrder(); //should be "_ _ A B N"
		String s4 = tree.toStringPreOrder(); //should be "_ _ B N _ N"
		String s5 = tree.toStringPostOrder(); //should be "B N _ A _"
		
		String s6 = decode(tree, "001011011"); //should be "BANANA"
		
		Object[] o2 = tree.mirror(); //should return [ "x", "A", "_", null, null, "N", "B" ]
		Object[] o3 = tree.subtree(1); //should return [ "_", "B", "N" ]
	
	}
}