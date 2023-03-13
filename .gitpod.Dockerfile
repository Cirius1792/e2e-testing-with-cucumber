FROM gitpod/workspace-full:2023-03-06-18-43-51

SHELL ["/bin/bash", "-c"]
RUN source "/home/gitpod/.sdkman/bin/sdkman-init.sh"  \
    && sdk install java 17.0.4.1-tem < /dev/null