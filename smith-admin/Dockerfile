# This file is a template, and might need editing before it works on your project.
FROM harbor.cloudminds.com/library/oraclejdk:Ubuntu.CM-Beta-1.3
ARG version 


ENV TZ=GMT
ENV MODULE=data-smith-admin
ENV RELEASE=$version
ENV LOGPATH=/logs/data-smith-admin


WORKDIR /var/java/
COPY . /var/java/


COPY startup.sh /etc/services.d/data-smith-admin/run

