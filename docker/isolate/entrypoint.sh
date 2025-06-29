#!/bin/bash
set -e

sudo mount -o remount,rw /sys/fs/cgroup

CGROUP_PATH="/sys/fs/cgroup/isolate"

if [ ! -d "$CGROUP_PATH" ]; then
  sudo mkdir -p "$CGROUP_PATH"
fi

sudo mkdir -p /run/isolate
echo "$CGROUP_PATH" | sudo tee /run/isolate/cgroup

exec "$@"
