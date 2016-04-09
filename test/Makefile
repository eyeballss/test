CFLAGS=-Wall

all: sum2ints

sum2ints: main.o implementation.o
	gcc -o sum2ints main.o implementation.o

main.o: main.c implementation.h
	gcc $(CFLAGS) -c main.c

implementation.o: implementation.c implementation.h
	gcc $(CFLAGS) -c implementation.c

implementation-test.o: implementation-test.c implementation.h
	gcc $(CFLAGS) -c implementation-test.c

clean:
	rm -f *.o sum2ints
