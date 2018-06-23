package com.vechain.thorclient.core.model.blockchain;

import com.vechain.thorclient.utils.BlockchainUtils;

import java.util.ArrayList;

public class EventFilter {
    private Range range;
    private Options options;
    private ArrayList<TopicSet> topicSets;

    public static EventFilter createFilter(Range range, Options options){
        EventFilter eventFilter = new EventFilter();
        eventFilter.range = range;
        eventFilter.options = options;
        return eventFilter;
    }

    private EventFilter(){
        this.topicSets = new ArrayList<>();
    }

    public void addTopicSet(String topic0, String topic1, String topic2, String topic3, String topic4){
        if(topic0 != null && !BlockchainUtils.isId( topic0 )){
            throw new IllegalArgumentException( "Invalid topic0" );
        }
        if(topic1 != null && !BlockchainUtils.isId( topic1 )){
            throw new IllegalArgumentException( "Invalid topic1" );
        }
        if(topic2 != null && !BlockchainUtils.isId( topic2 )){
            throw new IllegalArgumentException( "Invalid topic2" );
        }
        if(topic3 != null && !BlockchainUtils.isId( topic3 )){
            throw new IllegalArgumentException( "Invalid topic3" );
        }
        if(topic4 != null && !BlockchainUtils.isId( topic4 )){
            throw new IllegalArgumentException( "Invalid topic4" );
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
