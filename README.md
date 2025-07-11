# HashJ - Multithreaded Hash Cracker

HashJ is a high-performance, multithreaded hash cracking tool written in Java. It brute-forces hash values using a wordlist, supporting common hashing algorithms like MD5, SHA-1, and SHA-256. Leveraging Java's concurrency utilities, HashJ distributes workload across multiple threads and provides real-time progress tracking, thread status updates, and performance statistics.

## Features
- **Multithreaded Processing**: Accelerates hash cracking with configurable thread counts (defaults to available CPU cores).
- **Supported Algorithms**: Supports MD5, SHA-1, and SHA-256.
- **Real-Time UI**: Displays a dynamic progress bar, thread status, and metrics (passwords per second, ETA, etc.).
- **Wordlist Support**: Efficiently processes large wordlists with a streaming approach to minimize memory usage.
- **Colored Console Output**: Enhances terminal experience with ANSI color codes.
- **Robust Error Handling**: Gracefully manages invalid inputs, file errors, and unsupported algorithms.

## Usage
Run the program and provide:
- **Target Hash**: The hash to crack (e.g., MD5, SHA-1, or SHA-256 hash).
- **Hash Algorithm**: Choose `MD5`, `SHA-1`, or `SHA-256`.
- **Wordlist Path**: Path to the wordlist file containing potential passwords.
- **Number of Threads**: Specify thread count (press Enter for default, based on CPU cores).

The program shows a progress bar, thread activity, and estimated completion time. Upon completion, it outputs the cracked password (if found) and final statistics.

## Requirements
- Java 8 or higher
- Maven (for building the project)
- A wordlist file (e.g., `rockyou.txt`)

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/username/HashJ.git
   ```
2. Navigate to the project directory:
   ```bash
   cd HashJ
   ```
3. Build and run the project using the provided script:
   ```bash
   chmod +x run.sh
   ./run.sh
   ```

The `run.sh` script checks for Maven, compiles the project (`mvn clean package`), and runs the main class (`hashj.HashJ`).

## Example
```bash
$ ./run.sh
Compilare proiect...
[INFO] Scanning for projects...
[INFO] Building HashJ...
[INFO] BUILD SUCCESS
Pornire aplicație...
Target hash: 5f4dcc3b5aa765d61d8327deb882cf99
Hash algorithm (MD5, SHA-1, SHA-256): MD5
Wordlist path: /path/to/wordlist.txt
Number of threads (default: 8): 
```

## License
This project is licensed under the MIT License.
