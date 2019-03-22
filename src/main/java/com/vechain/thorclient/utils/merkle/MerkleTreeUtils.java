package com.vechain.thorclient.utils.merkle;

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
            for(int index = 0; index < size; index += 2){
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


    public static MerkleTree recoverMerkleTree(MerkleProof provementNode){
        return null;
    }


//    public static MerkleProvementNode convertMerkleProvementNode(IBinaryTreeNode node){
//        MerkleProvementNode provementNode = new MerkleProvementNode();
//        provementNode.setValue( node.getValue() );
//        provementNode.setType( node.getType() );
//        return provementNode;
//    }

}
