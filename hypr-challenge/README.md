# HYPR Cloud: Geo-Bidding Simulator — Technical Challenge

## Overview

Build a high-performance HTTP API that makes real-time bidding decisions based on user location and campaign geo-targeting rules.

This challenge simulates a core component of HYPR's ad decisioning infrastructure.

## The Challenge

Your API must:
1. Receive bid requests with user location data
2. Evaluate against campaign geo-targeting rules
3. Return bid decisions in **<50ms p99 latency**
4. Handle **1,000 requests/second** on a single instance

## API Specification

### Endpoint

```
POST /bid
Content-Type: application/json
```

### Request Format

```json
{
  "request_id": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-15T14:30:00Z",
  "user": {
    "lat": -23.5505,
    "lon": -46.6333,
    "user_id": "user_123456"
  },
  "inventory": {
    "publisher_id": "pub_042",
    "ad_format": "banner",
    "floor_price": 0.50,
    "size": "320x50"
  },
  "device": {
    "type": "mobile",
    "os": "iOS"
  }
}
```

### Response Format

**Success Response (200 OK):**

```json
{
  "request_id": "550e8400-e29b-41d4-a716-446655440000",
  "decision": "bid",
  "bid_price": 0.75,
  "campaign_id": "camp_042",
  "latency_ms": 12
}
```

**No-Bid Response (200 OK):**

```json
{
  "request_id": "550e8400-e29b-41d4-a716-446655440000",
  "decision": "no_bid",
  "reason": "no_matching_campaign",
  "latency_ms": 8
}
```

## Campaign Configuration

Your system must load and evaluate campaigns from `data/campaigns.json`.

### Campaign Structure

```json
{
  "campaign_id": "camp_001",
  "name": "Campaign 1 - Av Paulista",
  "budget_remaining": 15420.50,
  "bid_price": 1.25,
  "daily_budget": 2500.00,
  "active": true,
  "ad_formats": ["banner", "native"],
  "targeting": {
    "type": "radius",
    "center": {"lat": -23.5613, "lon": -46.6563},
    "radius_km": 3.5
  },
  "exclusions": [
    {
      "type": "polygon",
      "coords": [
        [-23.560, -46.655],
        [-23.561, -46.656],
        [-23.562, -46.655],
        [-23.560, -46.655]
      ]
    }
  ]
}
```

### Targeting Types

#### 1. Radius Targeting

Bid if user is within X km of a center point.

```json
{
  "type": "radius",
  "center": {"lat": -23.5613, "lon": -46.6563},
  "radius_km": 3.5
}
```

**Calculation:** Use Haversine formula for distance.

#### 2. Polygon Targeting

Bid if user is inside a polygon boundary.

```json
{
  "type": "polygon",
  "coords": [
    [-23.560, -46.655],
    [-23.561, -46.656],
    [-23.562, -46.655],
    [-23.560, -46.655]
  ]
}
```

**Calculation:** Use ray-casting or winding number algorithm.

#### 3. Multi-Radius Targeting

Bid if user is within ANY of multiple radius targets.

```json
{
  "type": "multi_radius",
  "targets": [
    {
      "center": {"lat": -23.5613, "lon": -46.6563},
      "radius_km": 2.0
    },
    {
      "center": {"lat": -23.5968, "lon": -46.6863},
      "radius_km": 1.5
    }
  ]
}
```

### Exclusion Zones

If a campaign has exclusions, do NOT bid if the user is inside any exclusion zone, even if they match the primary targeting.

Exclusions can be:
- `radius` type (same as radius targeting)
- `polygon` type (same as polygon targeting)

## Bidding Logic

Your system should:

