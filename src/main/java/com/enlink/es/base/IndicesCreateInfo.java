package com.enlink.es.base;

import lombok.Data;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.support.ActiveShardCount;

import java.util.List;

@Data
public class IndicesCreateInfo {
    private final String name;
    private String type;
    private int number_of_shards;
    private int number_of_replicas;
    private String settings;
    private String mappings;
    private String source;
    private List<Alias> aliases;
    private String timeout;
    private String masterNodeTimeout;
    private ActiveShardCount waitForActiveShards;
    private boolean isAsync;

    private IndicesCreateInfo(IndicesCIBuilder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.number_of_replicas = builder.number_of_replicas;
        this.number_of_shards = builder.number_of_shards;
        this.settings = builder.settings;
        this.mappings = builder.mappings;
        this.source = builder.source;
        this.aliases = builder.aliases;
        this.timeout = builder.timeout;
        this.masterNodeTimeout = builder.masterNodeTimeout;
        this.waitForActiveShards = builder.waitForActiveShards;
        this.isAsync = builder.isAsync;
    }

    public static class IndicesCIBuilder {
        private final String name;
        private String type = "doc";
        private int number_of_shards = 1;
        private int number_of_replicas = 1;
        private String settings;
        private String mappings;
        private String source;
        private List<Alias> aliases;
        private String timeout = "2m";
        private String masterNodeTimeout = "2m";
        private ActiveShardCount waitForActiveShards = ActiveShardCount.DEFAULT;
        private boolean isAsync = false;

        public IndicesCIBuilder(String name) {
            this.name = name;
        }

        public IndicesCIBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public IndicesCIBuilder setNumberOfShards(int numberOfShards) {
            this.number_of_shards = numberOfShards;
            return this;
        }

        public IndicesCIBuilder setNumberOfReplicas(int numberOfReplicas) {
            this.number_of_replicas = numberOfReplicas;
            return this;
        }

        public IndicesCIBuilder setSettings(String settings) {
            this.settings = settings;
            return this;
        }

        public IndicesCIBuilder setMappings(String mappings) {
            this.mappings = mappings;
            return this;
        }

        public IndicesCIBuilder setSource(String source) {
            this.source = source;
            return this;
        }

        public IndicesCIBuilder setAliases(List<Alias> aliases) {
            this.aliases = aliases;
            return this;
        }

        public IndicesCIBuilder setTimeout(String timeout) {
            this.timeout = timeout;
            return this;
        }

        public IndicesCIBuilder setMasterNodeTimeout(String masterNodeTimeout) {
            this.masterNodeTimeout = masterNodeTimeout;
            return this;
        }

        public IndicesCIBuilder setWaitForActiveShards(ActiveShardCount waitForActiveShards) {
            this.waitForActiveShards = waitForActiveShards;
            return this;
        }

        public IndicesCIBuilder setIsAsync() {
            this.isAsync = true;
            return this;
        }

        public IndicesCreateInfo create() {
            return new IndicesCreateInfo(this);
        }
    }
}
