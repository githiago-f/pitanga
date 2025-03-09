############################
# Base Stage: common deps  #
############################
FROM ubuntu:18.04 AS base
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y \
    build-essential \
    wget \
    curl \
    git \
    pkg-config \
    ca-certificates \
    xz-utils \
    libcap-dev \
    libsystemd-dev \
    asciidoc \
    libreadline-dev \
    && rm -rf /var/lib/apt/lists/*

############################
# Stage 1: Install isolate #
############################
FROM base AS isolate-stage
RUN git clone https://github.com/ioi/isolate.git /tmp/isolate && \
    cd /tmp/isolate && \
    make PREFIX=/usr/local/isolate && \
    make install PREFIX=/usr/local/isolate && \
    cd / && rm -rf /tmp/isolate

############################
# Stage 2: Install NASM    #
############################
FROM base AS nasm-stage
RUN wget https://www.nasm.us/pub/nasm/releasebuilds/2.14.02/nasm-2.14.02.tar.gz && \
    tar xzf nasm-2.14.02.tar.gz && cd nasm-2.14.02 && \
    ./configure --prefix=/usr/local/nasm && make && make install && \
    cd / && rm -rf nasm-2.14.02 nasm-2.14.02.tar.gz

############################
# Stage 3: Install Bash    #
############################
FROM base AS bash-stage
RUN wget https://ftp.gnu.org/gnu/bash/bash-5.0.tar.gz && \
    tar xzf bash-5.0.tar.gz && cd bash-5.0 && \
    ./configure --prefix=/usr/local/bash && make && make install && \
    cd / && rm -rf bash-5.0 bash-5.0.tar.gz

############################
# Stage 4: Install Clang   #
############################
FROM base AS clang-stage
RUN wget https://releases.llvm.org/7.0.1/clang+llvm-7.0.1-x86_64-linux-gnu-ubuntu-16.04.tar.xz && \
    mkdir -p /usr/local/clang && \
    tar -C /usr/local/clang --strip-components=1 -xf clang+llvm-7.0.1-x86_64-linux-gnu-ubuntu-16.04.tar.xz && \
    rm clang+llvm-7.0.1-x86_64-linux-gnu-ubuntu-16.04.tar.xz

############################
# Stage 5: Install Go      #
############################
FROM base AS go-stage
RUN wget https://go.dev/dl/go1.20.linux-amd64.tar.gz && \
    tar -C /usr/local -xzf go1.20.linux-amd64.tar.gz && \
    mv /usr/local/go /usr/local/go1.20 && \
    rm go1.20.linux-amd64.tar.gz

############################
# Stage 6: Install OpenJDK #
############################
FROM base AS jdk-stage
RUN wget https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.6%2B7/OpenJDK21U-jdk_x64_linux_hotspot_21.0.6_7.tar.gz && \
    mkdir -p /usr/local/jdk21 && \
    tar -C /usr/local/jdk21 --strip-components=1 -xzf OpenJDK21U-jdk_x64_linux_hotspot_21.0.6_7.tar.gz && \
    rm OpenJDK21U-jdk_x64_linux_hotspot_21.0.6_7.tar.gz

############################
# Stage 7: Install NodeJS  #
############################
FROM base AS node-stage
RUN wget https://nodejs.org/dist/v12.14.0/node-v12.14.0-linux-x64.tar.xz && \
    mkdir -p /usr/local/node && \
    tar -C /usr/local/node --strip-components=1 -xf node-v12.14.0-linux-x64.tar.xz && \
    rm node-v12.14.0-linux-x64.tar.xz

############################
# Stage 8: Install Lua     #
############################
FROM base AS lua-stage
RUN wget https://www.lua.org/ftp/lua-5.3.5.tar.gz && \
    tar xzf lua-5.3.5.tar.gz && cd lua-5.3.5 && \
    make linux && make INSTALL_TOP=/usr/local/lua install && \
    cd / && rm -rf lua-5.3.5 lua-5.3.5.tar.gz

############################
# Stage 9: Install SQLite  #
############################
FROM base AS sqlite-stage
RUN wget https://www.sqlite.org/2019/sqlite-autoconf-3270200.tar.gz && \
    tar xzf sqlite-autoconf-3270200.tar.gz && cd sqlite-autoconf-3270200 && \
    ./configure --prefix=/usr/local/sqlite && make && make install && \
    cd / && rm -rf sqlite-autoconf-3270200 sqlite-autoconf-3270200.tar.gz

############################
# Final Stage: Assemble    #
############################
FROM ubuntu:18.04
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y ca-certificates \
    libcap2 \
    && ldconfig \
    && rm -rf /var/lib/apt/lists/*
# Copy installations from all stages
COPY --from=isolate-stage /usr/local/isolate /usr/local/isolate
COPY --from=nasm-stage /usr/local/nasm /usr/local/nasm
COPY --from=bash-stage /usr/local/bash /usr/local/bash
COPY --from=clang-stage /usr/local/clang /usr/local/clang
COPY --from=go-stage /usr/local/go1.20 /usr/local/go
COPY --from=jdk-stage /usr/local/jdk21 /usr/local/jdk21
COPY --from=node-stage /usr/local/node /usr/local/node
COPY --from=lua-stage /usr/local/lua /usr/local/lua
COPY --from=sqlite-stage /usr/local/sqlite /usr/local/sqlite

ENV PATH="/usr/local/isolate/bin:/usr/local/jdk21/bin:${PATH}"
