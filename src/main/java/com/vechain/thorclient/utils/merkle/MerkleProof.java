package com.vechain.thorclient.utils.merkle;

public class MerkleProof {

    private MerkleProof parentProof;
    private IBinaryTreeNode selfNode;

    public IBinaryTreeNode getBrotherNode() {
        return selfNode.getBrother();
    }

    public IBinaryTreeNode getSelfNode() {
        return selfNode;
    }

    public void setSelfNode(IBinaryTreeNode selfNode) {
        this.selfNode = selfNode;
    }

    public MerkleProof getParentProof() {
        return parentProof;
    }

    public void setParentProof(MerkleProof parentProof) {
        this.parentProof = parentProof;
    }

}
