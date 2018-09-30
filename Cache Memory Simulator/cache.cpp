#include "stdafx.h"
#include "cache.h"
#include <math.h>

cache::cache(void)
{
}

cache::~cache(void)
{
	delete [] block;
}

cache::cache(int cache_capacity, int cache_blocksize, int cache_associativity)
{
	this->cache_capacity = cache_capacity * 1024;			//set capacity into bytes
	this->cache_associativity = cache_associativity;		//may need for later
	num_set_address_bits = log((float)cache_blocksize)/log((float)2);
	num_of_words_per_block = cache_blocksize/4;		//4 bytes per word
	num_of_sets = this->cache_capacity / (cache_blocksize * cache_associativity);
			//4096/(32*4) = 32 which is 0x20
	num_word_address_bits = log((float)num_of_words_per_block)/log((float)2);
	set_address_bits = (num_of_sets-1) << num_word_address_bits;

	block = new cache_block *[num_of_sets];
	for (int i = 0; i < num_of_sets; i++)
	{
		block[i] = new cache_block[cache_associativity];//((i/cache_associativity), false, true, num_of_words_per_block);

		for (int j = 0; j < cache_associativity; j++)
		{
			block[i][j].set_set(i);
			block[i][j].set_block(num_of_words_per_block);
		}

	}
}

void cache::print_cache()
{
	cout << "Set\tV\tTag\tDirty" << endl;
	for (int set = 0; set < cache_associativity; set++)
	{
		for (int association = 0; association < cache_associativity; association++)
		{
			cout << block[set][association].get_set() << "\t" << block[set][association].is_valid()? '1' : '0';
			cout << "\t";
			cout << hex <<  setw(8) << setfill('0') << block[set][association].get_tag();
			cout << "\t" << block[set][association].is_dirty()? '1' : '0';
			cout << "\t";
			//printf("%i\t%i\t%08Xl\t%i", block[i][j].get_set(), block[i][j].is_valid()? "1" : "0", block[i][j].get_tag(), block[i][j].is_dirty()? "1" : "0");
			for (int word = 0; word < num_of_words_per_block; word++)
			{
				cout  << hex <<  setw(8) << setfill('0') << block[set][association].get_memory_word(word);
				cout << "\t";
			}
				//printf("%08Xl\t", block[i][j].get_memory_word(j));

			cout << endl;
		}
	}
}

long cache::get_block_tag(int set, int association)
{	return block[set][association].get_tag();	}

int cache::get_capacity()
{	return cache_capacity;	}

int cache::get_num_sets()
{	return num_of_sets;	}

int cache::get_num_word_address_bits()
{	return num_word_address_bits;	}

int cache::get_num_set_address_bits()
{	return num_set_address_bits;	}

int cache::get_set_address_bits()
{	return set_address_bits;	}

int cache::get_words_per_block()
{	return num_of_words_per_block;	}

int cache::get_block_age(int set, int association)
{	return block[set][association].get_age();	}

bool cache::is_valid(int set, int association)
{	return block[set][association].is_valid();	}

bool cache::is_dirty(int set, int association)
{	return block[set][association].is_dirty();	}

long cache::get_memory_word(int set, int association, int address)
{	return block[set][association].get_memory_word(address);	}

void cache::set_block(int set, int association, long temp_tag, long * memory)
{
	long temp = *memory;
	block[set][association].reset_age();
	block[set][association].set_tag(temp_tag);
	block[set][association].set_dirty(false);
	block[set][association].set_valid(true);

	for (int i = 0; i < num_of_words_per_block; i++)
	{
		block[set][association].set_memory_word(i, *memory);
		memory++;
	}
}

void cache::set_word(int set, int association, int index, long word)
{	block[set][association].set_memory_word(index, word);	}

void cache::set_dirty(int set, int association, bool flag)
{	block[set][association].set_dirty(flag);	}

void cache::set_valid(int set, int association, bool flag)
{	block[set][association].set_valid(flag);	}

