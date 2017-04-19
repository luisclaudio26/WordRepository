sourcefiles = $(shell find words -name '*.java')

classfiles = $(sourcefiles:.java=.class)

all: $(classfiles)

%.class : %.java
	javac -g -cp .:/usr/tomcat/apache-tomcat-7.0.77/webapps/axis2/WEB-INF/lib/* $<

clean: $(classfiles)
	rm -rf $?
