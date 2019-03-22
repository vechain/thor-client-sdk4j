package com.vechain.thorclient.utils;

import com.vechain.thorclient.utils.merkle.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(JUnit4.class)
public class MerkleTreeUtilsTest {


    MerkleTree tree;
    List<MerkleLeaf> leaves;
    @Before
    public void setup(){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance( "SHA-256" );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            assert false;
        }

        final byte[] block1 = {(byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04};
        final byte[] block2 = {(byte)0xae, (byte)0x45, (byte)0x98, (byte)0xff};

        final byte[] block3 = {(byte)0x99, (byte)0x98, (byte)0x97, (byte)0x96};
        final byte[] block4 = {(byte)0xff, (byte)0xfe, (byte)0xfd, (byte)0xfc};
        MerkleLeaf leaf1 = new MerkleLeaf(block1);
        MerkleLeaf leaf2 = new MerkleLeaf(block2);
        MerkleLeaf leaf3 = new MerkleLeaf(block3);
        MerkleLeaf leaf4 = new MerkleLeaf(block4);

        leaves = new ArrayList<>();
        leaves.add( leaf1);
        leaves.add( leaf2);
        leaves.add( leaf3 );
        leaves.add( leaf4 );
        tree = MerkleTreeUtils.build(leaves, messageDigest  );
    }

    @Test
    public void testBuildMerkleTree(){
        IBinaryTreeNode temp = tree;
        List<byte[]> traversalList = new ArrayList<>( );
        MerkleTreeUtils.recursionPreorderTraversal( temp, traversalList );
        for (byte[] value : traversalList){
            log.info( "value: {}", BytesUtils.toHexString( value, Prefix.ZeroLowerX ) );
        }
    }


    @Test
    public void getMerkleProvementTest(){
        MerkleLeaf leaf = leaves.get( 1 );
        MerkleProof proof = MerkleProver.getProvementNode( leaf );
        int level = 0;
        MerkleProof tempProof = proof;
        while ( tempProof != null){
            log.info( "level {} - self value:{}, brother value:{}" , level, BytesUtils.toHexString(proof.getSelfNode
                            ().getValue(), Prefix.ZeroLowerX), BytesUtils.toHexString( proof.getBrotherNode()
                            .getValue(), Prefix.ZeroLowerX ));
            level = level + 1;
            tempProof = tempProof.getParentProof();
        }
    }
}
