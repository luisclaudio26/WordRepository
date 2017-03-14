sourcefiles = $(shell find words -name '*.java')

classfiles = $(sourcefiles:.java=.class)

all: $(classfiles)

%.class : %.java
	javac -g $<

clean: $(classfiles)
	rm -rf $?