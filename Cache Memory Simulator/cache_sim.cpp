// cache_sim.cpp : Defines the entry point for the console application.
//
#include "stdafx.h"
#include "cache.h"


// I got the following from 		http://stackoverflow.com/questions/10404448/getopt-h-compiling-linux-c-code-in-windows to make getopt() work
int     opterr = 1,             /* if error message should be printed */
		optind = 1,             /* index into parent argv vector */
		optopt,                 /* character checked for validity */
		optreset;               /* reset getopt */
char    *optarg;                /* argument associated with option */

#define BADCH   (int)'?'
#define BADARG  (int)':'
#define EMSG    ""

#define MEMORY_SIZE 16777216
#define CACHE_READ 0
#define CACHE_WRITE 1
#define SIZE_OF_WORD 4

#define DEBUG

// main memory will be 16M of 4-byte words
//long main_memory[16777216];

/*
* getopt --
*      Parse argc/argv argument vector.
*/
int getopt(int nargc, char * const nargv[], const char *ostr)
{
	static char *place = EMSG;              /* option letter processing */
	const char *oli;                        /* option letter list index */

	if (optreset || !*place)               /* update scanning pointer */
	{
		optreset = 0;
		if (optind >= nargc || *(place = nargv[optind]) != '-') 
		{
			place = EMSG;
			return (-1);
		}

		if (place[1] && *++place == '-')       /* found "--" */
		{
			++optind;
			place = EMSG;
			return (-1);
		}
	}                                       /* option letter okay? */

	if ((optopt = (int)*place++) == (int)':' ||	!(oli = strchr(ostr, optopt))) 
	{
		/*
		* if the user didn't specify '-' as an option,
		* assume it means -1.
		*/
		if (optopt == (int)'-')
			return (-1);
	
		if (!*place)
			++optind;
	
		if (opterr && *ostr != ':')
			(void)printf("illegal option -- %c\n", optopt);
		
		return (BADCH);
	}

	if (*++oli != ':')                     /* don't need argument */
	{
		optarg = NULL;
		if (!*place)
			++optind;
	}
	else 
	{                                  /* need an argument */
		if (*place)                     /* no white space */
			optarg = place;
		else if (nargc <= ++optind)    /* no arg */
		{
			place = EMSG;
			if (*ostr == ':')
				return (BADARG);
			if (opterr)
				(void)printf("option requires an argument -- %c\n", optopt);
			return (BADCH);
		}
		else                            /* white space */
			optarg = nargv[optind];
			place = EMSG;
			++optind;
		}
	return (optopt);                        /* dump back option letter */
}

bool parseParams(int argc, char *argv[ ], int& cache_capacity, int& cache_blocksize, int& cache_associativity)
{
	//needed for the parsing of command line options
	int c;
	bool c_flag, b_flag, a_flag;
	bool errflg = false;

	c_flag = b_flag = a_flag = errflg = false;

	//the following variables are used by getopt and
	//are defined elsewhere so we just make them
	//extern here
	//extern char *optarg;
	//extern int optind, optopt;

	//start the parsing of the command line options.
	//end is indicated by getopt returning -1
	//each option has a case statement
	//the corresponding flags are set if the option exists on
	//the command line
	//the : int he getopt indicates that the option preceeding the
	//: requires a argument to be specified
	while ((c = getopt(argc, argv, "c:b:a:")) != -1) 
	{
		switch (c) 
		{
			case 'c':	cache_capacity = atoi(optarg);
						c_flag = true;
						break;
			case 'b':	cache_blocksize = atoi(optarg);
						b_flag = true;
						break;
			case 'a':	cache_associativity = atoi(optarg);
						a_flag = true;
						break;
			case ':':   fprintf(stderr,	"Option -%c requires an operand\n", optopt);	    /* -c without operand */
						errflg++;
						break;
			case '?':	fprintf(stderr, "Unrecognised option: -%c\n", optopt);
						errflg=true;
		}
	}

	//check if we have all the options and have no illegal options
	if(errflg || !c_flag || !b_flag || !a_flag) 
	{
		fprintf(stderr, "usage: %s -c<capacity> -b<blocksize> -a<associativity>\n", argv[0]);
		return false;
	}

	return true;
}

