package com.vechain.thorclient.utils.merkle;

/**
 *
 */
public class MerkleProver {

    public static MerkleProof getProvementNode(MerkleLeaf leaf){
        MerkleProof proof = new MerkleProof();
        proof.setSelfNode( leaf );
        proof.setBrotherNode( leaf.getBrother() );
        extractProofNode( leaf.getParent(), proof );
        return proof;
    }

    private static void extractProofNode(IBinaryTreeNode merkleNode, MerkleProof childProof){
        MerkleProof proof = new MerkleProof();
        proof.setSelfNode( merkleNode );
        childProof.setParentProvement( proof );
        if(merkleNode.getParent() != null){
            proof.setBrotherNode( merkleNode.getParent().getBrother() );
            extractProofNode( merkleNode.getParent(), proof );
        }
    }



}
