#!/usr/bin/env bash

# delete output from previous run
if [ -e "./text-ui-test/ACTUAL-UNIX.TXT" ]
then
    rm ./text-ui-test/ACTUAL-UNIX.TXT
fi

# reset saved file from previous run
if [ -e "save/clintonData.txt" ]
then 
    rm save/clintonData.txt
fi

./gradlew clean shadowJar

# compile the code into the bin folder, terminates if error occurred
if ! ./gradlew build
then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# run Checkstyle, terminates if error occurred
if ! ./gradlew checkstyleMain
then
    echo "********** CHECKSTYLE FAILURE **********"
    exit 1
fi

# run the program, feed commands from input.txt file and redirect the output to the ACTUAL-UNIX.TXT
./gradlew -q run < ./text-ui-test/input.txt > ./text-ui-test/ACTUAL-UNIX.TXT

# compare the output to the expected output
diff ./text-ui-test/EXPECTED-UNIX.TXT ./text-ui-test/ACTUAL-UNIX.TXT
if [ $? -eq 0 ]
then
    echo "Test passed!"
    exit 0
else
    echo "Test failed!"
    exit 1
fi