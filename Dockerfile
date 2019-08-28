FROM clojure:tools-deps-alpine
COPY . /usr/src/app
WORKDIR /usr/src/app

# Cache dependencies; not sure this is a good idea? Worth a shot.
RUN clojure -Sforce -A:test
CMD ["clojure", "-A:test:test/clj"]
