#!/bin/bash
# NOTE: when executing this script from WSL an error like 'bash: ./clone_all.sh: cannot execute: required file not found'
# NOTE: this relates to windows special characters (carriage-returns) which are not clear for Linux subsystem
# NOTE: replace such characters by executing: "sed -i 's/\r$//' clone_all.sh"

# Downloads all existed repositories using paging approach (github does not work stable without paging)
i=1

while true; do
    file="all_repos_page$i.json"
    echo "Processing file $file"

    curl "https://api.github.com/orgs/Netcracker/repos?per_page=50&page=$i" > "$file"

    size=$(stat -c%s "$file")
    if [[ "$size" -lt "6" ]]; then
        rm -f $file
        break
    fi

    cat "$file" | grep -e 'clone_url*' | cut -d \" -f 4 | xargs -L1 --no-run-if-empty git clone

    i=$((i + 1))
done
