#!/usr/bin/env bash

echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>" > ./res/values/apikeys.xml
echo "<resources>" >> ./res/values/apikeys.xml
echo "    <string name=\"CRASHLYTICS_KEY\">${CRASHLYTICS_KEY}</string>" >> ./res/values/apikeys.xml
echo "</resources>" >> ./res/values/apikeys.xml