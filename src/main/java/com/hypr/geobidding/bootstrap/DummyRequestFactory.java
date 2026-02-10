package com.hypr.geobidding.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypr.geobidding.domain.campaign.CampaignAdFormat;
import com.hypr.geobidding.dto.bid.BidRequest;
import com.hypr.geobidding.dto.bid.BidRequestInventory;
import com.hypr.geobidding.dto.bid.BidRequestUser;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

public final class DummyRequestFactory {

    private DummyRequestFactory() {}
    private static final ObjectMapper mapper = new ObjectMapper();


    public static BidRequest create() {
        BidRequest req = new BidRequest();
        BidRequestUser reqUser = new BidRequestUser();
        reqUser.setLat(-23.5505);
        reqUser.setLon(-46.6333);
        req.setBidRequestUser(reqUser);
        BidRequestInventory reqInventory = new BidRequestInventory();
        reqInventory.setAdFormat(CampaignAdFormat.NATIVE);
        reqInventory.setPublisherId("pub_013");
        reqInventory.setFloorPrice(BigDecimal.valueOf(1.50));
        req.setBidRequestInventory(reqInventory);
        return req;
    }

    public static List<BidRequest> createListFromJson() {
        try (InputStream is = DummyRequestFactory.class
                .getClassLoader()
                .getResourceAsStream("test_requests.json")) {

            if (is == null) {
                throw new IllegalStateException("requests.json não encontrado");
            }

            return mapper.readValue(is, new TypeReference<List<BidRequest>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar dummy requests", e);
        }
    }
}
