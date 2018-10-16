/*
 * Copyright 2018 Juri Buchmueller <motionrugs@dbvis.inf.uni-konstanz.de>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dbvis.motionrugs.strategies;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Create a Point QuadTree
 * Basic idea from: Algorithms, 4th Edition by Robert Sedgewick and Kevin Wayne
 * 
 * @author Eren Cakmak, University of Konstanz <cakmak@dbvis.inf.uni-konstanz.de>
 * @author Juri Buchmüller, University of Konstanz <buchmueller@dbvis.inf.uni-konstanz.de>
 */
public class PointQuadTree< T> {

    private Node root;

    // Node data type
    private class Node {

        Point p;               // Point stores x and y 
        Node NW, NE, SE, SW;   // four subtrees
        T value;               // associated data

        /**
         * Constructor of the Node
         *
         * @param p Point
         * @param value data element
         */
        public Node(Point p, T value) {
            this.p = p;
            this.value = value;
        }
    }

    /**
     * Insert point
     *
     * @param p Point
     * @param value data element
     */
    public void insert(Point p, T value) {
        root = insert(root, p, value);
    }

    /**
     * Helper method for insert. Inserts the element and return the root element
     * Recursive call here
     *
     * @param p Point
     * @param value data element
     */
    private Node insert(Node node, Point p, T value) {
        if (node == null) {
            return new Node(p, value);
        } else if ((p.x < node.p.x) && (p.y < node.p.y)) {
            node.SW = insert(node.SW, p, value);
        } else if ((p.x < node.p.x) && !(p.y < node.p.y)) {
            node.NW = insert(node.NW, p, value);
        } else if (!(p.x < node.p.x) && (p.y < node.p.y)) {
            node.SE = insert(node.SE, p, value);
        } else if (!(p.x < node.p.x) && !(p.y < node.p.y)) {
            node.NE = insert(node.NE, p, value);
        }
        return node;
    }

    /**
     * Inorder traversal of the nodes
     *
     * @return ArrayList of the elements T in inorder order
     */
    public ArrayList inorderTraversal() {
        return inorderTraversal(root, new ArrayList<T>());
    }

    /**
     * Helper method inorder traversal - recursive call here
     *
     * @param p Point
     * @param list data element
     */
    private ArrayList inorderTraversal(Node node, ArrayList<T> list) {
        if (node.NW != null) {
            inorderTraversal(node.NW, list);
        }
        if (node.NE != null) {
            inorderTraversal(node.NE, list);
        }

        if (node != null && node.value != null) {
            list.add(node.value);
        }

        if (node.SW != null) {
            inorderTraversal(node.SW, list);
        }
        if (node.SE != null) {
            inorderTraversal(node.SE, list);
        }

        return list;
    }

    /**
     * Preorder traversal of the nodes
     *
     * @return ArrayList of the elements T in Preorder order
     */
    public ArrayList preorderTraversal() {
        return preorderTraversal(root, new ArrayList<T>());
    }

    /**
     * Helper method Preorder traversal - recursive call here
     *
     * @param p Point
     * @param list data element
     */
    private ArrayList preorderTraversal(Node node, ArrayList<T> list) {
        if (node != null && node.value != null) {
            list.add(node.value);
        }
        if (node.NW != null) {
            inorderTraversal(node.NW, list);
        }
        if (node.NE != null) {
            inorderTraversal(node.NE, list);
        }
        if (node.SW != null) {
            inorderTraversal(node.SW, list);
        }
        if (node.SE != null) {
            inorderTraversal(node.SE, list);
        }
        return list;
    }

    /**
     * Postorder traversal of the nodes
     *
     * @return ArrayList of the elements T in postorder order
     */
    public ArrayList postorderTraversal() {
        return postorderTraversal(root, new ArrayList<T>());
    }

    /**
     * Helper method postorder traversal - recursive call here
     *
     * @param p Point
     * @param list data element
     */
    private ArrayList postorderTraversal(Node node, ArrayList<T> list) {
        if (node.NW != null) {
            inorderTraversal(node.NW, list);
        }
        if (node.NE != null) {
            inorderTraversal(node.NE, list);
        }
        if (node.SW != null) {
            inorderTraversal(node.SW, list);
        }
        if (node.SE != null) {
            inorderTraversal(node.SE, list);
        }
        if (node != null && node.value != null) {
            list.add(node.value);
        }
        return list;
    }

    /**
     * BFS traversal of the nodes
     *
     * @return ArrayList of the elements T in BFS order
     */
    public ArrayList bfsTraversal() {
        //result list
        ArrayList<T> list = new ArrayList<T>();
        //BFS
        Stack stack = new Stack();
        stack.push(root);
        while (!stack.empty()) {
            Node node = (Node) stack.pop();
            if (node != null && node.value != null) {
                list.add(node.value);
            }
            if (node.NW != null) {
                stack.push(node.NW);
            }
            if (node.NE != null) {
                stack.push(node.NE);
            }
            if (node.SW != null) {
                stack.push(node.SW);
            }
            if (node.SE != null) {
                stack.push(node.SE);
            }
        }
        return list;
    }

}
