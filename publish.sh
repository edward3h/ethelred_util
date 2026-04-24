#!/usr/bin/env bash
set -euo pipefail

./gradlew \
    :common:publishOSSRHPublicationToProjectLocalRepository \
    :picocli:publishOSSRHPublicationToProjectLocalRepository

./gradlew \
    :common:zipMavenCentralPortalPublication \
    :picocli:zipMavenCentralPortalPublication

./gradlew \
    :common:releaseMavenCentralPortalPublication \
    :picocli:releaseMavenCentralPortalPublication
