FROM pitanga/compilers:1.0.0 AS compilers

EXPOSE 8443

WORKDIR /api

RUN mkdir -p /run/isolate && echo "/sys/fs/cgroup" > /run/isolate/cgroup

RUN useradd -u 1000 -m -r pitanga && \
    echo "pitanga ALL=(ALL) NOPASSWD: ALL" > /etc/sudoers

USER pitanga

LABEL version=0.0.2

CMD ["sleep", "infinity"]
