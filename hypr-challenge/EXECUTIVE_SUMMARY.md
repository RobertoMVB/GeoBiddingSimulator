# HYPR Cloud - Technical Challenge Package

## Executive Summary

This is a production-grade technical challenge designed to evaluate senior engineering candidates for VP-level positions at HYPR.

### What It Tests

**Core Competencies:**
- Systems design and architecture
- Performance optimization (latency-critical systems)
- Geo-spatial algorithms and data structures
- Concurrent programming
- Production engineering mindset

**Differentiators:**
- Cannot be solved by copy-pasting AI-generated code
- Requires real trade-off decisions
- Forces candidates to demonstrate depth over breadth
- Realistic to HYPR's actual technical challenges

---

## Challenge Overview

**Task:** Build a high-performance HTTP API for geo-targeted ad bidding decisions

**Requirements:**
- Process 1,000 requests/second
- p99 latency <50ms
- Accurate geo-calculations (Haversine, point-in-polygon)
- Safe concurrency
- Clean, maintainable code

**Timeline:** 7 days

**Deliverable:**
- Working implementation
- Documentation of architecture decisions
- Performance benchmarks
- 15-minute demo call

---

## Why This Challenge Works

### 1. AI-Resistant

Large language models will produce naive O(n) implementations that:
- Work functionally but fail performance requirements
- Don't consider spatial indexing or caching strategies
- Miss critical edge cases
- Can't explain trade-offs in the demo call

**Real engineers** will:
- Profile and optimize hot paths
- Choose appropriate data structures (R-trees, quad-trees, grids)
- Make conscious trade-offs (memory vs speed)
- Articulate their decisions clearly

### 2. Realistic Complexity

This mirrors an actual HYPR production component:
- Real geo-targeting logic we use daily
- Realistic latency constraints (RTB systems are <100ms)
- Production-scale load (1k req/s is modest for adtech)
- Actual business constraints (budgets, exclusions, ad formats)

### 3. Multiple Signal Extraction

The challenge reveals:

**Technical depth:**
- Algorithm knowledge (spatial indexing, geo calculations)
- Performance engineering (profiling, optimization)
- Systems thinking (concurrency, memory management)

**Judgment:**
- What to optimize (and what not to)
- When "good enough" is actually good enough
- How to structure code for maintainability

**Communication:**
- Can they explain complex trade-offs?
- Do they document decisions clearly?
- Can they defend their choices?

**Pragmatism:**
- Do they over-engineer?
- Do they under-deliver?
- Can they ship quality work on time?

---

## Package Contents