int main(int argc, char* argv[])
{
	int cache_capacity, cache_blocksize, cache_associativity;
	
	long address, value;

#ifdef DEBUG
	cache_capacity = 4;
	cache_blocksize = 32;
	cache_associativity = 4;
#else
	if (parseParams(argc, argv, cache_capacity, cache_blocksize, cache_associativity) == false)
		return -1;
#endif
	cache cache_memory(cache_capacity, cache_blocksize, cache_associativity);

	long *memory = new long[MEMORY_SIZE];
	for (long i = 0; i < MEMORY_SIZE; i++)
		memory[i] = i;
	long temp = memory[MEMORY_SIZE/2];
	int read_write;

	/* For example, if number of words per block is 8, the valid bits is 0b111 = 7dec*/
	int num_word_address_bits = cache_memory.get_num_word_address_bits(), 
		num_set_bits = cache_memory.get_num_set_address_bits();
	unsigned int data;

#ifdef DEBUG
	ifstream fs("C:\\Users\\HB61568\\workspace\\cache_sim\\Debug\\sample_input_file.trace");;
	if (!fs.is_open())
		return -1;

	while (!fs.eof())
	{
		fs >> dec >> read_write;
		fs >> hex >> address;

#else
	// repeat till we reach the end of the input	
	while(!feof(stdin))
	{
	  	//read in whether to read or write to the cache
		cin >> dec >> read_write;

		// check again if we have reached the end
		// as this flag is set only after a 'cin'
		if(feof(stdin)) 
			return 1;

		cin >> hex >> address;
#endif		
		long temp_tag = address >> (num_word_address_bits + num_set_bits);
		int cache_index = -1;

		/*Check if block is in cache*/
		int set = (address & cache_memory.get_set_address_bits()) >> cache_memory.get_num_word_address_bits();
		for (int association = 0; association < cache_associativity; association++)
		{
			/*If it is, set association*/
			if (temp_tag == cache_memory.get_block_tag(set, association))
			{
				cache_index = association;
				break;
			}
		}

		/*If it isn't, fetch it from "memory"*/
		if (cache_index < 0)
		{
			long address_to_get = (long)(address / (cache_blocksize/SIZE_OF_WORD)) * (cache_blocksize/SIZE_OF_WORD);
			//int set_number = (address & num_sets) >> num_address_bits;	

			/* We have to find a block where the valid bit is false, or the LRU*/
			int oldest_association = 0;
			for (int j = 0; j < cache_associativity; j++)
			{
				if (cache_memory.is_valid(set, j) == false)
				{
					cache_index = j;
					break;
				}
				else
					if(cache_memory.get_block_age(set, j) > cache_memory.get_block_age(set, oldest_association))
						oldest_association = j;
			}

			if (cache_index < 0)
				cache_index = oldest_association;

			/*Now we know where to put it, we just have to stick it*/
			/*If the dirty bit is false, we can write on top of it, otherwise, we have to write it back to "memory"*/
			/*If the dirty bit is true, we write it to "memory" first before overwriting cache*/
			if (cache_memory.is_dirty(set, cache_index) == true)
			{
				long start_address = (cache_memory.get_block_tag(set, cache_index)) << (num_word_address_bits + num_set_bits);
				for (long j = start_address; j < start_address + cache_memory.get_words_per_block(); j++)
				{
					memory[j] = cache_memory.get_memory_word(set, cache_index, j);
				}
			}

			/*I hope this works*/
			cache_memory.set_block(set, cache_index, temp_tag, &memory[address_to_get]);
		}

		//if it is a cache write the we have to read the data
		if(read_write == CACHE_WRITE)
		{
		  	fs >> hex >> data;
			//memory[address] = data;
			cache_memory.set_word(set, cache_index, (address & cache_memory.get_words_per_block()-1), data);
			if (cache_memory.is_dirty(set, cache_index) == false)
				cache_memory.set_dirty(set, cache_index, true);
			//output the new contents
			//cout  << hex <<  "memory[" <<setw(8) << setfill('0') << address << "] = " << dec << memory[address] << endl;
		}
		else
		{
			//If we're reading, we don't really have to do anything, just bugger up the cache a little
			//output the contents
			//cout << hex <<  "memory[" <<setw(8) << setfill('0') << address << "] = " << dec << memory[address] << endl;
		}

#if 0
		//if it is a cache write the we have to read the data
		if(read_write == CACHE_WRITE)
		{
		  	cin >> hex >> data;
			memory[address] = data;

			//output the new contents
			cout  << hex <<  "memory[" <<setw(8) << setfill('0') << address << "] = " << dec << memory[address] << endl;
		}
		else
		{
			//output the contents
			cout << hex <<  "memory[" <<setw(8) << setfill('0') << address << "] = " << dec << memory[address] << endl;
		}
#endif
	}

	cache_memory.print_cache();

	char c;
	cin >> c;

	delete memory;

	return 0;
}

