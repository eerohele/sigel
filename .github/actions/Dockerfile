FROM clojure:tools-deps-alpine
COPY . /app
WORKDIR /app
RUN clojure -Sforce -A:test
CMD ["clojure", "-J-Dmaven.repo.local=/app/.m2", "-A:test"]
