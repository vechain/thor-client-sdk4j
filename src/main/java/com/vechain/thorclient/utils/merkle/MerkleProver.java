package com.vechain.thorclient.utils.merkle;

/**
 *
 */
public class MerkleProver {

    public static MerkleProof getProvementNode(MerkleLeaf leaf){
        MerkleProof proof = new MerkleProof();
        proof.setSelfNode( leaf );
        extractProofNode( leaf.getParent(), proof );
        return proof;
    }

    private static void extractProofNode(IBinaryTreeNode merkleNode, MerkleProof childProof){
        MerkleProof proof = new MerkleProof();
        proof.setSelfNode( merkleNode );
        childProof.setParentProof( proof );
        if(merkleNode.getParent() != null){
            extractProofNode( merkleNode.getParent(), proof );
        }
    }

}
