package com.vechain.thorclient.utils;

import com.vechain.thorclient.utils.merkle.IBinaryTreeNode;
import com.vechain.thorclient.utils.merkle.MerkleLeaf;
import com.vechain.thorclient.utils.merkle.MerkleTree;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RunWith(JUnit4.class)
@Slf4j
public class MerkleTreeTest {

    @Test
    public void testMerkleTree(){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance( "SHA-256" );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            assert false;
        }

        final byte[] block1 = {(byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04};
        final byte[] block2 = {(byte)0xae, (byte)0x45, (byte)0x98, (byte)0xff};

//        final byte[] block3 = {(byte)0x99, (byte)0x98, (byte)0x97, (byte)0x96};
//        final byte[] block4 = {(byte)0xff, (byte)0xfe, (byte)0xfd, (byte)0xfc};
        MerkleLeaf leaf1 = new MerkleLeaf(block1);
        MerkleLeaf leaf2 = new MerkleLeaf(block2 );

        MerkleTree tree = new MerkleTree( messageDigest );
        tree.addChild( leaf1, leaf2 );

        log.info( "value:" + BytesUtils.toHexString(tree.getValue(), Prefix.ZeroLowerX));
        IBinaryTreeNode tempNode = leaf1;
        while (tempNode != null){
            log.info( "current value: {}, type:{}", BytesUtils.toHexString(tempNode.getValue(), Prefix.ZeroLowerX),
                    tempNode.getType().getValue() );
            IBinaryTreeNode brotherNode = tempNode.getBrother();
            if(brotherNode != null){
                log.info( "brother value: {}, type:{}", BytesUtils.toHexString( brotherNode.getValue(), Prefix.ZeroLowerX),
                        brotherNode.getType().getValue());
            }

            tempNode = tempNode.getParent();
        }

    }

}
