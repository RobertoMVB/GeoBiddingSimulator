# GeoBiddingSimulator
bidding system  to decide in real-time whether to bid on ad inventory based on user location signals. You'll build a simplified version of this decisioning engine.

## 🧱 Tech Stack

- **Java 21**
- **Spring Boot**
- **Maven**
- **Jackson**
- **Python 3** (validation script only)

## 📦 Prerequisites

You must have installed:

- Java **21+**
- Maven **3.8+**
- Git
- Python **3.9+** (only to run the validation script)
- Port **8080** available

## 📥 Clone the repository

```bash
git clone https://github.com/RobertoMVB/GeoBiddingSimulator.git
cd GeoBiddingSimulator
```

## ▶️ How to run the application

### 🪟 Windows

Verifique o Java:
```powershell
java -version
```

Run the application:

```powershell
mvn spring-boot:run
```

## 🍎 macOS

Verifique o java
```bash
java -version
```

If Java is not installed:
```bash
brew install openjdk@21
```

Run the application:
```bash
mvn spring-boot:run
```

## 🐧 Linux (Ubuntu / Debian)

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk maven python3 python3-venv
```

Run the application:
```bash
mvn spring-boot:run
```


## 🌐 API Endpoint
### POST /bid

Request example:
```bash
curl --location 'http://localhost:8080/bid' \
--header 'Content-Type: application/json' \
--data '{
    "request_id": "64f5b823-1e92-4b26-b7ef-a86fd766cfad",
    "timestamp": "2026-01-29T11:55:08.573059Z",
    "user": {
      "lat": -23.625124,
      "lon": -46.643525,
      "user_id": "user_081411"
    },
    "inventory": {
      "publisher_id": "pub_023",
      "ad_format": "banner",
      "floor_price": 0.06,
      "size": "320x50"
    },
    "device": {
      "type": "tablet",
      "os": "MacOS"
    }
  }'
```

### Responses

BID
```json
{
    "decision": "bid",
    "request_id": "64f5b823-1e92-4b26-b7ef-a86fd766cfad",
    "bid_price": 2.28,
    "campaign_id": "camp_052",
  "latency_ms": 3
}
```
NO BID
```json
{
  "decision": "no_bid",
  "request_id": "64f5b823-1e92-4b26-b7ef-a86fd766cfad",
  "latency_ms": 2
}
```
