# Submission Guidelines

## What to Submit

### 1. Code Repository

**Required structure:**

```
your-solution/
├── README.md           # Your documentation (see below)
├── src/                # Your source code
├── tests/              # Your tests
├── benchmarks/         # Performance test results
├── Dockerfile          # (Optional) For easy deployment
└── [build files]       # go.mod, requirements.txt, Cargo.toml, etc.
```

**Hosting:** Share via GitHub, GitLab, or a zip file

---

## README Requirements

Your README must include:

### 1. Quick Start (5 minutes to running)

```markdown
## Quick Start

### Prerequisites
- Go 1.21+ (or your language/version)
- [Any other dependencies]

### Run the server
```bash
git clone <your-repo>
cd your-solution
go run cmd/server/main.go
# or
python3 -m pip install -r requirements.txt
python3 src/main.py
```

### Test it works
```bash
curl -X POST http://localhost:8080/bid \
  -H "Content-Type: application/json" \
  -d @data/test_requests.json | head -n 1
```
```

### 2. Architecture & Design Decisions

Answer these questions clearly:

**Language/Framework Choice**
- Why did you choose this language?
- What frameworks/libraries did you use and why?
- What did you avoid and why?

**Data Structures**
- How do you store/index campaigns in memory?
- Did you use spatial indexing? (R-tree, quad-tree, grid, etc.)
- What's the time complexity of your geo lookups?

**Concurrency Model**
- How do you handle concurrent requests?
- What synchronization primitives do you use?
- How do you ensure campaign data updates are safe?

**Performance Optimizations**
- What did you optimize and why?
- What pre-computation or caching did you do?
- Where did you trade memory for speed?

**Example:**

```markdown
## Architecture

### Language: Go
Chose Go for:
- Native concurrency (goroutines)
- Low GC overhead for consistent latency
- Strong standard library (no heavy dependencies)

### Data Structures

**Campaign Index:**
- Used a quad-tree for spatial indexing of radius campaigns
- Polygon campaigns indexed by bounding box
- Achieved O(log n) average lookup vs O(n) linear scan

**Concurrency:**
- Read-optimized with sync.RWMutex
- Campaign reloads use copy-on-write
- No shared mutable state in request handling

### Key Optimizations
1. Pre-computed campaign bounding boxes
2. Cached Haversine calculations for common points
3. Short-circuit evaluation (check budget before geo)
```

### 3. Performance Results

**Required metrics:**

```markdown
## Performance

### Test Environment
- Machine: MacBook Pro M2, 16GB RAM
- OS: macOS 14.2
- Load: 1,000 requests/second for 30 seconds

### Latency Results
```
p50:  12.3ms
p95:  28.5ms
p99:  42.1ms  ✅ (target: <50ms)
p999: 68.2ms
Max:  124.5ms
```

### Throughput
```
Requests: 30,000
Duration: 30.2s
Rate: 994 req/s
```

### Resource Usage
```
Memory: 145MB stable
CPU: 3 cores at ~70% avg
```

### Profiling Insights
- Hottest path: point-in-polygon (35% CPU)
- Optimized with bounding box pre-check
- Haversine distance: 25% CPU
- Campaign iteration: 18% CPU
```

**Include screenshots or graphs if available!**

### 4. Trade-offs & Decisions

Be honest about what you optimized for and what you sacrificed:

```markdown
## Trade-offs

### What I Optimized For
1. **Latency** - Prioritized p99 <50ms over memory efficiency
2. **Correctness** - Accurate geo-calculations over approximations
3. **Simplicity** - Clear code over clever tricks

### What I Sacrificed
1. **Memory** - Using ~150MB for indexes vs minimal 10MB
2. **Flexibility** - Campaign schema is rigid (no dynamic fields)
3. **Cold start** - 200ms to load campaigns (acceptable for long-running service)

### What I Would Change With More Time
1. Implement proper spatial index (currently using grid)
2. Add campaign budget tracking and persistence
3. Better observability (structured logging, Prometheus metrics)
4. Horizontal scaling (currently single instance)
```

---

## Testing Requirements

### Minimum Tests

1. **Unit tests** for geo-calculations
   ```
   ✓ Haversine distance accuracy
   ✓ Point-in-polygon correctness
   ✓ Edge cases (poles, dateline, etc.)
   ```

2. **Integration tests** for API
   ```
   ✓ Valid requests return 200
   ✓ Correct bid decisions
   ✓ Campaign filtering logic
   ```

3. **Performance tests**
   ```
   ✓ Latency under load
   ✓ Throughput validation
   ✓ Memory stability
   ```

### How to Run Tests

Include clear instructions:

```markdown
## Running Tests

### Unit tests
```bash
go test ./... -v
```

### Integration tests
```bash
# Start server
./bin/server &

# Run integration suite
go test ./tests/integration -v

# Cleanup
killall server
```

### Performance tests
```bash
# Using provided validator
python3 validate.py http://localhost:8080

# Or custom load test
./scripts/load_test.sh
```
```

---

## Deliverables Checklist

Before submitting, verify:

- [ ] Code builds/runs successfully
- [ ] README has Quick Start section
- [ ] README documents architecture decisions
- [ ] Performance benchmarks included
- [ ] Unit tests pass
- [ ] API meets functional requirements
- [ ] Latency target achieved (p99 <50ms @ 1k req/s)
- [ ] Code is clean and readable
- [ ] Comments explain non-obvious logic

---

## Demo Call Preparation

For the 15-minute demo call, prepare to:

### Part 1: Live Demo (5 min)

1. **Show it running**
   - Start the server
   - Send a few requests
   - Show responses

2. **Show performance**
   - Run load test
   - Display latency metrics
   - Explain results

### Part 2: Code Walkthrough (5 min)

Pick **1-2 interesting code sections** to explain:

**Good examples:**
- Your spatial indexing implementation
- How you handle concurrency
- Your most clever optimization
- A tricky edge case you handled

**Bad examples:**
- Basic HTTP routing (boring)
- JSON parsing (trivial)
- Config loading (uninteresting)

### Part 3: Q&A (5 min)

Be ready to discuss:

- "Why did you choose X over Y?"
- "What would break under Z scenario?"
- "How would you scale this to 100k req/s?"
- "What's the worst-case latency and why?"
- "Where would you add caching?"

---

## Submission

**Timeline:** 7 days from receipt

**How to submit:**

1. **Email:** [engineering@hypr.com](mailto:engineering@hypr.com)
2. **Subject:** "Geo-Bidding Challenge - [Your Name]"
3. **Body:**
   ```
   Name: [Your Name]
   GitHub: [your-repo-link] or [attached zip]
   Summary: [2-3 sentences about your approach]
   
   I'm available for the demo call on:
   - [Date/time option 1]
   - [Date/time option 2]
   - [Date/time option 3]
   ```

---

## Questions?

If you have clarifying questions:

- **Email:** [challenge@hypr.com](mailto:challenge@hypr.com)
- **Response time:** Within 24 hours

---

## Final Tips

### Do:
✅ Focus on correctness first, then optimize
✅ Document your thought process
✅ Write clean, readable code
✅ Test thoroughly
✅ Be honest about trade-offs

### Don't:
❌ Over-engineer (no Kubernetes, no microservices)
❌ Copy-paste without understanding
❌ Ignore the performance requirements
❌ Submit untested code
❌ Skimp on documentation

---

**Good luck! 🚀**

We're excited to see your solution.

The HYPR Engineering Team
