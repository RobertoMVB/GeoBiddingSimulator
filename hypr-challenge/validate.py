#!/usr/bin/env python3
"""
Sample test script to validate your bidding API

Usage:
    python3 validate.py http://localhost:8080
"""

import sys
import json
import time
import requests
from typing import Dict, List
import statistics

def load_test_data():
    """Load test requests from dataset"""
    with open("data/test_requests.json", "r") as f:
        return json.load(f)

def load_campaigns():
    """Load campaigns from dataset"""
    with open("data/campaigns.json", "r") as f:
        return json.load(f)

def send_bid_request(base_url: str, request: Dict) -> Dict:
    """Send a single bid request and measure latency"""
    start = time.time()
    
    try:
        response = requests.post(
            f"{base_url}/bid",
            json=request,
            timeout=1.0
        )
        
        latency_ms = (time.time() - start) * 1000
        
        if response.status_code != 200:
            return {
                "success": False,
                "error": f"HTTP {response.status_code}",
                "latency_ms": latency_ms
            }
        
        data = response.json()
        
        # Validate response structure
        required_fields = ["request_id", "decision", "latency_ms"]
        for field in required_fields:
            if field not in data:
                return {
                    "success": False,
                    "error": f"Missing field: {field}",
                    "latency_ms": latency_ms
                }
        
        # Validate request_id matches
        if data["request_id"] != request["request_id"]:
            return {
                "success": False,
                "error": "request_id mismatch",
                "latency_ms": latency_ms
            }
        
        # Validate decision value
        if data["decision"] not in ["bid", "no_bid"]:
            return {
                "success": False,
                "error": f"Invalid decision: {data['decision']}",
                "latency_ms": latency_ms
            }
        
        # If bid, must have campaign_id and bid_price
        if data["decision"] == "bid":
            if "campaign_id" not in data or "bid_price" not in data:
                return {
                    "success": False,
                    "error": "Bid response missing campaign_id or bid_price",
                    "latency_ms": latency_ms
                }
        
        return {
            "success": True,
            "decision": data["decision"],
            "latency_ms": latency_ms,
            "server_latency_ms": data.get("latency_ms", 0)
        }
        
    except requests.exceptions.Timeout:
        return {
            "success": False,
            "error": "Timeout (>1s)",
            "latency_ms": 1000
        }
    except Exception as e:
        return {
            "success": False,
            "error": str(e),
            "latency_ms": 0
        }

def run_functional_tests(base_url: str, test_requests: List[Dict], num_tests: int = 100):
    """Run basic functional tests"""
    print(f"\n{'='*60}")
    print("FUNCTIONAL TESTS")
    print(f"{'='*60}")
    
    print(f"\nTesting {num_tests} requests...")
    
    results = []
    errors = []
    
    for i, request in enumerate(test_requests[:num_tests]):
        result = send_bid_request(base_url, request)
        results.append(result)
        
        if not result["success"]:
            errors.append((i, request["request_id"], result["error"]))
        
        if (i + 1) % 20 == 0:
            print(f"  Progress: {i+1}/{num_tests}")
    
    # Summary
    success_count = sum(1 for r in results if r["success"])
    bid_count = sum(1 for r in results if r.get("decision") == "bid")
    no_bid_count = sum(1 for r in results if r.get("decision") == "no_bid")
    
    print(f"\n✓ Results:")
    print(f"  • Total requests: {num_tests}")
    print(f"  • Successful: {success_count} ({success_count/num_tests*100:.1f}%)")
    print(f"  • Failed: {len(errors)}")
    print(f"  • Bid decisions: {bid_count}")
    print(f"  • No-bid decisions: {no_bid_count}")
    
    if errors:
        print(f"\n✗ Errors found:")
        for idx, req_id, error in errors[:10]:  # Show first 10
            print(f"  • Request {idx} ({req_id[:8]}...): {error}")
        if len(errors) > 10:
            print(f"  ... and {len(errors) - 10} more errors")
        return False
    
    print("\n✅ All functional tests passed!")
    return True

