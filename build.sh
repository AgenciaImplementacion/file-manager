#!/bin/bash -ex

# rationale: set a default JENKINS_DEPLOY option
if [ -z "$JENKINS_DEPLOY" ]; then
  JENKINS_DEPLOY='no'
fi

# rationale: JENKINS_DEPLOY is enable in Jenkins
if [ "$JENKINS_DEPLOY" == "yes" ]
then
  # Opcional, configura la aplicación descifrando secretos.
  rsaconfigcipher -P $JENKINS_SECRET_KEY src/main/resources/localfiles.properties.rsa
fi

# rationale: this generate a file build/libs/file-manager-0.1.0.jar
gradle clean build # o gradle assemble?
