package com.vechain.thorclient.utils.merkle;

public interface IBinaryTreeNode {
    IBinaryTreeNode getParent();
    IBinaryTreeNode getLeftChild();
    IBinaryTreeNode getRightChild();
    IBinaryTreeNode getBrother();

    void setParent(IBinaryTreeNode parent);
    void addChild(final IBinaryTreeNode leftChildTreeTree, final IBinaryTreeNode rightChildTree);
    byte[] getValue();
    NodeType getType();
    void setType(NodeType type);
}
