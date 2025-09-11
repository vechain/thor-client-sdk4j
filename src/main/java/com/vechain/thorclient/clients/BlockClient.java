package com.vechain.thorclient.clients;

import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.core.model.blockchain.Block;
import com.vechain.thorclient.core.model.clients.Revision;
import com.vechain.thorclient.core.model.exception.ClientIOException;

import java.util.HashMap;

/**
 * BlockClient provides access to VeChain blockchain block information.
 * 
 * <p>Blocks are the fundamental units of the VeChain blockchain, containing transactions 
 * and state changes. This client provides comprehensive block querying capabilities.</p>
 * 
 * <h3>Block Structure</h3>
 * <p>Each block contains:</p>
 * <ul>
 *   <li><strong>number</strong>: Block number (string)</li>
 *   <li><strong>id</strong>: Block ID (hex string)</li>
 *   <li><strong>size</strong>: Block size in bytes</li>
 *   <li><strong>parentID</strong>: Parent block ID</li>
 *   <li><strong>timestamp</strong>: Block timestamp (Unix)</li>
 *   <li><strong>gasLimit</strong>: Block gas limit</li>
 *   <li><strong>gasUsed</strong>: Gas used in block</li>
 *   <li><strong>totalScore</strong>: Cumulative difficulty</li>
 *   <li><strong>signer</strong>: Block signer address</li>
 *   <li><strong>beneficiary</strong>: Block reward recipient</li>
 *   <li><strong>isTrunk</strong>: Whether block is on main chain</li>
 *   <li><strong>transactions</strong>: Array of transaction IDs or full transactions</li>
 * </ul>
 * 
 * <h3>Usage Examples</h3>
 * <pre>{@code
 * // Get latest block
 * Block latestBlock = BlockClient.getBlock(null);
 * System.out.println("Latest block: " + latestBlock.getNumber());
 * System.out.println("Block ID: " + latestBlock.getId());
 * System.out.println("Timestamp: " + latestBlock.getTimestamp());
 * 
 * // Get specific block
 * Block block = BlockClient.getBlock(Revision.create(1000000));
 * System.out.println("Block 1000000 signer: " + block.getSigner());
 * 
 * // Get block with full transaction details
 * Block expandedBlock = BlockClient.getBlockExpanded(null);
 * System.out.println("Transactions in block: " + expandedBlock.getTransactions().size());
 * }</pre>
 * 
 * @see Block
 * @see Revision
 * @since 0.1.0
 */
public class BlockClient extends AbstractClient {

    /**
     * Retrieves block information from the VeChain blockchain.
     * 
     * <p>Returns basic block information including header data, transaction IDs, 
     * and block metadata. For full transaction details, use {@link #getBlockExpanded(Revision)}.</p>
     * 
     * @param revision the block revision to query. Use {@code null} for the latest block,
     *                 {@link Revision#BEST} for the best block, or {@link Revision#create(long)}
     *                 for a specific block number
     * @return the block information, or {@code null} if the block doesn't exist
     * @throws ClientIOException if there's a network error or the request fails
     * 
     * @see #getBlockExpanded(Revision)
     * @see Revision#BEST
     * @see Revision#create(long)
     */
    public static Block getBlock(Revision revision) throws ClientIOException {
        Revision currentRevision = revision;
        if (revision == null) {
            currentRevision = Revision.BEST;
        }
        HashMap<String, String> uriParams = parameters(new String[] { "revision" },
                new String[] { currentRevision.toString() });
        return sendGetRequest(Path.GetBlockPath, uriParams, null, Block.class);
    }

    /**
     * Retrieves expanded block information with full transaction details.
     * 
     * <p>Returns complete block information including full transaction objects rather than 
     * just transaction IDs. This provides detailed information about all transactions 
     * within the block, including clauses, gas usage, and execution results.</p>
     * 
     * <p><strong>Note:</strong> This method returns more data than {@link #getBlock(Revision)} 
     * and may take longer to execute for blocks with many transactions.</p>
     * 
     * @param revision the block revision to query. Use {@code null} for the latest block,
     *                 {@link Revision#BEST} for the best block, or {@link Revision#create(long)}
     *                 for a specific block number
     * @return the expanded block information with full transaction details, 
     *         or {@code null} if the block doesn't exist
     * @throws ClientIOException if there's a network error or the request fails
     * 
     * @see #getBlock(Revision)
     * @see Revision#BEST
     * @see Revision#create(long)
     */
    public static Block getBlockExpanded(Revision revision) throws ClientIOException {
        Revision currentRevision = revision;
        if (revision == null) {
            currentRevision = Revision.BEST;
        }
        HashMap<String, String> uriParams = parameters(new String[] { "revision" },
                new String[] { currentRevision.toString() });
        HashMap<String, String> queryParams = parameters(new String[] { "expanded" },
                new String[] { "true" });
        return sendGetRequest(Path.GetBlockPath, uriParams, queryParams, Block.class);
    }
}
