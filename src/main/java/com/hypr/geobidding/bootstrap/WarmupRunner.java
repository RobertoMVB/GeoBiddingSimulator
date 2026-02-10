package com.hypr.geobidding.bootstrap;

import com.hypr.geobidding.dto.bid.BidRequest;
import com.hypr.geobidding.repository.campaign.CampaignRepository;
import com.hypr.geobidding.service.campaign.CampaignBidService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarmupRunner implements ApplicationRunner {
    private final CampaignRepository repository;
    private final CampaignBidService bidService;

    public WarmupRunner(CampaignRepository repository,
                        CampaignBidService bidService) {
        this.repository = repository;
        this.bidService = bidService;
    }

    @Override
    public void run(ApplicationArguments args) {
        long start = System.currentTimeMillis();
        repository.findAll().size();
        BidRequest dummy = DummyRequestFactory.create();

        try {
            List<BidRequest> dummyBidRequests = DummyRequestFactory.createListFromJson();
            for (BidRequest req : dummyBidRequests) {
                bidService.decide(req);
            }
            long end = System.currentTimeMillis();
            System.out.println("Warmup " + (end - start) + " ms");
        } catch (Exception e) {
            System.err.println("Warmup falhou, continuando app");
            e.printStackTrace();
        }

    }
}
