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
            throw new IllegalArgumentException( "build MerkleTree argument is null" );
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

    /**
     * 
     * @param childTrees
     * @param digest
     * @return
     */
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
     * @param list an output parameter list.
     */
    public static void recursionPreorderTraversal(IBinaryTreeNode root, List<byte[]> list) {
        if (root != null && list != null) {
            list.add( root.getValue());
            recursionPreorderTraversal(root.getLeftChild(), list);
            recursionPreorderTraversal(root.getRightChild(), list);
        }else{
            throw new IllegalArgumentException( "root or list is null." );
        }
    }


    /**
     * Recover the merkle root from the
     * @param hashedMessage
     * @param proofValues
     * @param digest
     * @return
     */
    public static byte[] recoverMerkleRoot(byte[] hashedMessage, List<ProofValue> proofValues, MessageDigest digest){
        if (hashedMessage == null
                || proofValues == null
                || proofValues.isEmpty()
                || digest == null){
            throw new IllegalArgumentException( "recover merkle hash argument is null." );
        }
        byte[] resultMessage = hashedMessage;
        ProofValue lastProofValue = proofValues.get( proofValues.size() - 1 );
        int totalLevelCount = lastProofValue.getLevel() + 1;
        int level = 0;
        for(; level < totalLevelCount; level++){
            ProofValue foundProofValue = null;
            for(int index = 0; index < proofValues.size(); index ++) {
                if (level == proofValues.get( index ).getLevel()){
                    foundProofValue = proofValues.get( index );
                    break;
                }
            }
            if(foundProofValue != null){
                if(foundProofValue.getType() == NodeType.left.getValue()) {
                    byte[] brotherValueBytes = BytesUtils.toByteArray(foundProofValue.getBrotherValue());
                    if(brotherValueBytes != null){
                        digest.update( brotherValueBytes);
                    }
                    digest.update( resultMessage );
                }else if(foundProofValue.getType() == NodeType.right.getValue()){
                    byte[] brotherValueBytes = BytesUtils.toByteArray(foundProofValue.getBrotherValue());
                    digest.update( resultMessage );
                    if(brotherValueBytes != null){
                        digest.update( brotherValueBytes);
                    }
                }
                resultMessage = digest.digest();

            }else{
                resultMessage = digest.digest( resultMessage );
            }
            digest.reset();

        }
        return resultMessage;
    }


    /**
     * Get merkle proof by leaf.
     * @param leaf
     * @return
     */
    public static ArrayList<ProofValue> getMerkleProof( MerkleLeaf leaf){
        if(leaf == null){
            throw new IllegalArgumentException( "leaf is null" );
        }
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
