package com.vechain.thorclient.utils.merkle;

public abstract class TreeNode implements IBinaryTreeNode {

    @Override
    public IBinaryTreeNode getBrother() {
        if (this.getParent() != null){
            if (this.getType().equals( NodeType.left )){
                return this.getParent().getRightChild();
            }else{
                return this.getParent().getLeftChild();
            }
        }
        return null;
    }


}
