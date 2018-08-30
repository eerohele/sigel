.PHONY: run test deploy

run:
	@clj -m sigel.core

test:
	@clj -Atest

pom: pom.xml
	@clj -Spom

deploy: test, pom
	@mvn deploy
