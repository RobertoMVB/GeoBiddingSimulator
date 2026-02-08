# HYPR Cloud - Geo-Bidding Simulator Technical Challenge

## 📋 Quick Overview

**Challenge:** Build a high-performance HTTP API for real-time geo-targeted ad bidding

**Timeline:** 7 days

**Key Requirements:**
- Process bid requests based on user location
- Match against campaign geo-targeting rules
- Return decisions in <50ms p99 latency
- Handle 1,000 requests/second

---

## 📁 What's Included

### Documentation
- **[README.md](README.md)** - Complete challenge specification
- **[CAMPAIGN_EXAMPLES.md](CAMPAIGN_EXAMPLES.md)** - Campaign structure and examples
- **[SUBMISSION.md](SUBMISSION.md)** - Submission guidelines and checklist

### Datasets
- **data/campaigns.json** (82KB) - 100 campaigns with geo-targeting rules
- **data/test_requests.json** (4.1MB) - 10,000 sample bid requests
- **data/load_test_requests.json** (41MB) - 100,000 requests for load testing

### Tools & Utilities
- **generate_datasets.py** - Dataset generator (already run)
- **validate.py** - Validator to test your API
- **reference_implementation.py** - Simple reference implementation (unoptimized)
- **visualize.py** - Visualize campaigns on a map (requires folium)

---

## 🚀 Getting Started

### 1. Read the Challenge

Start with [README.md](README.md) to understand:
- API specification
- Campaign targeting rules
- Performance requirements
- Evaluation criteria

### 2. Explore the Data

```bash
# Look at campaign structure
head -n 50 data/campaigns.json | python3 -m json.tool

# Check sample requests
head -n 20 data/test_requests.json | python3 -m json.tool

# See campaign types
python3 -c "
import json
with open('data/campaigns.json') as f:
    camps = json.load(f)
    types = {}
    for c in camps:
        t = c['targeting']['type']
        types[t] = types.get(t, 0) + 1
    print('Campaign types:')
    for k, v in types.items():
        print(f'  {k}: {v}')
"
```

### 3. Understand the Logic

Review [CAMPAIGN_EXAMPLES.md](CAMPAIGN_EXAMPLES.md) for:
- Targeting type examples
- Exclusion zone logic
- Campaign selection rules

### 4. Validate Your Understanding

Run the reference implementation:

```bash
python3 reference_implementation.py
```

This shows the expected bidding logic (but is too slow for production).

### 5. Build Your Solution

Choose your stack and start building!

**Recommended languages:**
- Go (performance + concurrency)
- Python (fast development)
- Rust (maximum performance)

### 6. Test Your Implementation

Use the validator:

```bash
# Start your server on localhost:8080
# Then run:
python3 validate.py http://localhost:8080
```

### 7. Submit

Follow [SUBMISSION.md](SUBMISSION.md) for requirements and submission process.

---

## 🎯 What We're Looking For

### Core (Must Have)
✅ Correct geo-calculations  
✅ Meets latency requirement (p99 <50ms @ 1k req/s)  
✅ Safe concurrent request handling  
✅ Clean, readable code  
✅ Tests for critical logic  

### Bonus (Nice to Have)
⭐ Smart optimizations (spatial indexing, caching)  
⭐ Observability (metrics, logging)  
⭐ Budget management  
⭐ Graceful degradation  
⭐ Great documentation  

### Red Flags
❌ Over-engineering  
❌ Ignoring performance  
❌ No tests  
❌ Poor code structure  
❌ Incorrect calculations  

---

## 📊 Dataset Statistics

**Campaigns:** 100 total
- 47 radius targeting
- 40 polygon targeting  
- 13 multi-radius targeting
- 39 with exclusion zones
- 87 active campaigns

**Budget:** R$ 2.5M+ total

**Test Requests:**
- 10,000 for functional testing
- 100,000 for load testing
- Centered around São Paulo metro

---

## 🔧 Tooling

### Validation Script

Tests your API for correctness and performance:

```bash
python3 validate.py http://localhost:8080

# Output:
# - Functional tests (100 requests)
# - Performance tests (1000 requests)
# - Latency percentiles (p50, p95, p99)
```

### Reference Implementation

Unoptimized reference for logic validation:

```bash
python3 reference_implementation.py

# Shows expected bid decisions
# NOT suitable for production (too slow)
```

### Visualization (Optional)

Visualize campaigns on a map:

```bash
pip install folium
python3 visualize.py

# Opens campaign_map.html in browser
```

---

## 📚 Key Files to Read

**In order:**

1. [README.md](README.md) - Full challenge spec (start here!)
2. [CAMPAIGN_EXAMPLES.md](CAMPAIGN_EXAMPLES.md) - Understand campaign structure
3. [SUBMISSION.md](SUBMISSION.md) - Know what to deliver

---

## 💡 Tips

### Do:
✅ Start with correctness, then optimize  
✅ Profile before optimizing  
✅ Test edge cases (poles, dateline, empty results)  
✅ Document your trade-offs  
✅ Write clean, maintainable code  

### Don't:
❌ Prematurely optimize  
❌ Over-engineer (no k8s needed!)  
❌ Skip testing  
❌ Ignore performance requirements  
❌ Copy-paste without understanding  

---

## ❓ Questions?

**Email:** challenge@hypr.com  
**Response time:** Within 24 hours

---

## 🏁 Ready?

1. Read [README.md](README.md)
2. Explore the data
3. Start building
4. Test with `validate.py`
5. Submit following [SUBMISSION.md](SUBMISSION.md)

**Timeline:** 7 days from today

**Good luck! 🚀**

---

*This challenge simulates a core component of HYPR's production ad decisioning infrastructure. We're excited to see your approach!*
