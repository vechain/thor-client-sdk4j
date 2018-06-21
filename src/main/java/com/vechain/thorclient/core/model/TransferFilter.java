package com.vechain.thorclient.core.model;

import java.util.ArrayList;

public class TransferFilter {

    private Range range;
    private Options options;
    private ArrayList<AddressSet> addressSets;

    public void setRange(String unit, long from , long to){

        Range currRange = new Range();
        currRange.setUnit(unit);
        currRange.setFrom(from);
        currRange.setTo(to);
        this.range = currRange;
    }

    public void setOptions(long offset, int limit){
        Options opt = new Options();
        opt.setLimit(limit);
        opt.setOffset(offset);
        this.options = opt;
    }

    public void addAddressSet(String txOrigin, String sender, String recipient){

        if(this.addressSets == null){
            this.addressSets = new ArrayList<AddressSet>();
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
