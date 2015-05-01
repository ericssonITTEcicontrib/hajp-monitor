FROM nimmis/java-centos:oracle-8-jdk

RUN mkdir /code
WORKDIR /code

ADD hajp-monitor-deploy /code
ADD createConf.sh /code/

EXPOSE 2552
EXPOSE 9000

CMD ["/bin/bash", "createConf.sh"]
