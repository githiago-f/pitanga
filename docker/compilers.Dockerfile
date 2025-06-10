FROM buildpack-deps:buster AS base

LABEL version="1.0.0"

FROM base AS clang
########################
# Install GCC          #
########################
ENV GCC_VERSIONS \
    9.2.0
RUN set -xe && \
    for VERSION in $GCC_VERSIONS; do \
    curl -fSsL "https://ftpmirror.gnu.org/gcc/gcc-$VERSION/gcc-$VERSION.tar.gz" -o /tmp/gcc-$VERSION.tar.gz && \
    mkdir /tmp/gcc-$VERSION && \
    tar -xf /tmp/gcc-$VERSION.tar.gz -C /tmp/gcc-$VERSION --strip-components=1 && \
    rm /tmp/gcc-$VERSION.tar.gz && \
    cd /tmp/gcc-$VERSION && \
    ./contrib/download_prerequisites && \
    { rm *.tar.* || true; } && \
    tmpdir="$(mktemp -d)" && \
    cd "$tmpdir"; \
    if [ $VERSION = "9.2.0" ]; then \
    ENABLE_FORTRAN=",fortran"; \
    else \
    ENABLE_FORTRAN=""; \
    fi; \
    /tmp/gcc-$VERSION/configure \
    --disable-multilib \
    --enable-languages=c,c++$ENABLE_FORTRAN \
    --prefix=/usr/local/gcc-$VERSION  > configure.log && \
    make -j$(nproc) -s && \
    make -j$(nproc) -s install-strip && \
    rm -rf /tmp/*; \
    done

FROM base AS nasm
########################
# Install ASM compiler #
########################
ENV NASM_VERSIONS \
    2.14.02
RUN set -xe && \
    for VERSION in $NASM_VERSIONS; do \
    curl -fSsL "https://www.nasm.us/pub/nasm/releasebuilds/$VERSION/nasm-$VERSION.tar.gz" -o /tmp/nasm-$VERSION.tar.gz && \
    mkdir /tmp/nasm-$VERSION && \
    tar -xf /tmp/nasm-$VERSION.tar.gz -C /tmp/nasm-$VERSION --strip-components=1 && \
    rm /tmp/nasm-$VERSION.tar.gz && \
    cd /tmp/nasm-$VERSION && \
    ./configure \
    --prefix=/usr/local/nasm-$VERSION && \
    make -j$(nproc) nasm ndisasm && \
    make -j$(nproc) strip && \
    make -j$(nproc) install && \
    echo "/usr/local/nasm-$VERSION/bin/nasm -o main.o \$@ && ld main.o" >> /usr/local/nasm-$VERSION/bin/nasmld && \
    chmod +x /usr/local/nasm-$VERSION/bin/nasmld && \
    rm -rf /tmp/*; \
    done

FROM base AS bash
########################
# Install BASH         #
########################
ENV BASH_VERSIONS \
    5.0
RUN set -xe && \
    for VERSION in $BASH_VERSIONS; do \
    curl -fSsL "https://ftpmirror.gnu.org/bash/bash-$VERSION.tar.gz" -o /tmp/bash-$VERSION.tar.gz && \
    mkdir /tmp/bash-$VERSION && \
    tar -xf /tmp/bash-$VERSION.tar.gz -C /tmp/bash-$VERSION --strip-components=1 && \
    rm /tmp/bash-$VERSION.tar.gz && \
    cd /tmp/bash-$VERSION && \
    ./configure \
    --prefix=/usr/local/bash-$VERSION && \
    make -j$(nproc) && \
    make -j$(nproc) install && \
    rm -rf /tmp/*; \
    done

FROM base AS jdk
########################
# Install JDK 21       #
########################
RUN set -xe && \
    curl -fSsL "https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.tar.gz" \
    -o /tmp/jdk-21_linux-x64_bin.tar.gz && \
    mkdir /usr/local/jdk21 && \
    tar -xf /tmp/jdk-21_linux-x64_bin.tar.gz -C /usr/local/jdk21 --strip-components=1 && \
    rm /tmp/jdk-21_linux-x64_bin.tar.gz

FROM base AS python
########################
# Install Python       #
########################
ENV PYTHON_VERSIONS \
    3.13.2 \
    2.7.17
RUN set -xe && \
    for VERSION in $PYTHON_VERSIONS; do \
    curl -fSsL "https://www.python.org/ftp/python/$VERSION/Python-$VERSION.tar.xz" -o /tmp/python-$VERSION.tar.xz && \
    mkdir /tmp/python-$VERSION && \
    tar -xf /tmp/python-$VERSION.tar.xz -C /tmp/python-$VERSION --strip-components=1 && \
    rm /tmp/python-$VERSION.tar.xz && \
    cd /tmp/python-$VERSION && \
    ./configure \
    --prefix=/usr/local/python-$VERSION && \
    make -j$(nproc) && \
    make -j$(nproc) install && \
    rm -rf /tmp/*; \
    done

FROM base AS nodejs
########################
# Install NodeJS       #
########################
ENV TYPESCRIPT_VERSIONS \
    5.8.2
RUN set -xe && \
    apt-get update && \
    apt-get install -y --no-install-recommends curl ca-certificates && \
    curl -fsSL https://deb.nodesource.com/setup_22.x | bash - && \
    apt-get install -y --no-install-recommends nodejs && \
    rm -rf /var/lib/apt/lists/* && \
    for VERSION in $TYPESCRIPT_VERSIONS; do \
    npm install -g typescript@$VERSION; \
    done

FROM base AS golang
########################
# Install Go           #
########################
ENV GO_VERSIONS \
    1.24.2
RUN set -xe && \
    for VERSION in $GO_VERSIONS; do \
    curl -fSsL "https://storage.googleapis.com/golang/go$VERSION.linux-amd64.tar.gz" -o /tmp/go-$VERSION.tar.gz && \
    mkdir /usr/local/go-$VERSION && \
    tar -xf /tmp/go-$VERSION.tar.gz -C /usr/local/go-$VERSION --strip-components=1 && \
    rm -rf /tmp/*; \
    done

FROM base AS lua
########################
# Install Lua          #
########################
ENV LUA_VERSIONS \
    5.4.8
RUN set -xe && \
    for VERSION in $LUA_VERSIONS; do \
    curl -fSsL "https://www.lua.org/ftp/lua-$VERSION.tar.gz" -o /tmp/lua-$VERSION.tar.gz && \
    mkdir /usr/local/lua-$VERSION && \
    tar -xf /tmp/lua-$VERSION.tar.gz -C /usr/local/lua-$VERSION && \
    rm -rf /tmp/*; \
    done;

FROM base AS result
RUN set -xe && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
    git \
    libcap-dev \
    libsystemd-dev \
    sqlite3 \
    libsqlite3-0 \
    locales \
    xmlto \
    docbook-xml \
    docbook-xsl \
    libxml2-utils \
    asciidoc && \
    rm -rf /var/lib/apt/lists/* && \
    git clone https://github.com/ioi/isolate.git /tmp/isolate && \
    cd /tmp/isolate && \
    git checkout v2.0 && \
    make && make install && \
    rm -rf /tmp/*
ENV BOX_ROOT=/var/local/lib/isolate
RUN mkdir -p "$BOX_ROOT" && chmod 0700 "$BOX_ROOT"

COPY --from=clang /usr/local/gcc-9.2.0 /usr/local/gcc-9.2.0
COPY --from=golang /usr/local/go-1.24.2 /usr/local/go-1.24
COPY --from=nasm /usr/local/nasm-2.14.02 /usr/local/nasm-2.14.02
COPY --from=bash /usr/local/bash-5.0 /usr/local/bash-5.0

COPY --from=jdk /usr/local/jdk21 /usr/local/jdk21
RUN ln -s /usr/local/jdk21/bin/javac /usr/local/bin/javac && \
    ln -s /usr/local/jdk21/bin/java /usr/local/bin/java && \
    ln -s /usr/local/jdk21/bin/jar /usr/local/bin/jar

COPY --from=python /usr/local/python-3.13.2 /usr/local/python-3.13.2
COPY --from=python /usr/local/python-2.7.17 /usr/local/python-2.7.17

COPY --from=nodejs /usr/bin/node /usr/bin/node
COPY --from=nodejs /usr/bin/npm /usr/bin/npm
COPY --from=nodejs /usr/bin/tsc /usr/bin/tsc
COPY --from=nodejs /usr/lib/node_modules /usr/lib/node_modules

COPY --from=lua /usr/local/lua-5.4.8 /usr/local/lua-5.4.8
COPY --from=lua /lib/x86_64-linux-gnu/libreadline.so.7 /lib/x86_64-linux-gnu/libreadline.so.7

RUN cd /usr/local/lua-5.4.8/lua-5.4.8 && make && make install && cd /
RUN ln -s /lib/x86_64-linux-gnu/libreadline.so.7 \
    /lib/x86_64-linux-gnu/libreadline.so.6

RUN set -xe && \
    echo "en_US.UTF-8 UTF-8" > /etc/locale.gen && \
    locale-gen
ENV LANG=en_US.UTF-8 LANGUAGE=en_US:en LC_ALL=en_US.UTF-8
