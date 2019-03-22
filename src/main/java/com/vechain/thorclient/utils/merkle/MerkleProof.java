package com.vechain.thorclient.utils.merkle;

public class MerkleProof {
    private IBinaryTreeNode brotherNode;
    private MerkleProof parentProvement;
    private IBinaryTreeNode selfNode;

    public IBinaryTreeNode getBrotherNode() {
        return brotherNode;
    }

    public void setBrotherNode(IBinaryTreeNode brotherNode) {
        this.brotherNode = brotherNode;
    }


    public IBinaryTreeNode getSelfNode() {
        return selfNode;
    }

    public void setSelfNode(IBinaryTreeNode selfNode) {
        this.selfNode = selfNode;
    }

    public MerkleProof getParentProof() {
        return parentProvement;
    }

    public void setParentProvement(MerkleProof parentProvement) {
        this.parentProvement = parentProvement;
    }

}
