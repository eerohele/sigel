.PHONY: run test deploy

run:
	@clj -m sigel.core

test:
	@clj -A:test:test/clj

pom: pom.xml
	@clj -Spom

deploy: test, pom
	@mvn deploy
