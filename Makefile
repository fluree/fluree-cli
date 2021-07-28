.PHONY: uberjar native-image clean

SOURCES := $(shell find src)
RESOURCES := $(shell find resources)

fluree-cli.jar: deps.edn $(SOURCES)
	clojure -X:uberjar

uberjar: fluree-cli.jar

fluree: deps.edn $(SOURCES) $(RESOURCES)
	clojure -M:native-image

native-image: fluree

clean:
	rm -f fluree-cli.jar
	rm -f fluree