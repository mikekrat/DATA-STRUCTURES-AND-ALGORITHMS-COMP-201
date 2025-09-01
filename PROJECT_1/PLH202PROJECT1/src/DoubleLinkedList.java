
import java.util.ArrayList;

/**
 * 
 * @author Mike Kratimenos
 * @param <E>
 * @version https://www.java2novice.com/data-structures-in-java/linked-list/doubly-linked-list/
 */

public class DoubleLinkedList<E> {
		 
		/**
		 * it is DoubleLinkedList's head (first node)
		 */
	    private Node head;
	    /**
	     * it is DoubleLinkedList's tail (last node)
	     */
	    private Node tail;
	    /**
	     * it is it is DoubleLinkedList's size (length)
	     */
	    private int size;
	    /**
	     * This method is an empty constructor for DoubleLinkedList
	     */
	     
	    public DoubleLinkedList() {
	        size = 0;
	    }
	    
	    /**
	     * this class keeps track of each element information
	     * @author Mike Kratimenos
	     */
	    public class Node {
	    	/**
		     * it is Node's element
		     */
	        String element;
	        /**
		     * it is Node's next node
		     */
	        Node next;
	        /**
		     * it is Node's previous node
		     */
	        Node prev;
	        
	        /**
	         * This method is Node's complete constructor
	         * @param element
	         * @param next
	         * @param prev
	         */
	        public Node(String element, Node next, Node prev) {
	            this.element = element;
	            this.next = next;
	            this.prev = prev;
	        }
	    }
	    /**
	     * This method is a getter for DoubleLinkedList's head
	     * @return DoubleLinkedList's head
	     */
	    public Node getHead() {
			return head;
		}
	    /**
	     * This method is a getter for DoubleLinkedList's tail
	     * @return DoubleLinkedList's tail
	     */
		public Node getTail() {
			return tail;
		}
		/**
	     * This method is a getter for DoubleLinkedList's size
	     * @return DoubleLinkedList's size
	     */
		public int getSize() {
			return size;
		}
	     
	    /**
	     * This method adds an element (parameter element) at DoubleLinkedList's end
	     * @param element
	     */
	    public void addLast(String element) {
	         
	        Node tmp = new Node(element, null, tail);
	        if(tail != null) {tail.next = tmp;}
	        tail = tmp;
	        if(head == null) { head = tmp;}
	        size++;
	    }
	     
	    /**
	     * This method prints DoubleLinkedList's elements and their indexes if parameter tf is true 
	     * @param tf
	     */
	    @SuppressWarnings("unused")
		public void print(boolean tf){
	        int counter = 0;
	        Node tmp = head;
	        if(tmp.next!=null){
		        while(tmp != null){
		        	counter++;
		        	if(tf==true){
		        		System.out.println(counter+") "+tmp.element);
		        	}else
		        		System.out.println(tmp.element);
		            tmp = tmp.next;
		        }
		        System.out.println("");
	        } else if(tmp!=null) {
	        	counter++;
	            System.out.println(counter+") "+tmp.element);	            
	        } else {
	        	System.out.println("The list is empty...");
	        }
	    }
	    /**
	     * This method is used to delete a node (parameter node) from DoubleLinkedList
	     * @param node
	     */
	    public void deleteNode(Node node){
	    	if(size == 0){
	    		System.out.println("The list is empty...\n");
	    		return ;
	    	}
	    	if(node.next!=null && node.prev!=null){
	    		Node tmp = node;
	    		Node tmp1 = node.next;
	    		Node tmp2 = node.prev;
	    		node = tmp2;
	    		node.next = tmp1;
	    		node = tmp1;
	    		node.prev = tmp2;
	    		size--;
	    		System.out.println("the current line: "+tmp.element+" is deleted!!\n");
	    	}
	    	else if(node.next!=null && node.prev==null){
	    		Node tmp = node;
	    		node = node.next;
	    		head = head.next;
	    		node.prev = null;
	    		size--;
	    		System.out.println("the first line: "+tmp.element+" is deleted!!\n");
	    	}
	    	else if(node.next==null && node.prev!=null){
	    		Node tmp = node;
	    		node = node.prev;
	    		tail = tail.prev;
	    		node.next = null;
	    		size--;
	    		System.out.println("the last line: "+tmp.element+" is deleted!!\n");
	    	}
	    	
	    	
	    }
	    /**
	     * This method adds a new node after the given node (parameter node) in the DoubleLinkedList 
	     * @param node
	     * @param newElement is new node's element
	     */
	    public void addAfterCurrentNode(Node node,String newElement){
	    	if(size == 0){
	    		System.out.println("The list is empty...\n");
	    		return ;
	    	}
	    	if(node.next!=null){
		    	Node tmp = node.next;
				Node newNode = new Node(null,tmp,node);	    	
		    	node.next = newNode;
		    	tmp.prev = newNode;
		    	newNode.element = (String) newElement;
		    	System.out.println("the new line:\n"+newNode.element+"\nhas been added after current node");
		    }else{
		    	Node newNode = new Node(null,null,node);	    	
		    	node.next = newNode;
		    	newNode.element = (String) newElement;
		    	System.out.println("the new line:\n"+newNode.element+"\nhas been added after current node");
		    	tail = tail.next;
		    }
	    }
	    /**
	     * This method adds a new node before the given node (parameter node) in the DoubleLinkedList 
	     * @param node
	     * @param newElement is new node's element
	     */
	    public void addBeforeCurrentNode(Node node,String newElement){
	    	if(size == 0){
	    		System.out.println("The list is empty...\n");
	    		return ;
	    	}
	    	if(node.prev!=null){
		    	Node tmp = node.prev;
				Node newNode = new Node(null,node,tmp);	    	
		    	node.prev = newNode;
		    	tmp.next = newNode;
		    	newNode.element = (String) newElement;
		    	System.out.println("the new line:\n"+newNode.element+"\nhas been added before current node");
		    }else{
		    	Node newNode = new Node(null,node,null);	    	
		    	node.prev = newNode;
		    	newNode.element = (String) newElement;
		    	System.out.println("the new line:\n"+newNode.element+"\nhas been added before current node");		    
		    	head = head.prev;
		    }
	    }
	    /**
	     * This method passes DoubleLinkedList's elements' words and their indexes to an array list of Information (parameter AL)
	     * @param AL
	     * @version https://stackoverflow.com/questions/11726023/split-string-into-individual-words-java (6th answer)
	     */
	    public void passToArrayList(ArrayList<Information> AL){
	    	if(head.next!=null){
	    		Node tmp = head;	    	
		    	int line = 1;
		    	String [] strArray;
		    	while(tmp!=null){
		    		strArray = tmp.element.split("[ .,?!]+");
		    		for(int i=0; i<strArray.length;i++) {
		    			if(strArray[i].codePointCount(0, strArray[i].length())>=5){
		    				AL.add(new Information(strArray[i],line)) ;
		    			}
		    		}    		
		    		line++;
		    		tmp = tmp.next;
		    	}
	    	}else if(head!=null){
	    		Node tmp = head;	    	
		    	int line = 1;
		    	String [] strArray = tmp.element.split(" ");
		    	for(int i=0; i<strArray.length;i++) {
		    		if(strArray[i].codePointCount(0, strArray[i].length())>=5){
	    				AL.add(new Information(strArray[i],line)) ;
	    			}
		    	}    		
		    	line++;
		    	tmp = tmp.next;
	    	}else{
	    		System.out.println("the list is empty...");
	    	}
	    }
	    
}
