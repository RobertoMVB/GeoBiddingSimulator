# Frequently Asked Questions

## General Questions

### Q: Can I use [insert language/framework]?

**A:** Yes! Use whatever you're most productive in. We recommend Go, Python, or Rust, but any language that can build an HTTP API is fine.

The important thing is meeting the performance requirements, not the language choice.

---

### Q: How strict is the 7-day deadline?

**A:** We value quality over speed. If you need an extra day or two, just let us know. But don't take weeks - we want to see how you work under realistic time constraints.

---

### Q: Can I use external libraries/frameworks?

**A:** Absolutely! Use whatever tools make sense:
- Web frameworks (Flask, FastAPI, Gin, Actix)
- Geo libraries (Shapely, geo, s2)
- Testing frameworks (pytest, testing, cargo test)

Just document your choices and explain why you picked them.

---

## Technical Questions

### Q: Do I need to persist campaign budgets between restarts?

**A:** No. In-memory state is fine for this challenge. If you want to implement budget tracking as a bonus, it's appreciated but not required.

---

### Q: How accurate do geo-calculations need to be?

**A:** Use standard algorithms:
- **Haversine** for distance (accurate to ~0.5%)
- **Ray casting** or **winding number** for point-in-polygon

Don't approximate with bounding boxes only - that's not accurate enough.

---

### Q: Should I handle campaign updates in real-time?

**A:** You need to support hot-reloading (reload campaigns.json without restarting), but you don't need to implement a real-time update API.

A simple "reload on file change" or "reload on SIGHUP" is sufficient.

---

### Q: What if multiple campaigns have the same bid price?

**A:** Pick any one (e.g., first in list, random, or campaign_id order). As long as it's deterministic and documented, we don't care about the tie-breaking rule.

---

### Q: Do I need to return ALL eligible campaigns or just the winner?

**A:** Just the winner (highest bid price). Don't return a list of all eligible campaigns.

---

### Q: What if no campaigns match?

**A:** Return a "no_bid" decision with an optional reason:

```json
{
  "request_id": "...",
  "decision": "no_bid",
  "reason": "no_matching_campaign",
  "latency_ms": 5
}
```

---

## Performance Questions

### Q: What if I can't hit p99 <50ms on my machine?

**A:** Document your results honestly. If you got p99 = 65ms and explain:
- What you tried
- Where the bottleneck is
- What you'd do to fix it

That's better than hitting the target with no explanation.

We care more about your approach and thinking than the exact number.

---

### Q: Should I optimize for single-threaded or multi-threaded performance?

**A:** Multi-threaded. The "1000 req/s" requirement assumes concurrent requests.

A single-threaded solution that handles requests sequentially won't scale.

---

### Q: Can I use approximations to hit performance targets?

**A:** **No** for core geo-calculations. Use proper Haversine and point-in-polygon algorithms.

**Yes** for optimizations like:
- Bounding box pre-checks
- Spatial indexing
- Caching

Just don't sacrifice correctness for speed.

---

### Q: How should I measure latency?

**A:** Server-side latency (from receiving request to sending response).

Include the time in your response:

```json
{
  "request_id": "...",
  "latency_ms": 12.5
}
```

The validator will also measure client-side latency for comparison.

---

## Submission Questions

### Q: What if my solution doesn't meet all requirements?

**A:** Submit anyway! Explain what you achieved, what you didn't, and why.

A well-documented 90% solution is better than no submission.

---

### Q: Should I include a Dockerfile?

**A:** Optional but appreciated. It makes it easier for us to run your solution.

But if you have clear "Quick Start" instructions, that's sufficient.

---

### Q: Can I submit before the deadline?

**A:** Yes! The earlier you submit, the sooner we can schedule your demo call.

---

### Q: What should I wear for the demo call?

**A:** We're engineers, not a fashion show. Business casual or whatever you're comfortable in. We care about your code, not your wardrobe.

---

## Edge Cases & Scenarios

### Q: What if a user is on the edge of a polygon?

**A:** Use your point-in-polygon algorithm's behavior. Standard algorithms have well-defined edge behavior (usually: points exactly on edges are considered "inside").

---

### Q: What about users near the poles or international dateline?

**A:** Our test data is all in São Paulo, Brazil, so you won't encounter these edge cases. But if you want to handle them correctly for completeness, that's great!

---

### Q: What if a campaign has negative budget_remaining?

**A:** Treat it as having no budget. Don't bid on it.

---

### Q: What if floor_price is 0?

**A:** Valid request. Any campaign with bid_price > 0 can bid.

---

### Q: What if a polygon has only 2 points?

**A:** That's not a valid polygon (need at least 3 points). You can either:
- Reject it during campaign loading, or
- Skip it during evaluation

Your choice - just document it.

---

## Evaluation Questions

### Q: What's more important: performance or code quality?

**A:** Both matter, but in different ways:
- **Performance** is a hard requirement (must meet p99 <50ms)
- **Code quality** differentiates good solutions from great ones

A fast but unmaintainable solution is not ideal.
A beautiful but slow solution won't pass.

---

### Q: Will you read all my code?

**A:** We'll focus on:
- Your core bidding logic
- Geo-calculation implementations
- Performance-critical paths
- Test coverage

We won't scrutinize every line, but we will look for:
- Clear structure
- Good naming
- Appropriate comments
- No obvious bugs

---

### Q: Does algorithm choice matter?

**A:** Yes! We want to see that you understand:
- When to use spatial indexing vs linear scan
- Time/space trade-offs
- Algorithmic complexity

If you chose a quad-tree over an R-tree, explain why. If you used a simple grid, explain the trade-offs.

---

### Q: What if I have questions during the demo call?

**A:** The demo call is mostly for US to ask YOU questions, but feel free to ask clarifying questions.

We might ask things like:
- "Walk me through this section"
- "What would happen if...?"
- "Why did you choose X over Y?"
- "How would you extend this to...?"

---

## Gotchas & Common Mistakes

### Q: Should I implement authentication?

**A:** No. Skip auth, rate limiting, HTTPS, etc. Focus on the core challenge.

---

### Q: Should I validate all request fields?

**A:** Basic validation (required fields exist, lat/lon are valid numbers) is good practice.

But don't go overboard with regex validation of UUIDs, email formats, etc. Our test data is all valid.

---

### Q: Should I handle malformed JSON?

**A:** Yes, your HTTP framework should handle this automatically. Just return 400 Bad Request.

---

### Q: Should I log every request?

**A:** For production, yes. For this challenge, optional.

Structured logging (JSON logs with timestamps, request IDs, etc.) is a nice-to-have but not required.

---

### Q: Should I write documentation comments?

**A:** For non-obvious code, yes. But don't over-comment:

```python
# Bad: obvious comment
distance = haversine_distance(lat1, lon1, lat2, lon2)  # Calculate distance

# Good: explains the "why"
# Use Haversine instead of Euclidean because we need
# accurate distance on a sphere for radius targeting
distance = haversine_distance(lat1, lon1, lat2, lon2)
```

---

## Still Have Questions?

Email us: **challenge@hypr.com**

We typically respond within 24 hours (weekdays).

---

**Good luck! 🚀**
