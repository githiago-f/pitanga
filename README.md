<div style="text-align: center;">
    <img src="./docs/pitanga-icon.svg" alt="Pitanga Icon" style="height: 64px;"/>
    <h1>Pitanga</h1>
</div>

## Overview

Pitanga provides secure API for executing untrusted code. It's designed to power educational institutions on code teaching,
ensuring user code cannot harm the host system.

### Features

#### Key tecnical features

- **Multi-Language:** Execute code in Java, Go, Python, NodeJS, C and more.
- **Hardened-Isolation:** Laverage `cgroups` (v2) when available for resource limits (CPU, memory, disk I/O, PIDs) and isolation.
- **Resource Limiting:** Prevent DoS attacks by enforcing strict limits on memory, execution time and process count.
- **Security First:** Runs code as an unprivileged user (`pitanga`) within a minimal, read-only filesystem chroot/jail.
- **Simple REST API:** Easy integration with front-end applications.
- **Services Oriented:** Has a services-oriented architecture that allows for easy composition and fast development.

#### Key educational features

- **Problem Based Learning:** The service is built around a PBL pedagogy. Presenting users with real-world coding challenges
  and problems. This promotes critical thinking, problem decomposition, and the practical application of programming concepts,
  moving beyond passive learning to active skill development.
- **M-Learning:** The lightweight REST API ensures seamless performance on mobile devices. Users can practice coding, submit
  solutions, and receive instant feedback directly from their smartphones or tablets, making skill development possible anywhere
  anytime.
- **Accessible Skill Practice:** Removes the barrier to entry for coding practice. Users only need a browser and an internet
  connection to write and execute code in multiple languages, without the need to set up a local development environment.

### Architecture

#### Composition

#### Security

## Installation

### Prerequisites

### Setup
