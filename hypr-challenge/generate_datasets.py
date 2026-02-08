#!/usr/bin/env python3
"""
HYPR Challenge Dataset Generator

Generates:
1. campaigns.json - 100 campaigns with geo-targeting rules
2. test_requests.json - 10,000 sample bid requests for testing
3. load_test_requests.json - 100,000 requests for load testing
"""

import json
import random
import uuid
from datetime import datetime
from typing import List, Dict, Tuple
import math

# Key locations in São Paulo and surroundings
LOCATIONS = {
    "Av Paulista": (-23.5613, -46.6563),
    "Vila Olimpia": (-23.5968, -46.6863),
    "Itaim Bibi": (-23.5858, -46.6850),
    "Jardins": (-23.5680, -46.6550),
    "Pinheiros": (-23.5629, -46.6902),
    "Moema": (-23.6033, -46.6667),
    "Brooklin": (-23.6134, -46.6978),
    "Morumbi": (-23.6210, -46.7020),
    "Berrini": (-23.6167, -46.6933),
    "Shopping Iguatemi": (-23.5670, -46.6890),
    "Shopping Eldorado": (-23.5917, -46.6936),
    "Parque Ibirapuera": (-23.5875, -46.6575),
    "Centro": (-23.5505, -46.6333),
    "Vila Madalena": (-23.5598, -46.6917),
    "Faria Lima": (-23.5778, -46.6881),
    "Shopping JK": (-23.5911, -46.6798),
    "Alphaville": (-23.4953, -46.8428),
    "Santo Amaro": (-23.6528, -46.7083),
    "Tatuape": (-23.5427, -46.5769),
    "Santana": (-23.5056, -46.6289),
}

# Retail chains (for exclusion zones)
RETAIL_COMPETITORS = [
    "Carrefour Morumbi",
    "Extra Berrini", 
    "Pão de Açúcar Jardins",
]

AD_FORMATS = ["banner", "native", "video", "interstitial"]
PUBLISHERS = [f"pub_{i:03d}" for i in range(1, 51)]

def generate_polygon_around_point(center_lat: float, center_lon: float, radius_km: float, sides: int = 6) -> List[List[float]]:
    """Generate a polygon with N sides around a center point"""
    coords = []
    for i in range(sides):
        angle = (2 * math.pi * i) / sides
        # Convert km to approximate degrees (rough approximation)
        lat_offset = (radius_km / 111.0) * math.cos(angle)
        lon_offset = (radius_km / (111.0 * math.cos(math.radians(center_lat)))) * math.sin(angle)
        coords.append([
            round(center_lat + lat_offset, 6),
            round(center_lon + lon_offset, 6)
        ])
    coords.append(coords[0])  # Close the polygon
    return coords

def generate_campaigns(num_campaigns: int = 100) -> List[Dict]:
    """Generate realistic campaign configurations"""
    campaigns = []
    location_list = list(LOCATIONS.items())
    
    for i in range(num_campaigns):
        campaign_id = f"camp_{i+1:03d}"
        
        # Mix of targeting types
        targeting_type = random.choices(
            ["radius", "polygon", "multi_radius"],
            weights=[0.5, 0.3, 0.2]
        )[0]
        
        # Pick a random location as center
        location_name, (center_lat, center_lon) = random.choice(location_list)
        
        targeting = {}
        
        if targeting_type == "radius":
            targeting = {
                "type": "radius",
                "center": {"lat": center_lat, "lon": center_lon},
                "radius_km": round(random.uniform(0.5, 10.0), 2)
            }
        
        elif targeting_type == "polygon":
            radius = random.uniform(1.0, 5.0)
            sides = random.choice([5, 6, 8])
            coords = generate_polygon_around_point(center_lat, center_lon, radius, sides)
            targeting = {
                "type": "polygon",
                "coords": coords
            }
        
        elif targeting_type == "multi_radius":
            # Multiple radius targets (e.g., around multiple stores)
            num_targets = random.randint(2, 4)
            targets = []
            for _ in range(num_targets):
                loc_name, (lat, lon) = random.choice(location_list)
                targets.append({
                    "center": {"lat": lat, "lon": lon},
                    "radius_km": round(random.uniform(0.5, 3.0), 2)
                })
            targeting = {
                "type": "multi_radius",
                "targets": targets
            }
        
        # Some campaigns have exclusion zones
        exclusions = []
        if random.random() < 0.3:  # 30% have exclusions
            num_exclusions = random.randint(1, 3)
            for _ in range(num_exclusions):
                exc_loc_name, (exc_lat, exc_lon) = random.choice(location_list)
                exc_radius = random.uniform(0.2, 1.5)
                
                if random.random() < 0.5:
                    # Radius exclusion
                    exclusions.append({
                        "type": "radius",
                        "center": {"lat": exc_lat, "lon": exc_lon},
                        "radius_km": round(exc_radius, 2)
                    })
                else:
                    # Polygon exclusion
                    exc_coords = generate_polygon_around_point(exc_lat, exc_lon, exc_radius, 4)
                    exclusions.append({
                        "type": "polygon",
                        "coords": exc_coords
                    })
        
        campaign = {
            "campaign_id": campaign_id,
            "name": f"Campaign {i+1} - {location_name}",
            "budget_remaining": round(random.uniform(1000, 50000), 2),
            "bid_price": round(random.uniform(0.10, 2.50), 2),
            "daily_budget": round(random.uniform(500, 5000), 2),
            "targeting": targeting,
            "exclusions": exclusions,
            "ad_formats": random.sample(AD_FORMATS, k=random.randint(1, 3)),
            "active": random.random() < 0.85  # 85% active
        }
        
        campaigns.append(campaign)
    
    return campaigns

