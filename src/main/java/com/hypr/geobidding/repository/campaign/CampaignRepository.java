package com.hypr.geobidding.repository.campaign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypr.geobidding.domain.campaign.Campaign;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.List;

@Repository
public class CampaignRepository {

    private List<Campaign> campaigns;

    private final ObjectMapper objectMapper;

    public CampaignRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void load() throws Exception {
        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("data/campaigns.json");

        this.campaigns = objectMapper.readValue(
                is,
                new TypeReference<List<Campaign>>() {}
        );
    }

    public List<Campaign> findAll() {
        return campaigns;
    }
}