def run_performance_tests(base_url: str, test_requests: List[Dict], num_tests: int = 1000):
    """Run performance tests and measure latency"""
    print(f"\n{'='*60}")
    print("PERFORMANCE TESTS")
    print(f"{'='*60}")
    
    print(f"\nSending {num_tests} requests sequentially...")
    
    latencies = []
    start_time = time.time()
    
    for i, request in enumerate(test_requests[:num_tests]):
        result = send_bid_request(base_url, request)
        
        if result["success"]:
            latencies.append(result["latency_ms"])
        
        if (i + 1) % 200 == 0:
            print(f"  Progress: {i+1}/{num_tests}")
    
    total_time = time.time() - start_time
    
    # Calculate statistics
    if latencies:
        latencies_sorted = sorted(latencies)
        p50 = latencies_sorted[int(len(latencies) * 0.50)]
        p95 = latencies_sorted[int(len(latencies) * 0.95)]
        p99 = latencies_sorted[int(len(latencies) * 0.99)]
        avg = statistics.mean(latencies)
        
        print(f"\n✓ Latency Statistics (client-measured):")
        print(f"  • Average: {avg:.2f}ms")
        print(f"  • p50: {p50:.2f}ms")
        print(f"  • p95: {p95:.2f}ms")
        print(f"  • p99: {p99:.2f}ms {'✅' if p99 < 50 else '❌ (target: <50ms)'}")
        print(f"  • Min: {min(latencies):.2f}ms")
        print(f"  • Max: {max(latencies):.2f}ms")
        
        print(f"\n✓ Throughput:")
        print(f"  • Requests: {len(latencies)}")
        print(f"  • Duration: {total_time:.2f}s")
        print(f"  • Rate: {len(latencies)/total_time:.2f} req/s")
        
        print(f"\n{'='*60}")
        
        if p99 < 50:
            print("✅ PERFORMANCE TARGET MET: p99 < 50ms")
        else:
            print(f"❌ PERFORMANCE TARGET MISSED: p99 = {p99:.2f}ms (target: <50ms)")
        
        print(f"{'='*60}")
        
        return p99 < 50
    else:
        print("\n❌ No successful requests to measure latency")
        return False

def main():
    if len(sys.argv) < 2:
        print("Usage: python3 validate.py <base_url>")
        print("Example: python3 validate.py http://localhost:8080")
        sys.exit(1)
    
    base_url = sys.argv[1].rstrip('/')
    
    print(f"\n🚀 HYPR Challenge Validator")
    print(f"Testing API at: {base_url}")
    
    # Load data
    print("\nLoading test data...")
    test_requests = load_test_data()
    campaigns = load_campaigns()
    print(f"  ✓ Loaded {len(test_requests)} test requests")
    print(f"  ✓ Loaded {len(campaigns)} campaigns")
    
    # Run tests
    functional_pass = run_functional_tests(base_url, test_requests, num_tests=100)
    
    if functional_pass:
        performance_pass = run_performance_tests(base_url, test_requests, num_tests=1000)
        
        if performance_pass:
            print("\n" + "="*60)
            print("🎉 ALL TESTS PASSED!")
            print("="*60)
            print("\nYour implementation meets the basic requirements.")
            print("Next steps:")
            print("  1. Run load tests at 1k req/s (use wrk, hey, or custom script)")
            print("  2. Profile and optimize hot paths")
            print("  3. Add observability (metrics, logging)")
            print("  4. Document your architecture decisions")
            sys.exit(0)
        else:
            print("\n" + "="*60)
            print("⚠️  Functional tests passed, but performance needs work")
            print("="*60)
            sys.exit(1)
    else:
        print("\n" + "="*60)
        print("❌ FUNCTIONAL TESTS FAILED")
        print("="*60)
        print("\nFix the errors above before testing performance.")
        sys.exit(1)

if __name__ == "__main__":
    main()