1. **Filter active campaigns** with sufficient budget
2. **Check ad format match** (inventory format must be in campaign's `ad_formats`)
3. **Evaluate geo-targeting** (user must be in target area)
4. **Check exclusions** (user must NOT be in any exclusion zone)
5. **Validate bid price** (campaign bid must be >= inventory floor price)
6. **Select winning campaign** (highest bid price among eligible campaigns)
7. **Decrement budget** (optional: track budget spend)

## Performance Requirements

### Latency Constraints

- **p50**: <20ms
- **p95**: <40ms
- **p99**: <50ms
- **Measured at**: 1,000 requests/second sustained load

### Concurrency

Must safely handle concurrent requests without data races or corruption.

### Campaign Updates

Support hot-reloading campaigns without dropping requests or causing downtime.

## Datasets Provided

### 1. `campaigns.json`
- 100 campaigns with mixed targeting types
- Realistic geo-targeting rules for São Paulo metro area
- Mix of radius, polygon, and multi-radius targeting
- ~30% include exclusion zones

### 2. `test_requests.json`
- 10,000 sample bid requests
- Realistic location distribution
- Use for functional testing and development

### 3. `load_test_requests.json`
- 100,000 bid requests
- Use for performance/load testing
- Replay at 1k req/s to validate latency requirements

## Evaluation Criteria

### ✅ Must Have (Core Requirements)

- **Correctness**: Accurate geo-calculations (Haversine distance, point-in-polygon)
- **Performance**: Meets p99 <50ms @ 1k req/s
- **Concurrency**: Safe handling of concurrent requests
- **Code Quality**: Clean, readable, well-structured code
- **Tests**: Unit tests for critical logic

### ⭐ Nice to Have (Bonus Points)

- **Optimizations**: Spatial indexing (R-trees, quad-trees), caching, pre-computation
- **Observability**: Metrics (latency histograms, bid rate), structured logging, tracing
- **Budget Management**: Track and decrement campaign budgets on bid
- **Graceful Degradation**: Circuit breakers, rate limiting, fallback strategies
- **Documentation**: Architecture decisions, performance analysis

### ❌ Red Flags

- Over-engineering (don't build Kubernetes)
- Ignoring performance constraints
- No tests or minimal testing
- Spaghetti code with poor structure
- Incorrect geo-calculations

## Deliverables

### 1. Code Repository

- Working implementation
- Clear project structure
- Dependencies managed (requirements.txt, go.mod, Cargo.toml, etc.)

### 2. README.md

Must include:

- **Quick Start**: How to run the service
- **Architecture**: High-level design decisions
  - Why did you choose this language/framework?
  - What data structures did you use for geo-indexing?
  - How do you handle concurrency?
- **Performance**: Benchmark results
  - Include latency histograms (p50, p95, p99)
  - Show results under 1k req/s load
  - Profiling insights (what's the bottleneck?)
- **Trade-offs**: What you optimized for and what you sacrificed
- **Future Work**: What you'd do differently with more time

### 3. Tests

- Unit tests for geo-calculations
- Integration tests for API endpoints
- Load test script or instructions

### 4. Demo Call (15 minutes)

- **5 min**: Live demo of the system running
- **5 min**: Code walkthrough (1-2 interesting sections)
- **5 min**: Q&A and trade-off discussion

## Timeline

**Due**: 7 days from receipt

We value quality over speed — take the time to do it right.

## Getting Started

### 1. Clone this repository

```bash
git clone <repo_url>
cd hypr-challenge
```

### 2. Review the datasets

```bash
# Inspect campaigns
head -n 50 data/campaigns.json

# Check sample requests
head -n 20 data/test_requests.json
```

### 3. Choose your stack

Recommended:
- **Go**: Excellent performance, good standard library
- **Python**: Fast development, rich ecosystem (FastAPI, NumPy)
- **Rust**: Maximum performance, safety guarantees

But use whatever you're most productive in.

### 4. Start building

Focus on getting correctness first, then optimize for performance.

### 5. Test performance

```bash
# Example: use wrk, hey, or custom script
wrk -t4 -c100 -d30s --latency http://localhost:8080/bid @test_requests.json
```

## Example Campaign Evaluation

**Bid Request:**
```json
{
  "user": {"lat": -23.5613, "lon": -46.6563}
}
```

**Campaign:**
```json
{
  "campaign_id": "camp_001",
  "bid_price": 1.25,
  "targeting": {
    "type": "radius",
    "center": {"lat": -23.5613, "lon": -46.6563},
    "radius_km": 3.0
  }
}
```

**Evaluation:**
1. Calculate distance: 0.0 km (same location)
2. Check: 0.0 km < 3.0 km ✅
3. Result: **BID** with price 1.25

## Geo-Calculation Reference

### Haversine Distance Formula

```python
import math

def haversine(lat1, lon1, lat2, lon2):
    R = 6371  # Earth radius in km
    
    lat1, lon1, lat2, lon2 = map(math.radians, [lat1, lon1, lat2, lon2])
    
    dlat = lat2 - lat1
    dlon = lon2 - lon1
    
    a = math.sin(dlat/2)**2 + math.cos(lat1) * math.cos(lat2) * math.sin(dlon/2)**2
    c = 2 * math.asin(math.sqrt(a))
    
    return R * c
```

### Point-in-Polygon (Ray Casting)

```python
def point_in_polygon(lat, lon, polygon):
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
```

## Questions?

If you have clarifying questions about the challenge, email us at [challenge@hypr.com](mailto:challenge@hypr.com).

Good luck! 🚀
