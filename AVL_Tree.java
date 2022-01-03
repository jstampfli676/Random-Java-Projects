public class AVL_Tree {
	
	public Node root;

	public Node insert(Node node, int key) {
		if (node == null) {
			return new Node(key);
		} else if (key<node.key) {
			node.left = insert(node.left, key);
		} else if (key>node.key) {
			node.right = insert(node.right, key);
		} else {
			return node;
		}

		node.height = 1+getMax(getHeight(node.left), getHeight(node.right));
		
		return balanceTree(node, key);
	}

	public Node search(Node node, int key) {
		if (node == null || node.key == key) {
			return node;
		} else if (key < node.key) {
			return search(node.left, key);
		} else {
			return search(node.right, key);
		}
	}

	public Node delete(Node node, int key) {
		if (node==null) {
			return null;
		}
		if (key<node.key) {
			node.left = delete(node.left, key);
		} else if (key>node.key) {
			node.right = delete(node.right, key);
		} else {
			//we are at the node to be deleted
			if (node.left == null || node.right == null) {
				Node temp = null;
				if (temp == node.left) {
					temp = node.right;
				} else {
					temp = node.left;
				}

				if (temp == null) {
					temp = node;
					node = null;
				} else {
					node = temp;
				}
			} else {
				Node temp = findPredecessor(root, node.key);
				node.key = temp.key;
				node.left = delete(node.left, temp.key);
			}
		}

		if (node == null) {
			return node;
		}

		node.height = getMax(getHeight(node.left), getHeight(node.right))+1;

		int balance = getBalance(node);
 
        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && getBalance(node.left) >= 0)
            return rightRotate(node);
 
        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0)
        {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
 
        // Right Right Case
        if (balance < -1 && getBalance(node.right) <= 0)
            return leftRotate(node);
 
        // Right Left Case
        if (balance < -1 && getBalance(node.right) > 0)
        {
            root.right = rightRotate(node.right);
            return leftRotate(node);
        }
 
        return node;
	}

	public Node findPredecessor(Node node, int key) {
		Node target = null;
		Node answer = null;
		if (node==null || node.key==key) {
			target = node;
		} else if (key>node.key) {
			if (node.right==null) {
				return null;
			} else if (node.right.key==key) {
				answer = node;
				target = node.right;
			} else {
				return findPredecessor(node.right, key);
			}
		} else {
			return findPredecessor(node.left, key);
		}
		Node betterAnswer = checkLeftSubtree(target);
		return betterAnswer==null?answer:betterAnswer;
	}

	private Node checkLeftSubtree(Node node) {
		if (node==null) {
			return null;
		}
		Node answer = node.left;
		while (answer != null && answer.right!=null) {
			answer = answer.right;
		}
		return answer;
	}

	private Node balanceTree(Node node, int key) {
		int balance = getBalance(node);

		if (balance>1 && key<node.left.key) {
			return rightRotate(node);
		} 
		if (balance>1 && key>node.left.key) {
			node.left = leftRotate(node.left);
			return rightRotate(node);
		} 
		if (balance<-1 && key>node.right.key) {
			return leftRotate(node);
		} 
		if (balance<-1 && key<node.right.key) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}
		return node;
	}

	private Node rightRotate(Node node) {
		Node child = node.left;
		Node temp = child.right;
		child.right = node;
		node.left = temp;

		node.height = 1 + getMax(getHeight(node.left), getHeight(node.right));
		child.height = 1 + getMax(getHeight(child.left), getHeight(child.right));	

		return child;
	}

	private Node leftRotate(Node node) {
		Node child = node.right;
		Node temp = child.left;
		child.left = node;
		node.right = temp;

		node.height = 1 + getMax(getHeight(node.left), getHeight(node.right));
		child.height = 1 + getMax(getHeight(child.left), getHeight(child.right));	

		return child;
	}

	private int getMax(int a, int b) {
		return a>b?a:b;
	}

	private int getBalance(Node node) {
		return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
	}

	private int getHeight(Node n) {
		return n==null?0:n.height;
	}

	public void preOrder(Node n) {
		if (n != null) {
			System.out.print(n);
			preOrder(n.left);
			preOrder(n.right);
		}
	}

	public void inOrder(Node n) {
		if (n!=null) {
			inOrder(n.left);
			System.out.print(n);
			inOrder(n.right);
		}
	}

	public void postOrder(Node n) {
		if (n!=null) {
			postOrder(n.left);
			postOrder(n.right);
			System.out.print(n);
		}
	}

	public static void main(String[] args) {
		AVL_Tree tree = new AVL_Tree();
		tree.root = tree.insert(tree.root, 30);
		tree.root = tree.insert(tree.root, 15);
		tree.root = tree.insert(tree.root, 10);
		tree.root = tree.insert(tree.root, 8);
		tree.root = tree.insert(tree.root, 6);
		tree.root = tree.insert(tree.root, 17);
		tree.root = tree.insert(tree.root, 38);
		tree.root = tree.delete(tree.root, 8);
		tree.root = tree.delete(tree.root, 6);
		tree.root = tree.delete(tree.root, 10);
		tree.inOrder(tree.root);
		System.out.println(tree.findPredecessor(tree.root, 8));
	}

	

	private class Node {
		public Node right, left;
		public int height, key;

		public Node(int key) {
			height = 1;
			this.key = key;
		}

		public String toString() {
			return "("+key+", "+height+", "+getBalance(this)+") ";
		}
	}
}