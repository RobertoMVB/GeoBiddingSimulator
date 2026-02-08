# Example Solution Structure

This directory shows the expected structure for your submission.

**Note:** This is just a skeleton - you need to implement the actual logic!

## Directory Structure

```
your-solution/
├── README.md              # Your documentation (required!)
├── src/
│   ├── main.py           # Entry point
│   ├── bidder.py         # Core bidding logic
│   ├── geo.py            # Geo-calculation utilities
│   └── models.py         # Data models
├── tests/
│   ├── test_geo.py       # Unit tests for geo functions
│   ├── test_bidder.py    # Unit tests for bidding logic
│   └── test_api.py       # Integration tests
├── benchmarks/
│   ├── results.txt       # Performance test results
│   └── profile.png       # Profiling visualization (optional)
├── requirements.txt      # Python dependencies
└── Dockerfile            # (Optional) For easy deployment
```

## Minimal README Template

Your README.md should include:

```markdown
# Geo-Bidding API Solution

## Quick Start

### Prerequisites
- Python 3.11+
- pip

### Installation
```bash
pip install -r requirements.txt
```

### Run Server
```bash
python3 src/main.py
```

Server will start on http://localhost:8080

### Test It Works
```bash
curl -X POST http://localhost:8080/bid \
  -H "Content-Type: application/json" \
  -d '{
    "request_id": "test-123",
    "user": {"lat": -23.5505, "lon": -46.6333},
    "inventory": {
      "publisher_id": "pub_001",
      "ad_format": "banner",
      "floor_price": 0.50
    }
  }'
```

## Architecture

### Language Choice: Python
Chose Python for [your reasons here]

### Key Design Decisions

**Data Structures:**
- [Describe how you index campaigns]
- [Explain your spatial indexing approach]

**Concurrency:**
- [Explain how you handle concurrent requests]

**Optimizations:**
- [List your key optimizations]

## Performance

### Test Environment
- Machine: [your specs]
- OS: [your OS]

### Results
```
Requests: 1000
Duration: 10.2s
Rate: 98 req/s

Latency:
  p50: 15ms
  p95: 35ms
  p99: 45ms ✅
```

## Trade-offs

### What I Optimized For
1. [Primary goal]
2. [Secondary goal]

### What I Would Improve
1. [Future improvement 1]
2. [Future improvement 2]

## Running Tests

```bash
pytest tests/ -v
```
```

## Code Structure Example

### src/main.py (Entry point)

```python
#!/usr/bin/env python3
from flask import Flask, request, jsonify
from bidder import BiddingEngine
import time

app = Flask(__name__)
engine = BiddingEngine("../data/campaigns.json")

@app.route('/bid', methods=['POST'])
def bid():
    start = time.time()
    
    req = request.json
    decision = engine.evaluate(req)
    
    latency_ms = (time.time() - start) * 1000
    
    return jsonify({
        "request_id": req["request_id"],
        "decision": decision.decision,
        "campaign_id": decision.campaign_id,
        "bid_price": decision.bid_price,
        "latency_ms": latency_ms
    })

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)
```

### src/geo.py (Geo utilities)

```python
import math

def haversine_distance(lat1, lon1, lat2, lon2):
    """Calculate distance in km between two points"""
    # Your implementation here
    pass

def point_in_polygon(lat, lon, polygon):
    """Check if point is inside polygon"""
    # Your implementation here
    pass
```

### src/bidder.py (Core logic)

```python
from dataclasses import dataclass
from typing import Optional

@dataclass
class BidDecision:
    decision: str
    campaign_id: Optional[str] = None
    bid_price: Optional[float] = None

class BiddingEngine:
    def __init__(self, campaigns_file):
        # Load and index campaigns
        pass
    
    def evaluate(self, request):
        # Your bidding logic here
        pass
```

### tests/test_geo.py (Unit tests)

```python
import pytest
from src.geo import haversine_distance, point_in_polygon

def test_haversine_same_point():
    dist = haversine_distance(-23.5505, -46.6333, -23.5505, -46.6333)
    assert dist == 0.0

def test_haversine_known_distance():
    # Test against known distance
    dist = haversine_distance(-23.5505, -46.6333, -23.5613, -46.6563)
    assert 1.0 < dist < 2.0  # Approximately 1.5km

def test_point_in_polygon_inside():
    polygon = [
        [-23.55, -46.63],
        [-23.56, -46.63],
        [-23.56, -46.64],
        [-23.55, -46.64],
        [-23.55, -46.63]
    ]
    assert point_in_polygon(-23.555, -46.635, polygon) == True

def test_point_in_polygon_outside():
    polygon = [
        [-23.55, -46.63],
        [-23.56, -46.63],
        [-23.56, -46.64],
        [-23.55, -46.64],
        [-23.55, -46.63]
    ]
    assert point_in_polygon(-23.57, -46.65, polygon) == False
```

## Dependencies Example

### requirements.txt

```
flask==3.0.0
pytest==7.4.3
requests==2.31.0
numpy==1.26.0  # If using for calculations
```

## Performance Results Example

### benchmarks/results.txt

```
Performance Test Results
========================

Date: 2024-01-15
Machine: MacBook Pro M2, 16GB RAM
OS: macOS 14.2

Test Configuration:
- Requests: 10,000
- Duration: 100 seconds
- Rate: 100 req/s

Latency Percentiles (milliseconds):
p50:  12.3
p75:  18.7
p90:  25.4
p95:  32.1
p99:  45.2  ✅ (target: <50ms)
p999: 78.3
Max:  124.5

Resource Usage:
Memory: 145MB (stable)
CPU: 2 cores @ 65% avg

Profiling:
Top 3 hot paths:
1. point_in_polygon: 38% CPU time
2. haversine_distance: 27% CPU time
3. campaign_iteration: 15% CPU time

Optimizations Applied:
1. Bounding box pre-check for polygons
2. Campaign filtering by ad_format before geo check
3. Early exit on budget exhaustion
```

---

This structure provides a clear, professional submission that makes it easy to:
1. Run and test your solution
2. Understand your design decisions
3. Evaluate your performance
4. Review your code quality

Adapt this to your chosen language and add more detail where relevant!
