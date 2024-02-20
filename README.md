# Blind SSTI Scanner for Burp Suite

This extension enchances Burp Suite's Active Scan by adding template engine specific payloads to detect remote code execution via server-side template injection. The extension utilizes polyglot payloads and code context escaping for efficient and accurate detection.

### Usage

Run an Active Scan against the target. Identified vulnerabilities will be reported as scanner issues.

### Installation

To install the extension, download the `jar` file from the [releases](https://github.com/efecankaya/BlindSSTIScanner/releases/tag/release) page, and add it to Burp Suite from `Extensions > Add`.

### Configuration options

<img width="777" alt="Detection and Efficiency" src="https://github.com/efecankaya/BlindSSTIScanner/assets/56351220/3f2d114c-5396-4a3b-b72f-67b38e365a85">
<img width="512" alt="Template Engines" src="https://github.com/efecankaya/BlindSSTIScanner/assets/56351220/f6c7ed83-90cb-4c80-8729-95e986ed972d">
<img width="584" alt="Polling" src="https://github.com/efecankaya/BlindSSTIScanner/assets/56351220/e3e0d890-ae5c-424f-9012-13d44eb0fbd1">
