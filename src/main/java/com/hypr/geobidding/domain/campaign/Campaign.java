package com.hypr.geobidding.domain.campaign;

import java.math.BigDecimal;
import java.util.List;

public class Campaign {

    private String campaignId;
    private String name;
    private BigDecimal budgetRemaining;
    private BigDecimal bidPrice;
    private BigDecimal dailyBudget;
    private CampaignTargeting targeting;
    private List<CampaignExclusion> exclusions;
    private List<String> adFormats;
    private boolean active;

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBudgetRemaining() {
        return budgetRemaining;
    }

    public void setBudgetRemaining(BigDecimal budgetRemaining) {
        this.budgetRemaining = budgetRemaining;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getDailyBudget() {
        return dailyBudget;
    }

    public void setDailyBudget(BigDecimal dailyBudget) {
        this.dailyBudget = dailyBudget;
    }

    public CampaignTargeting getTargeting() {
        return targeting;
    }

    public void setTargeting(CampaignTargeting targeting) {
        this.targeting = targeting;
    }

    public List<CampaignExclusion> getExclusions() {
        return exclusions;
    }

    public void setExclusions(List<CampaignExclusion> exclusions) {
        this.exclusions = exclusions;
    }

    public List<String> getAdFormats() {
        return adFormats;
    }

    public void setAdFormats(List<String> adFormats) {
        this.adFormats = adFormats;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

