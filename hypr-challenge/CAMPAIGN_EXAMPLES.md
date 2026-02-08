# Campaign Examples

This document explains the structure of campaigns in the dataset and provides examples of each targeting type.

## Campaign Structure

```json
{
  "campaign_id": "camp_001",           // Unique identifier
  "name": "Campaign Name",             // Human-readable name
  "budget_remaining": 15420.50,        // Budget available (R$)
  "bid_price": 1.25,                   // CPM bid price (R$)
  "daily_budget": 2500.00,            // Daily spending limit (R$)
  "active": true,                      // Campaign status
  "ad_formats": ["banner", "native"],  // Supported ad formats
  "targeting": {...},                  // Geo-targeting rules
  "exclusions": [...]                  // Exclusion zones (optional)
}
```

## Targeting Types

### 1. Radius Targeting

Target users within X kilometers of a center point.

**Example: Shopping mall campaign**

```json
{
  "campaign_id": "camp_001",
  "name": "Shopping Iguatemi - Drive to Store",
  "bid_price": 1.85,
  "targeting": {
    "type": "radius",
    "center": {
      "lat": -23.5670,
      "lon": -46.6890
    },
    "radius_km": 2.5
  },
  "ad_formats": ["banner", "native"]
}
```

**Logic:** Bid if `haversine_distance(user, center) <= radius_km`

**Use Case:** Drive foot traffic to physical locations (stores, malls, restaurants)

---

### 2. Polygon Targeting

Target users inside a defined geographic boundary.

**Example: Neighborhood targeting**

```json
{
  "campaign_id": "camp_015",
  "name": "Jardins Luxury Retail",
  "bid_price": 2.10,
  "targeting": {
    "type": "polygon",
    "coords": [
      [-23.5650, -46.6580],
      [-23.5680, -46.6550],
      [-23.5720, -46.6600],
      [-23.5690, -46.6630],
      [-23.5650, -46.6580]  // First point repeated to close polygon
    ]
  },
  "ad_formats": ["video", "native"]
}
```

**Logic:** Bid if `point_in_polygon(user, coords) == true`

**Use Case:** Target specific neighborhoods, ZIP codes, or custom boundaries

---

### 3. Multi-Radius Targeting

Target users near ANY of multiple locations.

**Example: Retail chain with multiple stores**

```json
{
  "campaign_id": "camp_042",
  "name": "Pharmacy Chain - 3 Locations",
  "bid_price": 1.40,
  "targeting": {
    "type": "multi_radius",
    "targets": [
      {
        "center": {"lat": -23.5613, "lon": -46.6563},  // Store 1: Paulista
        "radius_km": 1.5
      },
      {
        "center": {"lat": -23.5968, "lon": -46.6863},  // Store 2: Vila Olimpia
        "radius_km": 1.2
      },
      {
        "center": {"lat": -23.6033, "lon": -46.6667},  // Store 3: Moema
        "radius_km": 1.8
      }
    ]
  },
  "ad_formats": ["banner", "interstitial"]
}
```

**Logic:** Bid if user is within ANY target's radius

**Use Case:** Chains with multiple locations (retail, restaurants, services)

---

## Exclusion Zones

Campaigns can define zones where they DON'T want to bid, even if the user matches primary targeting.

### Use Cases for Exclusions

1. **Competitor locations** - Don't advertise near competitor stores
2. **Low-performing areas** - Exclude zones with poor conversion
3. **Brand safety** - Avoid certain neighborhoods or areas

### Exclusion Types

Exclusions support the same types as targeting:
- `radius` - Exclude users within X km of a point
- `polygon` - Exclude users inside a boundary

**Example: Campaign with competitor exclusions**

```json
{
  "campaign_id": "camp_078",
  "name": "Fast Food Chain - Citywide",
  "bid_price": 1.65,
  "targeting": {
    "type": "radius",
    "center": {"lat": -23.5505, "lon": -46.6333},  // Downtown SP
    "radius_km": 10.0  // Wide targeting
  },
  "exclusions": [
    {
      "type": "radius",
      "center": {"lat": -23.5613, "lon": -46.6550},  // Competitor A
      "radius_km": 0.5
    },
    {
      "type": "radius",
      "center": {"lat": -23.5875, "lon": -46.6575},  // Competitor B
      "radius_km": 0.5
    },
    {
      "type": "polygon",
      "coords": [  // Low-performing zone
        [-23.600, -46.650],
        [-23.605, -46.650],
        [-23.605, -46.655],
        [-23.600, -46.655],
        [-23.600, -46.650]
      ]
    }
  ],
  "ad_formats": ["banner", "video"]
}
```

**Logic:**
1. First check if user matches targeting (inside 10km radius)
2. Then check if user is in ANY exclusion zone
3. If in exclusion zone → no bid (even if targeting matched)

---

## Budget Management

### Fields

- **budget_remaining**: Total campaign budget left (R$)
- **daily_budget**: Maximum to spend per day (R$)

### Logic

A campaign should only bid if:
```
budget_remaining > 0 && daily_spend < daily_budget
```

In a production system, you'd decrement budgets on each bid.

**Optional Implementation:** Track budget spend and prevent bidding when exhausted.

---

## Ad Format Matching

Campaigns specify which ad formats they support.

**Available formats:**
- `banner` - Standard display banners
- `native` - Native ad units
- `video` - Video ads
- `interstitial` - Full-screen ads

**Logic:** Only bid if `inventory.ad_format IN campaign.ad_formats`

---

## Floor Price Validation

Publishers set a minimum price (floor price) for their inventory.

**Logic:** Only bid if `campaign.bid_price >= inventory.floor_price`

---

## Campaign Selection

When multiple campaigns are eligible for a request, select the one with the **highest bid price**.

**Example:**

```
Request: User at [-23.5613, -46.6563]

Eligible campaigns:
- camp_001: bid_price = 1.25
- camp_042: bid_price = 1.85  ← Winner
- camp_078: bid_price = 1.40

Decision: Bid R$ 1.85 for camp_042
```

---

## Dataset Statistics

The provided `campaigns.json` contains:

- **100 campaigns** total
- **~47% radius targeting**
- **~40% polygon targeting**
- **~13% multi-radius targeting**
- **~30% have exclusion zones**
- **87% active campaigns**
- **Average bid:** R$ 1.25
- **Total budget:** R$ 2.5M+

All campaigns are centered around São Paulo metro area for realistic testing.

---

## Testing Your Implementation

Use the reference implementation to validate your logic:

```bash
python3 reference_implementation.py
```

Compare your bid decisions against the reference for the same requests.

**Note:** The reference is intentionally unoptimized. Your production implementation should be much faster while producing the same decisions.
