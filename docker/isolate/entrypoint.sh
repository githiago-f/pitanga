#!/bin/bash
set -e

CGROUP_PATH="/sys/fs/cgroup/isolate"

if [ ! -d "$CGROUP_PATH" ]; then
  sudo mkdir -p "$CGROUP_PATH"
fi

sudo mkdir -p /run/isolate
echo "$CGROUP_PATH" | sudo tee /run/isolate/cgroup

exec "$@"
