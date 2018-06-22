package com.vechain.thorclient.core.model.blockchain;

import java.util.ArrayList;

public class EventFilter {
    private Range range;
    private Options options;
    private ArrayList<TopicSet> topicSets;

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

    public void addTopicSet(String topic0, String topic1, String topic2, String topic3, String topic4){

        if(this.topicSets == null){
            this.topicSets = new ArrayList<TopicSet>();
        }

        TopicSet topicSet = new TopicSet();
        topicSet.setTopic0(topic0);
        topicSet.setTopic1(topic1);
        topicSet.setTopic1(topic2);
        topicSet.setTopic1(topic3);
        topicSet.setTopic1(topic4);
        topicSets.add(topicSet);

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
