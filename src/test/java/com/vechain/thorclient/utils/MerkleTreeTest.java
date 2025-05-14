package com.vechain.thorclient.utils;

import com.vechain.thorclient.utils.merkle.IBinaryTreeNode;
import com.vechain.thorclient.utils.merkle.MerkleLeaf;
import com.vechain.thorclient.utils.merkle.MerkleTree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RunWith(JUnit4.class)
public class MerkleTreeTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testMerkleTree() {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            assert false;
        }

        final byte[] block1 = {(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04};
        final byte[] block2 = {(byte) 0xae, (byte) 0x45, (byte) 0x98, (byte) 0xff};

//        final byte[] block3 = {(byte)0x99, (byte)0x98, (byte)0x97, (byte)0x96};
//        final byte[] block4 = {(byte)0xff, (byte)0xfe, (byte)0xfd, (byte)0xfc};
        MerkleLeaf leaf1 = new MerkleLeaf(block1);
        MerkleLeaf leaf2 = new MerkleLeaf(block2);

        MerkleTree tree = new MerkleTree(messageDigest);
        tree.addChild(leaf1, leaf2);

        logger.info("value:" + BytesUtils.toHexString(tree.getValue(), Prefix.ZeroLowerX));
        IBinaryTreeNode tempNode = leaf1;
        while (tempNode != null) {
            logger.info("current value: " + BytesUtils.toHexString(tempNode.getValue(), Prefix.ZeroLowerX)
                    + " type: " + tempNode.getType().getValue()
            );
            IBinaryTreeNode brotherNode = tempNode.getBrother();
            if (brotherNode != null) {
                logger.info("brother value: " + BytesUtils.toHexString(brotherNode.getValue(), Prefix.ZeroLowerX)
                        + " type: " + brotherNode.getType().getValue()
                );
            }

            tempNode = tempNode.getParent();
        }

    }

}
