#include "stdafx.h"
#include "cache_block.h"

cache_block::cache_block(void)
{
	this->valid = false;
	this->dirty = false;

	age = 0;
}

cache_block::~cache_block(void)
{
	delete [] memory_word;
}

cache_block::cache_block(int set, bool valid, bool dirty, int num_of_words_per_block)
{
	this->valid = valid;
	this->dirty = dirty;
	this->set = set;

	memory_word = new long[num_of_words_per_block];

	for (int i = 0; i < num_of_words_per_block; i++)
		memory_word = 0;

	age = 0;
}

bool cache_block::is_dirty()
{	return dirty;	}

bool cache_block::is_valid()
{	return valid;	}

int cache_block::get_set()
{	return set;	}

long cache_block::get_tag()
{	return tag;	}

long cache_block::get_memory_word(int addr)
{	return memory_word[addr];	}

void cache_block::increment_age()
{	age++;	}

int cache_block::get_age()
{	return age;	}

void cache_block::reset_age()
{	age = 0;	}

void cache_block::set_set(int set)
{	this->set = set;	}

void cache_block::set_block(int num_words_per_block)
{
	memory_word = new long[num_words_per_block];

	for (int i = 0; i < num_words_per_block; i++)
		memory_word[i] = 0;
}

void cache_block::set_dirty(bool flag)
{	this->dirty = flag;	}

void cache_block::set_valid(bool flag)
{	this->valid = flag;	}

void cache_block::set_tag(long tag)
{	this->tag = tag;	}

void cache_block::set_memory_word(int addr, long word)
{	memory_word[addr] = word;	}
