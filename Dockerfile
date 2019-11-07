FROM clojure:tools-deps-alpine
COPY . /app
WORKDIR /app
RUN clojure -Sforce -A:test
CMD ["clojure", "-A:test"]
