# Test cases
Because nobody writes perfect code. 

`bds` provides a simple unit testing functionality. 
Simply use the `-t` command line option and `bds` will run all functions `test*()` (that is functions whose names start with 'test' and have no arguments).

File <a href="bds/test_24.bds">test_24.bds</a>
```
#!/usr/bin/env bds

int twice(int n)    return 3 * n    // Looks like I don't really know what "twice" means...

void test01() {
    print("Nice test code 01\n")
}

void test02() {
    i := 1
    i++
    if( i != 2 )    error("I can't add")
}

void test03() {
    i := twice( 1 )
    if( i != 2 )    error("This is weird")
}
```

When we execute the tests, we get
```
$ bds -t ./test_24.bds 

Nice test code 01
00:00:00.002	Test 'test01': OK

00:00:00.003	Test 'test02': OK

00:00:00.004	Error: This is weird
00:00:00.004	Test 'test03': FAIL

00:00:00.005	Totals
                  OK    : 2
                  ERROR : 1

```
			

