#!/usr/bin/env python3
"""
Visualize campaigns and test requests on a map

Requires: pip install folium
"""

import json
import folium
from folium import plugins

def load_campaigns():
    with open("data/campaigns.json", "r") as f:
        return json.load(f)

def load_test_requests(limit=500):
    with open("data/test_requests.json", "r") as f:
        data = json.load(f)
        return data[:limit]

def create_map():
    # Center on São Paulo
    m = folium.Map(
        location=[-23.5505, -46.6333],
        zoom_start=11,
        tiles='OpenStreetMap'
    )
    
    campaigns = load_campaigns()
    test_requests = load_test_requests(500)
    
    # Add campaigns
    campaign_group = folium.FeatureGroup(name='Campaigns', show=True)
    
    for campaign in campaigns:
        if not campaign.get('active'):
            continue
            
        targeting = campaign['targeting']
        
        # Draw targeting zones
        if targeting['type'] == 'radius':
            center = targeting['center']
            radius_m = targeting['radius_km'] * 1000
            
            folium.Circle(
                location=[center['lat'], center['lon']],
                radius=radius_m,
                color='blue',
                fill=True,
                fillColor='blue',
                fillOpacity=0.1,
                popup=f"{campaign['name']}<br>Bid: R$ {campaign['bid_price']}<br>Budget: R$ {campaign['budget_remaining']:.2f}"
            ).add_to(campaign_group)
        
        elif targeting['type'] == 'polygon':
            coords = [[c[0], c[1]] for c in targeting['coords']]
            
            folium.Polygon(
                locations=coords,
                color='green',
                fill=True,
                fillColor='green',
                fillOpacity=0.1,
                popup=f"{campaign['name']}<br>Bid: R$ {campaign['bid_price']}"
            ).add_to(campaign_group)
        
        elif targeting['type'] == 'multi_radius':
            for i, target in enumerate(targeting['targets']):
                center = target['center']
                radius_m = target['radius_km'] * 1000
                
                folium.Circle(
                    location=[center['lat'], center['lon']],
                    radius=radius_m,
                    color='purple',
                    fill=True,
                    fillColor='purple',
                    fillOpacity=0.1,
                    popup=f"{campaign['name']} (Target {i+1})"
                ).add_to(campaign_group)
        
        # Draw exclusion zones
        for exclusion in campaign.get('exclusions', []):
            if exclusion['type'] == 'radius':
                center = exclusion['center']
                radius_m = exclusion['radius_km'] * 1000
                
                folium.Circle(
                    location=[center['lat'], center['lon']],
                    radius=radius_m,
                    color='red',
                    fill=True,
                    fillColor='red',
                    fillOpacity=0.2,
                    popup=f"Exclusion Zone<br>{campaign['name']}"
                ).add_to(campaign_group)
            
            elif exclusion['type'] == 'polygon':
                coords = [[c[0], c[1]] for c in exclusion['coords']]
                
                folium.Polygon(
                    locations=coords,
                    color='red',
                    fill=True,
                    fillColor='red',
                    fillOpacity=0.2,
                    popup=f"Exclusion Zone<br>{campaign['name']}"
                ).add_to(campaign_group)
    
    campaign_group.add_to(m)
    
    # Add test requests as a heatmap
    request_locations = [
        [req['user']['lat'], req['user']['lon']]
        for req in test_requests
    ]
    
    plugins.HeatMap(
        request_locations,
        name='Test Requests (Heatmap)',
        min_opacity=0.3,
        radius=15,
        blur=20,
        show=True
    ).add_to(m)
    
    # Add layer control
    folium.LayerControl().add_to(m)
    
    # Save map
    m.save('campaign_map.html')
    print("✓ Map saved to campaign_map.html")
    print("  Open in browser to visualize campaigns and test requests")

if __name__ == "__main__":
    try:
        create_map()
    except ImportError:
        print("Error: folium not installed")
        print("Install with: pip install folium")
