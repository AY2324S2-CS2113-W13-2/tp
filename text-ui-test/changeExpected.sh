#!/usr/bin/env bash

# reset saved file from previous run
if [ -e "save/clintonData.txt" ]
then 
    rm save/clintonData.txt
fi

# delete output from previous run
if [ -e "./text-ui-test/EXPECTED-UNIX.TXT" ]
then 
    rm ./text-ui-test/EXPECTED-UNIX.TXT
fi

# run the program, feed commands from input.txt file and redirect the output to the EXPECTED-UNIX.TXT
if ./gradlew -q run < ./text-ui-test/input.txt > ./text-ui-test/EXPECTED-UNIX.TXT; then
    echo "EXPECTED-UNIX.TXT changed according to the output from commands in input.txt!"
else
    echo "An error occurred"
    exit 1
fi

# reset saved file from previous run
if [ -e "save/clintonData.txt" ]
then 
    rm save/clintonData.txt
fi

# run the program, feed commands from input.txt file and redirect the output to the EXPECTED.TXT
if ./gradlew -q run < ./text-ui-test/input.txt > ./text-ui-test/EXPECTED.TXT; then
    echo "EXPECTED.TXT changed according to the output from commands in input.txt!"
else
    echo "An error occurred"
    exit 1
fi