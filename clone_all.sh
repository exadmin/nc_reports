#!/bin/bash

# Download all existed repositories using paging approach (github does not work stable without paging)
i=0

while true; do
    file="all_repos_page$i.json"
    echo "Processig file $file"

    curl "https://api.github.com/orgs/Netcracker/repos?per_page=50&page=$i" > "$file"

    size=$(stat -c%s "$file")
    if [[ "$size" -lt "6" ]]; then
        rm -f $file
        break
    fi

    cat "$file" | grep -e 'clone_url*' | cut -d \" -f 4 | xargs -L1 --no-run-if-empty git clone

    i=$((i + 1))
done
