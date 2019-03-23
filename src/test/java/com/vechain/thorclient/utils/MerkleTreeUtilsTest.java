package com.vechain.thorclient.utils;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.utils.merkle.*;
import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@RunWith(JUnit4.class)
public class MerkleTreeUtilsTest {


    MerkleTree tree;
    List<MerkleLeaf> leaves;
    MessageDigest messageDigest = null;
    @Before
    public void setup(){

        try {
            messageDigest = MessageDigest.getInstance( "SHA-256" );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            assert false;
        }
        leaves = new ArrayList<>();
        for(int i = 0; i < 12; i++){
            byte[] randomBytes = new byte[]{(byte)i};
            MerkleLeaf leaf = new MerkleLeaf( randomBytes );
            leaves.add( leaf );
        }
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
    public void getMerkleProofTest(){
        MerkleLeaf leaf = leaves.get( 1 );
        MerkleProof proof = MerkleProver.getProvementNode( leaf );
        int level = 0;
        MerkleProof tempProof = proof;
        while ( tempProof != null){
            log.info( "level {} - self value:{}", level, BytesUtils.toHexString(tempProof.getSelfNode
                            ().getValue(), Prefix.ZeroLowerX));
            if (tempProof.getBrotherNode() != null){
                log.info( "brother node value {}", BytesUtils.toHexString( tempProof.getBrotherNode().getValue(), Prefix.ZeroLowerX
                ) );
            }

            level = level + 1;
            tempProof = tempProof.getParentProof();
        }
    }

    @Test
    public void getMerkleProofValueTest(){
        MerkleLeaf leaf = leaves.get( 2 );
        List<ProofValue> values = MerkleTreeUtils.getMerkleProof( leaf );
        String string = JSON.toJSONString( values );
        System.out.println( "Proof value: "+ string );

    }

    @Test
    public void recoverMerkleRootTest(){
//        Random random = new Random();
//        int index = random.nextInt( leaves.size() );
//        ArrayList<byte[]> arrayList = new ArrayList<>();
        MerkleLeaf leaf = leaves.get(11);
        List<byte[]> nodeValues = new ArrayList<>();
        MerkleTreeUtils.recursionPreorderTraversal( tree, nodeValues );
        for(byte[] value : nodeValues){
            log.info( "Merkle Tree node value: {}", BytesUtils.toHexString( value, Prefix.ZeroLowerX ) );
        }
        List<ProofValue> values = MerkleTreeUtils.getMerkleProof( leaf );
        System.out.println( "proof:" + JSON.toJSONString( values ) );
        byte[] recoverRoot = MerkleTreeUtils.recoverMerkleRoot(leaf.getValue(), values,
                messageDigest );

        log.info( "recover root:{}", BytesUtils.toHexString( recoverRoot, Prefix.ZeroLowerX )  );
        log.info( "merkle root: {}", BytesUtils.toHexString( tree.getValue(), Prefix.ZeroLowerX ) );
        Assert.assertEquals(BytesUtils.toHexString( tree.getValue(), Prefix.ZeroLowerX ) , BytesUtils.toHexString( recoverRoot, Prefix.ZeroLowerX ));
    }

    @Test
    public void benchmarkMerkleTree(){
        List<MerkleLeaf> list = new ArrayList<>();

        for(int i = 0; i < 2333; i++){
            byte[] randomBytes = CryptoUtils.randomBytes( 32 );
            MerkleLeaf leaf = new MerkleLeaf( randomBytes );
            list.add( leaf );
        }
        log.info( "leaves size: {}", list.size() );
        long startTime = System.currentTimeMillis();
        log.info( "Start:{}", startTime );
        MerkleTree currentTree = MerkleTreeUtils.build(list, messageDigest);
        long delta = System.currentTimeMillis() - startTime;
        log.info("elapsed time: {}", delta);
        ArrayList<byte[]> recursion = new ArrayList();
        MerkleTreeUtils.recursionPreorderTraversal(currentTree, recursion );
//        for(byte[] value : recursion){
//            log.info( "value:" + BytesUtils.toHexString( value, Prefix.ZeroLowerX ) );
//        }
        log.info( "Size is: {}", recursion.size() );
    }

}
