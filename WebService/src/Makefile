sourcefiles = $(shell find words -name '*.java')

classfiles = $(sourcefiles:.java=.class)

all: $(classfiles)

%.class : %.java
	javac -g -cp .:/var/lib/tomcat8/webapps/axis2/WEB-INF/lib/* $<

clean: $(classfiles)
	rm -rf $?
