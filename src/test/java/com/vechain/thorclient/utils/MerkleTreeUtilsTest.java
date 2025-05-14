package com.vechain.thorclient.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.utils.merkle.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class MerkleTreeUtilsTest extends BaseTest {

    final boolean prettyFormat = isPretty();

    final ObjectMapper objectMapper = new ObjectMapper();

    final ObjectWriter writer = prettyFormat ? objectMapper.writerWithDefaultPrettyPrinter() : objectMapper.writer();


    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    MerkleTree tree;
    List<MerkleLeaf> leaves;
    MessageDigest messageDigest = null;

    @Before
    public void setup() {

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            assert false;
        }
        leaves = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            byte[] randomBytes = new byte[]{(byte) i};
            MerkleLeaf leaf = new MerkleLeaf(randomBytes);
            leaves.add(leaf);
        }
        tree = MerkleTreeUtils.build(leaves, messageDigest);
    }

    @Test
    public void testBuildMerkleTree() {
        IBinaryTreeNode temp = tree;
        List<byte[]> traversalList = new ArrayList<>();
        MerkleTreeUtils.recursionPreorderTraversal(temp, traversalList);
        for (byte[] value : traversalList) {
            logger.info("value: {}", BytesUtils.toHexString(value, Prefix.ZeroLowerX));
        }
    }

    @Test
    public void getMerkleProofTest() {
        MerkleLeaf leaf = leaves.get(1);
        MerkleProof proof = MerkleProver.getProvementNode(leaf);
        int level = 0;
        MerkleProof tempProof = proof;
        while (tempProof != null) {
            logger.info("level " + level
                    + " - self value: " + BytesUtils.toHexString(tempProof.getSelfNode().getValue(), Prefix.ZeroLowerX)
            );
            if (tempProof.getBrotherNode() != null) {
                logger.info("brother node value {}",
                        BytesUtils.toHexString(tempProof.getBrotherNode().getValue(), Prefix.ZeroLowerX));
            }

            level = level + 1;
            tempProof = tempProof.getParentProof();
        }
    }

    @Test
    public void getMerkleProofValueTest() throws JsonProcessingException {
        MerkleLeaf leaf = leaves.get(2);
        List<ProofValue> values = MerkleTreeUtils.getMerkleProof(leaf);
        String string = writer.writeValueAsString(values);
        logger.info("Proof value: {}", string);

    }

    @Test
    public void recoverMerkleRootTest() throws JsonProcessingException {
//        Random random = new Random();
//        int index = random.nextInt( leaves.size() );
//        ArrayList<byte[]> arrayList = new ArrayList<>();
        MerkleLeaf leaf = leaves.get(11);
        List<byte[]> nodeValues = new ArrayList<>();
        MerkleTreeUtils.recursionPreorderTraversal(tree, nodeValues);
        for (byte[] value : nodeValues) {
            logger.info("Merkle Tree node value: {}", BytesUtils.toHexString(value, Prefix.ZeroLowerX));
        }
        List<ProofValue> values = MerkleTreeUtils.getMerkleProof(leaf);
        logger.info("proof: {}", writer.writeValueAsString(values));
        byte[] recoverRoot = MerkleTreeUtils.recoverMerkleRoot(leaf.getValue(), values, messageDigest);

        logger.info("recover root:{}", BytesUtils.toHexString(recoverRoot, Prefix.ZeroLowerX));
        logger.info("merkle root: {}", BytesUtils.toHexString(tree.getValue(), Prefix.ZeroLowerX));
        Assert.assertEquals(BytesUtils.toHexString(tree.getValue(), Prefix.ZeroLowerX),
                BytesUtils.toHexString(recoverRoot, Prefix.ZeroLowerX));
    }

    @Test
    public void benchmarkMerkleTree() {
        List<MerkleLeaf> list = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            byte[] randomBytes = CryptoUtils.randomBytes(32);
            MerkleLeaf leaf = new MerkleLeaf(randomBytes);
            list.add(leaf);
        }
        logger.info("leaves size: " + list.size());
        long startTime = System.currentTimeMillis();
        logger.info("Start: " + startTime);
        MerkleTree currentTree = MerkleTreeUtils.build(list, messageDigest);
        long delta = System.currentTimeMillis() - startTime;
        logger.info("elapsed time: " + delta);
        ArrayList<byte[]> recursion = new ArrayList();
        MerkleTreeUtils.recursionPreorderTraversal(currentTree, recursion);
//        for(byte[] value : recursion){
//            logger.info( "value:" + BytesUtils.toHexString( value, Prefix.ZeroLowerX ) );
//        }
        logger.info("Size is: " + recursion.size());
    }

}
