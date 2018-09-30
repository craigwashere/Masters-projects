This a program to simulate cache memory, oddly, using primitive data types. It counts the misses of data reads and writes and calculates the rate of each.

Input and output are redirected from stdin and stdout.

Once complete, it prints a summary of the miss statistics, the contents of the cache, and the first 4 MB of "memory".

For getopt() to work, I borrowed some code from stackoverflow.com. Most of main() was already written, I just needed to create the rest.

