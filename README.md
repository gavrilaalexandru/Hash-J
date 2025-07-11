HashJ - Multithreaded Hash Cracker

HashJ is a high-performance, multithreaded hash cracking tool written in Java. It is designed to brute-force hash values using a wordlist, supporting common hashing algorithms such as MD5, SHA-1, and SHA-256. With a focus on efficiency, HashJ leverages Java's concurrency utilities to distribute workload across multiple threads, providing real-time progress tracking, thread status updates, and performance statistics.

Features





Multithreaded Processing: Utilizes multiple threads to accelerate hash cracking, with configurable thread counts (defaults to available CPU cores).



Supported Algorithms: Supports MD5, SHA-1, and SHA-256 hashing algorithms.



Real-Time UI: Displays a dynamic progress bar, thread status, and performance metrics (passwords per second, ETA, etc.).



Wordlist Support: Processes large wordlists efficiently with a streaming approach to minimize memory usage.



Colored Console Output: Uses ANSI color codes for an enhanced, user-friendly terminal experience.



Robust Error Handling: Gracefully handles invalid inputs, file errors, and unsupported algorithms.

Usage

Run the program and provide the following inputs when prompted:





Target Hash: The hash to crack (e.g., an MD5, SHA-1, or SHA-256 hash).



Hash Algorithm: Choose from MD5, SHA-1, or SHA-256.



Wordlist Path: Path to the wordlist file containing potential passwords.



Number of Threads: Specify the number of threads (press Enter for default, based on available CPU cores).

The program will display a progress bar, thread activity, and estimated time of completion. Upon completion, it outputs the cracked password (if found) and final statistics.

Requirements





Java 8 or higher



A wordlist file (e.g., rockyou.txt)
