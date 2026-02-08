#!/usr/bin/env python3
"""
REFERENCE IMPLEMENTATION - Simple bidding engine (NOT OPTIMIZED)

This is a minimal reference implementation to help you validate your logic.
It is NOT optimized for performance and will NOT meet the latency requirements.

Use this to:
1. Understand the basic bidding logic
2. Validate your geo-calculations
3. Compare results with your implementation

DO NOT submit this as your solution - it's intentionally naive.
"""

import json
import math
from typing import Dict, List, Optional, Tuple
from dataclasses import dataclass

@dataclass
class BidDecision:
    decision: str  # "bid" or "no_bid"
    campaign_id: Optional[str] = None
    bid_price: Optional[float] = None
    reason: Optional[str] = None

class GeoBiddingEngine:
    def __init__(self, campaigns_file: str):
        with open(campaigns_file, 'r') as f:
            self.campaigns = json.load(f)
    
    @staticmethod
    def haversine_distance(lat1: float, lon1: float, lat2: float, lon2: float) -> float:
        """Calculate distance between two points in km using Haversine formula"""
        R = 6371  # Earth radius in km
        
        lat1, lon1, lat2, lon2 = map(math.radians, [lat1, lon1, lat2, lon2])
        
        dlat = lat2 - lat1
        dlon = lon2 - lon1
        
        a = math.sin(dlat/2)**2 + math.cos(lat1) * math.cos(lat2) * math.sin(dlon/2)**2
        c = 2 * math.asin(math.sqrt(a))
        
        return R * c
    
    @staticmethod
    def point_in_polygon(lat: float, lon: float, polygon: List[List[float]]) -> bool:
        """Check if point is inside polygon using ray casting algorithm"""
        inside = False
        n = len(polygon)
        
        p1_lat, p1_lon = polygon[0]
        for i in range(1, n + 1):
            p2_lat, p2_lon = polygon[i % n]
            
            if lon > min(p1_lon, p2_lon):
                if lon <= max(p1_lon, p2_lon):
                    if lat <= max(p1_lat, p2_lat):
                        if p1_lon != p2_lon:
                            xinters = (lon - p1_lon) * (p2_lat - p1_lat) / (p2_lon - p1_lon) + p1_lat
                        if p1_lat == p2_lat or lat <= xinters:
                            inside = not inside
            
            p1_lat, p1_lon = p2_lat, p2_lon
        
        return inside
    
    def check_targeting(self, lat: float, lon: float, targeting: Dict) -> bool:
        """Check if user location matches targeting rules"""
        ttype = targeting['type']
        
        if ttype == 'radius':
            center = targeting['center']
            distance = self.haversine_distance(
                lat, lon,
                center['lat'], center['lon']
            )
            return distance <= targeting['radius_km']
        
        elif ttype == 'polygon':
            return self.point_in_polygon(lat, lon, targeting['coords'])
        
        elif ttype == 'multi_radius':
            for target in targeting['targets']:
                center = target['center']
                distance = self.haversine_distance(
                    lat, lon,
                    center['lat'], center['lon']
                )
                if distance <= target['radius_km']:
                    return True
            return False
        
        return False
    
    def check_exclusions(self, lat: float, lon: float, exclusions: List[Dict]) -> bool:
        """Check if user is in any exclusion zone (returns True if excluded)"""
        for exclusion in exclusions:
            if exclusion['type'] == 'radius':
                center = exclusion['center']
                distance = self.haversine_distance(
                    lat, lon,
                    center['lat'], center['lon']
                )
                if distance <= exclusion['radius_km']:
                    return True
            
            elif exclusion['type'] == 'polygon':
                if self.point_in_polygon(lat, lon, exclusion['coords']):
                    return True
        
        return False
    
    def evaluate_bid(self, request: Dict) -> BidDecision:
        """Evaluate a bid request and return decision"""
        user_lat = request['user']['lat']
        user_lon = request['user']['lon']
        inventory = request['inventory']
        
        eligible_campaigns = []
        
        # Find eligible campaigns
        for campaign in self.campaigns:
            # Check if campaign is active
            if not campaign.get('active', True):
                continue
            
            # Check if campaign has budget
            if campaign.get('budget_remaining', 0) <= 0:
                continue
            
            # Check ad format match
            if inventory['ad_format'] not in campaign.get('ad_formats', []):
                continue
            
            # Check bid price vs floor price
            if campaign['bid_price'] < inventory['floor_price']:
                continue
            
            # Check geo-targeting
            if not self.check_targeting(user_lat, user_lon, campaign['targeting']):
                continue
            
            # Check exclusions
            if self.check_exclusions(user_lat, user_lon, campaign.get('exclusions', [])):
                continue
            
            eligible_campaigns.append(campaign)
        
        # Select campaign with highest bid
        if not eligible_campaigns:
            return BidDecision(
                decision="no_bid",
                reason="no_matching_campaign"
            )
        
        winning_campaign = max(eligible_campaigns, key=lambda c: c['bid_price'])
        
        return BidDecision(
            decision="bid",
            campaign_id=winning_campaign['campaign_id'],
            bid_price=winning_campaign['bid_price']
        )

# Example usage
if __name__ == "__main__":
    import time
    
    # Load engine
    engine = GeoBiddingEngine("data/campaigns.json")
    
    # Load test requests
    with open("data/test_requests.json", "r") as f:
        test_requests = json.load(f)
    
    print("Running reference implementation on sample requests...")
    print("(This is NOT optimized - expect slow performance)\n")
    
    # Test first 100 requests
    latencies = []
    bid_count = 0
    no_bid_count = 0
    
    for i, request in enumerate(test_requests[:100]):
        start = time.time()
        decision = engine.evaluate_bid(request)
        latency_ms = (time.time() - start) * 1000
        latencies.append(latency_ms)
        
        if decision.decision == "bid":
            bid_count += 1
        else:
            no_bid_count += 1
        
        if i < 5:  # Show first 5
            print(f"Request {i+1}: {decision.decision}", end="")
            if decision.decision == "bid":
                print(f" - Campaign: {decision.campaign_id}, Price: R$ {decision.bid_price}")
            else:
                print(f" - Reason: {decision.reason}")
    
    print(f"\n{'='*60}")
    print(f"Results from {len(latencies)} requests:")
    print(f"  • Bids: {bid_count}")
    print(f"  • No-bids: {no_bid_count}")
    print(f"  • Avg latency: {sum(latencies)/len(latencies):.2f}ms")
    print(f"  • p99 latency: {sorted(latencies)[int(len(latencies)*0.99)]:.2f}ms")
    print(f"{'='*60}")
    print("\nNote: This naive implementation is too slow for production.")
    print("Your optimized version should achieve <50ms p99 @ 1k req/s.")
