FROM buildpack-deps:buster AS base

LABEL version="1.0.0"

########################
# Install GCC          #
########################
ENV GCC_VERSIONS \
      7.4.0 \
      8.3.0 \
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

########################
# Install isolate      #
########################
RUN set -xe && \
    apt-get update && \
    apt-get install -y --no-install-recommends git libcap-dev && \
    rm -rf /var/lib/apt/lists/* && \
    git clone https://github.com/judge0/isolate.git /tmp/isolate && \
    cd /tmp/isolate && \
    git checkout ad39cc4d0fbb577fb545910095c9da5ef8fc9a1a && \
    make -j$(nproc) install && \
    rm -rf /tmp/*
ENV BOX_ROOT=/var/local/lib/isolate
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

########################
# Install JDK 21       #
########################
RUN set -xe && \
    curl -fSsL "https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.tar.gz" \
    -o /tmp/jdk-21_linux-x64_bin.tar.gz && \
    mkdir /usr/local/jdk21 && \
    tar -xf /tmp/jdk-21_linux-x64_bin.tar.gz -C /usr/local/jdk21 --strip-components=1 && \
    rm /tmp/jdk-21_linux-x64_bin.tar.gz && \
    ln -s /usr/local/jdk21/bin/javac /usr/local/bin/javac && \
    ln -s /usr/local/jdk21/bin/java /usr/local/bin/java && \
    ln -s /usr/local/jdk21/bin/jar /usr/local/bin/jar

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

########################
# Install NodeJS       #
########################
ENV NODE_VERSIONS \
    12.14.0
RUN set -xe && \
    for VERSION in $NODE_VERSIONS; do \
        curl -fSsL "https://nodejs.org/dist/v$VERSION/node-v$VERSION.tar.gz" -o /tmp/node-$VERSION.tar.gz && \
        mkdir /tmp/node-$VERSION && \
        tar -xf /tmp/node-$VERSION.tar.gz -C /tmp/node-$VERSION --strip-components=1 && \
        rm /tmp/node-$VERSION.tar.gz && \
        cd /tmp/node-$VERSION && \
        ./configure \
        --prefix=/usr/local/node-$VERSION && \
        make -j$(nproc) && \
        make -j$(nproc) install && \
        rm -rf /tmp/*; \
    done

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

########################
# Install Lua          #
########################
ENV LUA_VERSIONS \
  5.4.7
RUN set -xe && \
    for VERSION in $LUA_VERSIONS; do \
        curl -fSsL "https://downloads.sourceforge.net/project/luabinaries/$VERSION/Tools%20Executables/lua-${VERSION}_Linux44_64_bin.tar.gz" -o /tmp/lua-$VERSION.tar.gz && \
        mkdir /usr/local/lua-$VERSION && \
        tar -xf /tmp/lua-$VERSION.tar.gz -C /usr/local/lua-$VERSION && \
        rm -rf /tmp/*; \
    done; \
    ln -s /lib/x86_64-linux-gnu/libreadline.so.7 /lib/x86_64-linux-gnu/libreadline.so.6

########################
# Install TypeScript   #
########################
ENV TYPESCRIPT_VERSIONS \
    5.8.2
RUN set -xe && \
    curl -fSsL "https://deb.nodesource.com/setup_12.x" | bash - && \
    apt-get update && \
    apt-get install -y --no-install-recommends nodejs && \
    rm -rf /var/lib/apt/lists/* && \
    for VERSION in $TYPESCRIPT_VERSIONS; do \
        npm install -g typescript@$VERSION; \
    done

########################
# Install SQLite       #
########################
RUN set -xe && \
    apt-get update && \
    apt-get install -y --no-install-recommends sqlite3 && \
    rm -rf /var/lib/apt/lists/*

RUN set -xe && \
    apt-get update && \
    apt-get install -y --no-install-recommends locales && \
    rm -rf /var/lib/apt/lists/* && \
    echo "en_US.UTF-8 UTF-8" > /etc/locale.gen && \
    locale-gen
ENV LANG=en_US.UTF-8 LANGUAGE=en_US:en LC_ALL=en_US.UTF-8
