package com.hypr.geobidding.repository.campaign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypr.geobidding.domain.campaign.Campaign;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Repository
@Lazy(false)
public class CampaignRepository {

    private List<Campaign> campaigns;

    private final ObjectMapper objectMapper;

    public CampaignRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void load() throws IOException {
        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("campaigns.json");

        if (is == null) {
            throw new IllegalStateException("campaigns.json não encontrado no classpath");
        }

        Campaign[] loaded = objectMapper.readValue(is, Campaign[].class);
        this.campaigns = List.of(loaded);
        System.out.println("✅ Campanhas carregadas: " + this.campaigns.size());
    }

    public List<Campaign> findAll() {
        return campaigns == null ? List.of() : campaigns;
    }
}

