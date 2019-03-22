package com.vechain.thorclient.utils.merkle;

import java.security.MessageDigest;

public class MerkleTree extends TreeNode {

    //message digest
    private MessageDigest md;
    //children trees
    private IBinaryTreeNode leftChild;
    private IBinaryTreeNode rightChild;

    private IBinaryTreeNode parent;


    //hash value of this node
    private byte[] value;
    private NodeType type = NodeType.single;


    public IBinaryTreeNode getLeftChild() {
        return leftChild;
    }

    public IBinaryTreeNode getRightChild() {
        return rightChild;
    }

    @Override
    public void setParent(IBinaryTreeNode parent) {
        this.parent =  parent;
    }

    public MerkleTree(MessageDigest messageDigest){
        this.md = messageDigest;
    }

    /**
     * Adds two child to this merkle Tree.
     * @param leftChild
     * @param rightChild
     */
    public void addChild(final IBinaryTreeNode leftChild, final IBinaryTreeNode rightChild){
        if(leftChild == null){
            throw new IllegalArgumentException( "left child should not be null." );
        }
        this.leftChild = leftChild;
        this.rightChild = rightChild;

        this.leftChild.setParent( this );
        this.leftChild.setType( NodeType.left );

        md.update( leftChild.getValue() );
        if (this.rightChild != null){
            this.rightChild.setParent( this );
            this.rightChild.setType( NodeType.right );
            this.value = md.digest( rightChild.getValue() );
        }else {
            this.value = md.digest();
        }
    }


    @Override
    public IBinaryTreeNode getParent() {
        return this.parent;
    }


    @Override
    public byte[] getValue() {
        return this.value;
    }

    @Override
    public NodeType getType() {
        return this.type;
    }

    @Override
    public void setType(NodeType type) {
        this.type = type;
    }

}
