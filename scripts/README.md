# Scripts

This folder contains utility scripts used by the project.

## watch-address.js

Monitors an Ethereum address for outgoing or incoming transactions and flags
high-risk approvals.

### Requirements

- Node.js 18+
- RPC access to an Ethereum-compatible node

### Setup

```bash
cd scripts
npm install
```

### Usage

```bash
RPC_URL="https://your-rpc" \
WATCH_ADDRESS="0xYourWatchAddress" \
POLL_MS=12000 \
CONFIRMATIONS=2 \
START_BLOCK=12345678 \
npm run watch-address
```

Outputs:

- `state-<address>.json` keeps the last processed block.
- `evidence-<address>.log` logs detected activity.
