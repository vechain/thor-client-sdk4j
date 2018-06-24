package com.vechain.thorclient.core.model.blockchain;

import com.vechain.thorclient.utils.BlockchainUtils;

import java.util.ArrayList;

public class TransferFilter {

    private Range range;
    private Options options;
    private ArrayList<AddressSet> addressSets;

    /**
     * Create TransferFilter
     * @param range {@link Range} range from and to
     * @param options {@link Options} offset and limit.
     * @return {@link TransferFilter} filter.
     */
    public static TransferFilter createFilter(Range range, Options options){
        if(range == null){
            throw new IllegalArgumentException( "Invalid range" );
        }
        if( options == null){
            throw new IllegalArgumentException( "Invalid options" );
        }
        TransferFilter transferFilter = new TransferFilter();
        transferFilter.range = range;
        transferFilter.options = options;
        return transferFilter;
    }

    private TransferFilter(){
        this.addressSets = new ArrayList<AddressSet>();

    }

    public void addAddressSet(String txOrigin, String sender, String recipient){
        if(txOrigin != null && !BlockchainUtils.isAddress( txOrigin )){
            throw new IllegalArgumentException( "Invalid txOrigin address" );
        }
        if(sender != null && !BlockchainUtils.isAddress( sender )){
            throw new IllegalArgumentException( "Invalid sender address" );
        }
        if(recipient != null && BlockchainUtils.isAddress( recipient )){
            throw new IllegalArgumentException( "Invalid sender address" );
        }

        AddressSet addressSet = new AddressSet();
        addressSet.setTxOrigin(txOrigin);
        addressSet.setSender(sender);
        addressSet.setRecipient(recipient);
        addressSets.add(addressSet);

    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

}
