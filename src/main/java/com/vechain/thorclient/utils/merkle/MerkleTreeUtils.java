package com.vechain.thorclient.utils.merkle;

import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MerkleTreeUtils {

    /**
     * Build a merkle tree
     * @param leaves the {@link List< MerkleLeaf >}
     * @return
     */
    public static MerkleTree build(List<MerkleLeaf> leaves, MessageDigest digest){
        if (leaves == null || leaves.size() == 0 || digest == null){
            return null;
        }
        List<MerkleTree> childTrees = new ArrayList<>();
        int size = leaves.size();
        for(int index = 0; index < size; index ++){
            MerkleLeaf leftLeaf =  leaves.get( index );
            index += 1;
            MerkleLeaf rightLeaf = null;
            if(index < size){
                rightLeaf = leaves.get( index );
            }

            MerkleTree merkleTree = new MerkleTree(digest);
            merkleTree.addChild( leftLeaf ,rightLeaf);
            childTrees.add( merkleTree );
        }
        return buildFromChildTree( childTrees, digest );
    }

    public static MerkleTree buildFromChildTree(List<MerkleTree> childTrees, MessageDigest digest){
        if(childTrees.size() == 1){
            return childTrees.get( 0 );
        }else{
            List<MerkleTree> trees = new ArrayList<>( );
            int size = childTrees.size();
            for(int index = 0; index < size; index ++){
                MerkleTree  leftChild = childTrees.get( index );
                index ++;
                MerkleTree rightChild = null;
                if(index < size){
                    rightChild = childTrees.get( index );
                }
                MerkleTree parentTree = new MerkleTree( digest );
                parentTree.addChild( leftChild, rightChild );
                trees.add( parentTree );
            }
            return  buildFromChildTree( trees, digest );
        }
    }

    /**
     * Preorder to view the merkle tree node.
     * @param root
     * @param list
     */
    public static void recursionPreorderTraversal(IBinaryTreeNode root, List<byte[]> list) {
        if (root != null) {
            list.add( root.getValue());
            recursionPreorderTraversal(root.getLeftChild(), list);
            recursionPreorderTraversal(root.getRightChild(), list);
        }
    }


    /**
     *
     * @param hashedMessage
     * @param proofValues
     * @param digest
     * @return
     */
    public static byte[] recoverMerkleRoot(byte[] hashedMessage, List<ProofValue> proofValues, MessageDigest digest){
        int level = 0;
        byte[] resultMessage = hashedMessage;
        for(ProofValue proofValue: proofValues){
            if(level == proofValue.getLevel()){
                if(proofValue.getType() == NodeType.left.getValue()) {
                    byte[] brotherValueBytes = BytesUtils.toByteArray(proofValue.getBrotherValue());
                    if(brotherValueBytes != null){
                        digest.update( brotherValueBytes);
                    }
                    digest.update( resultMessage );
                }else if(proofValue.getType() == NodeType.right.getValue()){
                    byte[] brotherValueBytes = BytesUtils.toByteArray(proofValue.getBrotherValue());
                    digest.update( resultMessage );
                    if(brotherValueBytes != null){
                        digest.update( brotherValueBytes);
                    }
                }
                resultMessage = digest.digest();
                digest.reset();
            }
            level += 1;
        }

        return resultMessage;
    }


    /**
     * Get merkle proof by leaf.
     * @param leaf
     * @return
     */
    public static ArrayList<ProofValue> getMerkleProof( MerkleLeaf leaf){
        MerkleProof proof = MerkleProver.getProvementNode( leaf );
        int level = 0;
        MerkleProof tempProof = proof;
        ArrayList<ProofValue> proofValues = new ArrayList<>();
        while ( tempProof != null){
            ProofValue value = new ProofValue();

            value.setLevel( level );
            if (tempProof.getBrotherNode() != null){
                value.setBrotherValue( BytesUtils.toHexString(tempProof.getBrotherNode().getValue(), Prefix.ZeroLowerX) );
                value.setType( tempProof.getBrotherNode().getType().getValue() );
                proofValues.add( value );
            }
            level = level + 1;
            tempProof = tempProof.getParentProof();

        }
        return proofValues;
    }


}
