package com.enlink.es.services.impl;

import afu.org.checkerframework.checker.oigj.qual.O;
import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.models.UserLog;
import com.enlink.es.models.UserLoginCount;
import com.enlink.es.services.UserLogService;
import com.enlink.es.utils.DateUtils;
import com.enlink.es.utils.SecurityUtils;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValueType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserLogServiceImpl extends GeneralAbstractServiceImpl<UserLog> implements UserLogService {

    @Value("${elasticsearch.index.user}")
    private String userLogIndex;

    @Autowired
    private RestHighLevelClient elasticsearch;

    @Override
    public RestHighLevelClient getClient() throws Exception {
        return elasticsearch;
    }

    @Override
    public IndicesCreateInfo getIndicesCI() throws Exception {
        return new IndicesCreateInfo.IndicesCIBuilder(userLogIndex)
                .setMappings("{\n" +
                        "  \"doc\": {\n" +
                        "    \"properties\": {\n" +
                        "      \"log_level\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"log_time\": {\n" +
                        "        \"type\": \"date\",\n" +
                        "        \"format\": \"yyyy-MM-dd HH:mm:ss\"\n" +
                        "      },\n" +
                        "      \"operation_method\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"keyword_status\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"log_info\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"full_name\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"user_name\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"user_group\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"login_method\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"remote_ip\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"auth_center\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"link_interface\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"os\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"extend_fields_1\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"extend_fields_2\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"extend_fields_3\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"extend_fields_4\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"create_at\": {\n" +
                        "        \"type\": \"date\",\n" +
                        "        \"format\": \"strict_date_optional_time||yyyy-MM-dd HH:mm:ss||yyyy-MM-dd HH:mm:ss.SSS||dd/MMM/yyyy:HH:mm:ss Z||epoch_millis\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}")
                .create();
    }

    @Override
    public List<UserLoginCount> getUserLoginCount(String type, Date day) throws Exception {
        CyclePojo cycle = getCycle(type, day);
        CyclePojo cyclePojo = docCountByTimestamp(cycle, "doc['full_name'].value + '|' + doc['user_name'].value + '|' + doc['user_group'].value");
        List<UserLoginCount> ulCounts = new ArrayList<>();
        if (null != cyclePojo && null != cyclePojo.getDatas()) {
            for (String k : cyclePojo.getDatas().keySet()) {
                if (Strings.isNotBlank(k) && !k.replaceAll("\\|", "").equals("")) {
                    UserLoginCount ulcount = new UserLoginCount();
                    ulcount.setCount_type(cyclePojo.getName());
                    ulcount.setCycle(cyclePojo.getValue());
                    String[] keys = k.split("\\|");
                    ulcount.setFull_name(keys[0]);
                    ulcount.setUsername(keys[1]);
                    ulcount.setUser_group(keys[2]);
                    ulcount.setAccess_count((long) cyclePojo.getDatas().get(k));
                    ulcount.setCreate_at(DateUtils.datetime2string(new Date()));
                    ulcount.setId(SecurityUtils.md5(cyclePojo.getName() + cyclePojo.getValue() + k));
                    ulCounts.add(ulcount);
                }
            }
        }
        // 对象释放
        cycle = null;
        cyclePojo = null;
        return ulCounts;
    }
}
