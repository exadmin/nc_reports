#!/bin/bash

# download all repositories from github.com/Netcracker
# (C) https://stackoverflow.com/questions/19576742/how-to-clone-all-repos-at-once-from-github
# CNTX={users|orgs}; NAME={username|orgname}; PAGE=1

curl "https://api.github.com/orgs/Netcracker/repos?per_page=500" |
  grep -e 'clone_url*' |
  cut -d \" -f 4 |
  xargs -L1 git clone

