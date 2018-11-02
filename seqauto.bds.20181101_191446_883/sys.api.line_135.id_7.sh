# Execution shell: /bin/bash -e -c

curl --silent --insecure --noproxy '*' "http://127.0.0.1:8000/api/1.0/log" --data-raw "unique_id=TASKID_002" --data-raw "log_type=START" --data-raw "message=Start"