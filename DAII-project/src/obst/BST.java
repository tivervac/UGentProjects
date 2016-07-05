/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obst;

//////////////////////////////////////
// Do not alter this file in any way!
//////////////////////////////////////

/**
 * Inteface BST contains methods that need to be implemented by a binary 
 * search tree.
 * This interface has to be implemented by all binary search tree classes. 
 * These classes should be named either Obst1, ..., ObstN for binary search trees
 * that require the method optimize() to organize the tree and Sbst1, ..., SbstN
 * for self-organizing binary search trees.
 */
public interface BST {
    
    //NOTE: implementing classes should work with a default constructor
    
    /**
     * searches the key "key" in the binary search tree.
     * @return the navigational cost: the depth of the search path + 1
     * The value should be POSITVE if  the key is present and 
     * NON-POSITIVE if the key does not occur in the tree. 
     * Thus, if the tree is empty, the return value is 0 (there is no navigational cost). 
     * If the key is located in the root, 1 will be returned (depth of root +1)
     * If the key is not found, and this can be decided in the root, -1 will be returned.
     */
    public int contains(int key);
    
    /**
     * adds given integer "key" to the search tree.
     * @return the navigational cost: the depth of the search path + 1, similar
     * to the value returned by contains(key).
     * If the key was not yet present in the tree, the value
     * should be NON-NEGATIVE. If the key was already present in the tree, the 
     * value should be NEGATIVE. For instance, if value was already located in
     * the root of the tree, the method returns -1.
     * If the tree is empty, the return value is 0 (there is no navigational cost). 
     * If the key is not found and can be added to the root, 1 will be returned.
     */
    public int add(int key);
    
    
    /**
     * returns the size of the current tree (number of vertices/keys)
     */    
    public int size();
    
    /**
     * reorganizes the tree so it becomes perfectly balanced.
     */    
    public void balance();
    
    /**
     * Returns the current cost of the tree.
     * @return The cost of the tree is the sum of the costs of the keys, which is
     * equal to its weight times its depth+1.
     */
    public int cost();
    
    /**
     * Reorganizes the tree so it has minmal cost for the current weights of the 
     * keys. The algorithm implementing this function should use DYNAMIC
     * PROGRAMMING. Classes that implement self-organizing binary search trees
     * do not have to implement this function.
     */
    public void optimize();
    
}
