#!/bin/bash

# download all repositories from github.com/Netcracker
# (C) https://stackoverflow.com/questions/19576742/how-to-clone-all-repos-at-once-from-github
# CNTX={users|orgs}; NAME={username|orgname}; PAGE=1

#curl "https://api.github.com/orgs/Netcracker/repos?per_page=500" |
#  grep -e 'clone_url*' |
#  cut -d \" -f 4 |
#  xargs -L1 git clone

cd /mnt/z
rm -rf /mnt/z/tmp/workingdir
mkdir -p /mnt/z/tmp/workingdir/netcracker
cp /mnt/c/SVN/nc_reports/stats-collector/target/stats-collector.jar /mnt/z/tmp/workingdir/stats-collector.jar
cd /mnt/z/tmp/workingdir/netcracker

curl "https://api.github.com/orgs/Netcracker/repos?per_page=50&page=1" > all_repos_page1.json
curl "https://api.github.com/orgs/Netcracker/repos?per_page=50&page=2" > all_repos_page2.json
curl "https://api.github.com/orgs/Netcracker/repos?per_page=50&page=3" > all_repos_page3.json
curl "https://api.github.com/orgs/Netcracker/repos?per_page=50&page=4" > all_repos_page4.json
curl "https://api.github.com/orgs/Netcracker/repos?per_page=50&page=5" > all_repos_page5.json

cat all_repos_page1.json | grep -e 'clone_url*' | cut -d \" -f 4 | xargs -L1 --no-run-if-empty git clone
cat all_repos_page2.json | grep -e 'clone_url*' | cut -d \" -f 4 | xargs -L1 --no-run-if-empty git clone
cat all_repos_page3.json | grep -e 'clone_url*' | cut -d \" -f 4 | xargs -L1 --no-run-if-empty git clone
cat all_repos_page4.json | grep -e 'clone_url*' | cut -d \" -f 4 | xargs -L1 --no-run-if-empty git clone
cat all_repos_page5.json | grep -e 'clone_url*' | cut -d \" -f 4 | xargs -L1 --no-run-if-empty git clone

cd ..
java -jar stats-collector.jar /tmp/workingdir/netcracker