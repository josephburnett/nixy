goals = dist repl test clean
.DEFAULT_GOAL : dist
.PHONY : $(goals)

dist : clean
	clojure -A:fig:min

repl :
	clojure -A:fig:build

test :
	clojure -A:fig:test

clean :
	rm -rf target/public
