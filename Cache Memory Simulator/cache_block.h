#pragma once

#include <stdio.h>
#include <stdlib.h> //for atoi
#include <string.h>
#include <iostream>
#include <iomanip>
#include <fstream>
using namespace std;

class cache_block
{
public:
	cache_block(void);
	cache_block(int set, bool valid, bool dirty, int num_of_words_per_block);
	~cache_block(void);
	
	bool is_dirty();
	bool is_valid();
	long get_tag();
	long get_memory_word(int addr);
	int get_set();
	int get_age();

	void reset_age();
	void increment_age();

	void set_set(int set);
	void set_block(int num_words_per_block);
	void set_dirty(bool flag);
	void set_valid(bool flag);
	void set_tag(long tag);
	void set_memory_word(int addr, long word);

private:
	bool valid, dirty;
	long tag;
	long *memory_word;
	int set;
	int age;
};

