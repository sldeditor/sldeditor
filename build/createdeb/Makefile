all: bin/SLDEditor.jar

bin/SLDEditor.jar:
	mvn clean install -DskipTests

install: bin/SLDEditor.jar

clean:
	mvn clean

distclean: clean

uninstall:
	-rm -f bin/SLDEditor.jar

.PHONY: all install clean distclean uninstall
