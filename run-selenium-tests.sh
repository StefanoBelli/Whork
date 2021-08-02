#!/bin/sh

failure=0
TESTS="
test.selenium.SeleniumRegisterJobSeeker
test.selenium.SeleniumSearchOffer"

echo "[$0] will run your tests"

for t in $TESTS; do
	echo "[$0] running $t..."
	mvn test -Dtest=$t 2>/dev/null >>/dev/null
	if [ $? -ne 0 ]; then
		failure=1
	fi
	echo "[$0] done ($t)"
done

[ $failure -eq 1 ] && \
	echo "[$0] one or more test failed, exiting..." || \
	echo "[$0] ok, exiting..."

exit $failure
