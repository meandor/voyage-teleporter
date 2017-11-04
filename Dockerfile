FROM openjdk:jre
COPY build/distributions/transporter /usr/src/transporter
WORKDIR /usr/src/transporter
USER nobody
CMD ["/usr/src/transporter/bin/transporter"]