goals = dist repl test clean
.DEFAULT_GOAL : dist
.PHONY : $(goals)

dist : clean
	clojure -A:fig:min

repl :
	clojure -A:fig:build

test :
	clojure -A:fig:test 2>/dev/null

clean :
	rm -rf target/public

release : clean dist
	gsutil -m cp -r resources/public/* gs://u.nixy.io
	gsutil -m cp -r target/public/* gs://u.nixy.io
