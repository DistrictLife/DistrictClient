#!/bin/sh
APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`
GRADLE_OPTS="${GRADLE_OPTS:-""}"
GRADLE_USER_HOME="${GRADLE_USER_HOME:-"$HOME/.gradle"}"
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
exec "$JAVACMD" "${JVM_OPTS[@]}" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
