package com.enlink.es.services.impl;

import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.models.ResourceLog;
import com.enlink.es.models.ResourcesAccessCount;
import com.enlink.es.models.UserAccessCount;
import com.enlink.es.models.UserLoginCount;
import com.enlink.es.services.ResourceLogService;
import com.enlink.es.utils.DateUtils;
import com.enlink.es.utils.SecurityUtils;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资源日志业务层实现
 *
 * @author changgq
 */
@Slf4j
@Service
public class ResourceLogServiceImpl extends GeneralAbstractServiceImpl<ResourceLog> implements ResourceLogService {

    @Value("${elasticsearch.index.resources}")
    private String resourceLogIndex;

    @Autowired
    private RestHighLevelClient elasticsearch;

    @Override
    public RestHighLevelClient getClient() throws Exception {
        return elasticsearch;
    }

    @Override
    public IndicesCreateInfo getIndicesCI() throws Exception {
        return new IndicesCreateInfo.IndicesCIBuilder(resourceLogIndex)
                .setMappings("{\n" +
                        "  \"doc\": {\n" +
                        "    \"properties\": {\n" +
                        "      \"log_level\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"session_id\": {\n" +
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
                        "      \"remote_ip\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"keyword_status\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"log_time\": {\n" +
                        "        \"type\": \"date\",\n" +
                        "        \"format\": \"yyyy-MM-dd HH:mm:ss\"\n" +
                        "      },\n" +
                        "      \"response_time\": {\n" +
                        "        \"type\": \"float\"\n" +
                        "      },\n" +
                        "      \"resource_name\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"uri\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"upward_flow\": {\n" +
                        "        \"type\": \"long\"\n" +
                        "      },\n" +
                        "      \"downward_flow\": {\n" +
                        "        \"type\": \"long\"\n" +
                        "      },\n" +
                        "      \"total_flow\": {\n" +
                        "        \"type\": \"long\"\n" +
                        "      },\n" +
                        "      \"user_agent_string\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"request_referer\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"request_count\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"http_protocol\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"content_type\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"app_type\": {\n" +
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
    public List<ResourcesAccessCount> findResourceAccessCount(String type, Date day) throws Exception {
//        return docCountByTimestamp(type, "doc['resource_name'].value");
        CyclePojo cycle = getCycle(type, day);
        CyclePojo cyclePojo = docCountByTimestamp(cycle, "doc['resource_name'].value");
        List<ResourcesAccessCount> raCounts = new ArrayList<>();
        if (null != cyclePojo && null != cyclePojo.getDatas()) {
            for (String k : cyclePojo.getDatas().keySet()) {
                if (Strings.isNotBlank(k)) {
                    ResourcesAccessCount racount = new ResourcesAccessCount();
                    racount.setId(SecurityUtils.md5(cyclePojo.getName() + cyclePojo.getValue() + k));
                    racount.setCount_type(cyclePojo.getName());
                    racount.setCycle(cyclePojo.getValue());
                    String[] keys = k.split("\\|");
                    racount.setResource_name(k);
                    // 获取配置的应用库名称
                    racount.setDomain_name("");
                    racount.setAccess_count((long) cyclePojo.getDatas().get(k));
                    racount.setCreate_at(DateUtils.datetime2string(new Date()));
                    raCounts.add(racount);
                }
            }
        }
        return raCounts;
    }

    @Override
    public List<UserAccessCount> findUserAccessCount(String type, Date day) throws Exception {
        CyclePojo cycle = getCycle(type, day);
        CyclePojo cyclePojo = docCountByTimestamp(cycle, "doc['full_name'].value + '|' + doc['user_name'].value + '|' + doc['user_group'].value");
        List<UserAccessCount> uaCounts = new ArrayList<>();
        if (null != cyclePojo && null != cyclePojo.getDatas()) {
            for (String k : cyclePojo.getDatas().keySet()) {
                if (Strings.isNotBlank(k) && !k.replaceAll("\\|", "").equals("")) {
                    UserAccessCount uacount = new UserAccessCount();
                    uacount.setId(SecurityUtils.md5(cyclePojo.getName() + cyclePojo.getValue() + k));
                    uacount.setCount_type(cyclePojo.getName());
                    uacount.setCycle(cyclePojo.getValue());
                    String[] keys = k.split("\\|");
                    uacount.setFull_name(keys[0]);
                    uacount.setUsername(keys[1]);
                    uacount.setUser_group(keys[2]);
                    uacount.setAccess_count((long) cyclePojo.getDatas().get(k));
                    uacount.setCreate_at(DateUtils.datetime2string(new Date()));
                    uaCounts.add(uacount);
                }
            }
        }
        return uaCounts;
    }
}
