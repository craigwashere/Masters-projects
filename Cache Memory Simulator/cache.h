#pragma once
#include "cache_block.h"

class cache
{
public:
	cache(void);
	cache(int cache_capacity, int cache_blocksize, int cache_associativity);
	~cache(void);

	void set_word(int set, int association, int index, long word);
	void print_cache();
	long get_block_tag(int set, int association);
	bool is_valid(int set, int association);
	bool is_dirty(int set, int association);
	long get_memory_word(int set, int cache_index, int address);
	int get_capacity();
	int get_num_sets();
	int get_num_word_address_bits();
	int get_words_per_block();
	int get_num_set_address_bits();
	int get_set_address_bits();
	int get_block_age(int set, int association);

	void set_dirty(int set, int association, bool flag);
	void set_valid(int set, int association, bool flag);
	void set_block(int set, int association, long temp_tag, long * memory);
private:
	cache_block ** block;

	int dataread_misses, datawrite_misses, num_dirty_blocks_evicted;
	float total_missrate, data_read_missrate, datawrite_missrate;

	int num_set_address_bits, num_of_words_per_block, block_to_fetch, num_of_sets, num_word_address_bits, cache_capacity;
	int cache_associativity, set_address_bits;
};