def generate_random_location_near(center_lat: float, center_lon: float, max_radius_km: float) -> Tuple[float, float]:
    """Generate a random location within max_radius_km of center"""
    # Random angle
    angle = random.uniform(0, 2 * math.pi)
    # Random radius (square root for uniform distribution)
    radius = math.sqrt(random.uniform(0, 1)) * max_radius_km
    
    # Convert to lat/lon offsets
    lat_offset = (radius / 111.0) * math.cos(angle)
    lon_offset = (radius / (111.0 * math.cos(math.radians(center_lat)))) * math.sin(angle)
    
    return (
        round(center_lat + lat_offset, 6),
        round(center_lon + lon_offset, 6)
    )

def generate_bid_requests(num_requests: int, campaigns: List[Dict]) -> List[Dict]:
    """Generate realistic bid requests"""
    requests = []
    
    # Weight distribution around key locations
    location_weights = [1.0] * len(LOCATIONS)
    location_list = list(LOCATIONS.values())
    
    for i in range(num_requests):
        # Pick a location center (weighted towards known locations)
        if random.random() < 0.7:  # 70% near known locations
            center_lat, center_lon = random.choices(location_list, weights=location_weights)[0]
            lat, lon = generate_random_location_near(center_lat, center_lon, 2.0)
        else:  # 30% random in greater SP area
            lat = random.uniform(-23.7, -23.4)
            lon = random.uniform(-46.8, -46.4)
            lat, lon = round(lat, 6), round(lon, 6)
        
        request = {
            "request_id": str(uuid.uuid4()),
            "timestamp": datetime.utcnow().isoformat() + "Z",
            "user": {
                "lat": lat,
                "lon": lon,
                "user_id": f"user_{random.randint(1, 100000):06d}"
            },
            "inventory": {
                "publisher_id": random.choice(PUBLISHERS),
                "ad_format": random.choice(AD_FORMATS),
                "floor_price": round(random.uniform(0.05, 1.50), 2),
                "size": random.choice(["320x50", "300x250", "728x90", "300x600"])
            },
            "device": {
                "type": random.choice(["mobile", "tablet", "desktop"]),
                "os": random.choice(["iOS", "Android", "Windows", "MacOS"])
            }
        }
        
        requests.append(request)
    
    return requests

def main():
    print("Generating HYPR Challenge datasets...")
    
    # Generate campaigns
    print("→ Generating 100 campaigns...")
    campaigns = generate_campaigns(100)
    
    with open("/home/claude/hypr-challenge/data/campaigns.json", "w") as f:
        json.dump(campaigns, f, indent=2)
    print(f"  ✓ Saved campaigns.json ({len(campaigns)} campaigns)")
    
    # Generate test requests
    print("→ Generating 10,000 test requests...")
    test_requests = generate_bid_requests(10000, campaigns)
    
    with open("/home/claude/hypr-challenge/data/test_requests.json", "w") as f:
        json.dump(test_requests, f, indent=2)
    print(f"  ✓ Saved test_requests.json ({len(test_requests)} requests)")
    
    # Generate load test requests
    print("→ Generating 100,000 load test requests...")
    load_requests = generate_bid_requests(100000, campaigns)
    
    with open("/home/claude/hypr-challenge/data/load_test_requests.json", "w") as f:
        json.dump(load_requests, f, indent=2)
    print(f"  ✓ Saved load_test_requests.json ({len(load_requests)} requests)")
    
    # Generate summary stats
    print("\n📊 Dataset Summary:")
    print(f"  • Campaigns: {len(campaigns)}")
    print(f"    - Radius targeting: {sum(1 for c in campaigns if c['targeting']['type'] == 'radius')}")
    print(f"    - Polygon targeting: {sum(1 for c in campaigns if c['targeting']['type'] == 'polygon')}")
    print(f"    - Multi-radius targeting: {sum(1 for c in campaigns if c['targeting']['type'] == 'multi_radius')}")
    print(f"    - With exclusions: {sum(1 for c in campaigns if c['exclusions'])}")
    print(f"    - Active campaigns: {sum(1 for c in campaigns if c['active'])}")
    print(f"  • Test requests: {len(test_requests)}")
    print(f"  • Load test requests: {len(load_requests)}")
    
    total_budget = sum(c['budget_remaining'] for c in campaigns)
    avg_bid = sum(c['bid_price'] for c in campaigns) / len(campaigns)
    print(f"  • Total campaign budget: R$ {total_budget:,.2f}")
    print(f"  • Average bid price: R$ {avg_bid:.2f}")
    
    print("\n✅ All datasets generated successfully!")
    print(f"📁 Files saved in: /home/claude/hypr-challenge/data/")

if __name__ == "__main__":
    main()