### Documentation (7 files)
- **INDEX.md** - Getting started guide
- **README.md** - Full challenge specification
- **CAMPAIGN_EXAMPLES.md** - Data structure reference
- **SUBMISSION.md** - Submission guidelines
- **FAQ.md** - Common questions
- **example_solution/** - Expected submission structure

### Datasets (3 files, 45MB total)
- **campaigns.json** (100 campaigns)
- **test_requests.json** (10,000 requests)
- **load_test_requests.json** (100,000 requests)

All geo data centered on São Paulo metro area for realism.

### Tools (4 utilities)
- **validate.py** - Automated correctness/performance testing
- **reference_implementation.py** - Unoptimized reference for logic validation
- **visualize.py** - Campaign/request visualization
- **generate_datasets.py** - Dataset regeneration (if needed)

---

## Evaluation Rubric

### Must Pass (Core Requirements)
- ✅ Functional correctness (accurate geo-calculations)
- ✅ Performance target (p99 <50ms @ 1k req/s)
- ✅ Concurrent request handling
- ✅ Basic test coverage
- ✅ Readable code structure

### Differentiators (Good → Great)
- ⭐ Sophisticated optimizations (spatial indexing)
- ⭐ Clear architecture documentation
- ⭐ Observability (metrics, logging)
- ⭐ Thoughtful trade-off analysis
- ⭐ Strong demo presentation

### Red Flags (Disqualifiers)
- ❌ Incorrect geo-calculations
- ❌ Fails performance requirements
- ❌ No tests or documentation
- ❌ Over-engineered (unnecessary complexity)
- ❌ Poor code quality (spaghetti, no structure)

---

## Expected Time Investment

**For qualified candidates:**
- 8-12 hours total work
- Spread over 7 days
- ~2 hours for design/planning
- ~4-6 hours for implementation
- ~2-3 hours for testing/optimization
- ~1 hour for documentation

**Red flag:** If a candidate says they spent 40+ hours, they either:
- Over-engineered the solution
- Lack relevant experience
- Don't work efficiently

---

## How to Use This Challenge

### For Recruiters

**Sending to candidates:**
1. Email the entire `/hypr-challenge` folder as a zip
2. Include clear deadline (7 days from receipt)
3. Provide contact email for questions
4. Schedule demo call window in advance

**Template email:**
```
Subject: HYPR VP Engineering - Technical Challenge

Hi [Name],

Attached is the technical challenge for the VP of Engineering position.

Quick summary:
- Build a geo-bidding API (full spec in README.md)
- Due: [Date, 7 days from now]
- Demo call: [Schedule 15-min slot]

Questions? Reply to this email.

The package includes everything you need:
- Full specification
- Test datasets
- Validation tools
- Example structure

Good luck!
```

**Evaluating submissions:**

Run the validator:
```bash
python3 validate.py http://localhost:8080
```

Review in order:
1. Does it work? (functional tests pass)
2. Is it fast? (p99 <50ms)
3. Is it clean? (code quality)
4. Do they understand it? (demo call)

### For Hiring Managers

**Demo call agenda (15 min):**

1. **Live demo (5 min)**
   - Candidate runs their solution
   - Shows performance results
   - Walks through key metrics

2. **Code review (5 min)**
   - Ask about 1-2 interesting sections
   - Focus on their optimizations
   - Probe on trade-offs

3. **Scenario questions (5 min)**
   - "What if we needed 10k req/s?"
   - "What if campaigns updated every second?"
   - "Where would this break under load?"

**Good signals:**
- Clear explanations
- Acknowledges limitations
- Discusses trade-offs thoughtfully
- Has profiling data
- Can reason about scale

**Bad signals:**
- Can't explain their code
- Dismisses performance concerns
- Over-confident without data
- No consideration of edge cases
- Blames tools/environment

---

## Customization Options

### Make It Easier
- Increase p99 target to 100ms
- Reduce load requirement to 500 req/s
- Provide partial implementation
- Allow 10 days instead of 7

### Make It Harder
- Add real-time campaign updates via API
- Require budget persistence
- Demand 10k req/s throughput
- Add fraud detection (block suspicious requests)
- Require horizontal scalability

---

## Success Metrics

After 6 months of use:

- **Submit rate:** 75% of candidates submit
- **Pass rate:** 25% meet all requirements
- **False negatives:** <5% (good candidates we rejected)
- **False positives:** <10% (bad candidates we hired)
- **Time to evaluate:** <30 min per submission

---

## Maintenance

**Quarterly:**
- Regenerate datasets with updated dates
- Review and update FAQs based on common questions
- Adjust difficulty if pass rate drifts

**Yearly:**
- Major refresh of challenge scenario (keep structure)
- Update technology references
- Incorporate new evaluation criteria

---

## Questions?

**Technical:** challenge@hypr.com  
**Recruiting:** [your-recruiting-contact]

---

## License & Usage

This challenge is proprietary to HYPR.

**Allowed:**
- Use for evaluating HYPR engineering candidates
- Share with candidates during hiring process
- Customize for internal use

**Not allowed:**
- Public distribution
- Use by other companies without permission
- Sharing on job boards or public repos

---

**Created:** January 2026  
**Version:** 1.0  
**Owner:** HYPR Engineering Team
